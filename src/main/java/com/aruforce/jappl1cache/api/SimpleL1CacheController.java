package com.aruforce.jappl1cache.api;

import com.alibaba.fastjson.JSON;
import com.aruforce.jappl1cache.api.enums.CacheEventType;
import com.aruforce.jappl1cache.api.enums.CacheLineState;
import com.aruforce.jappl1cache.api.exceptions.CacheNotExistsException;
import com.aruforce.jappl1cache.api.inter.*;
import com.aruforce.jappl1cache.api.life.LifeCycle;
import com.aruforce.jappl1cache.api.spi.DistributedLock;
import com.aruforce.jappl1cache.api.spi.RemoteCache;
import com.aruforce.jappl1cache.api.util.CacheEventResponseLockKey;
import com.aruforce.jappl1cache.api.util.CacheLineDistributeAcessLockKey;
import com.aruforce.jappl1cache.api.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单的缓存控制器实现：目前先设置单线程模式,下一步需要更新提供更高的性能
 * 除去提供L1Cache的读写功能外还要提供事件总线的监听功能
 *
 * @author Aruforce
 */
public class SimpleL1CacheController implements L1CacheController,LifeCycle {
    private Logger logger = LoggerFactory.getLogger(SimpleL1CacheController.class);
	
    private static final SimpleL1CacheController cacheController = new SimpleL1CacheController();

    private volatile boolean isStarted = false;
    /**
     * L1Cache 本地缓存实现
     */
    private L1Cache l1cache;
    /**
     * 缓存事件消息总线
     */
    private CacheEeventBus cacheEeventBus;

    /**
     * 自己发出的缓存事件消息列表
     */
    private ConcurrentHashMap<String, CacheEvent> pubedEvents;
    /**
     * 分布式锁
     */
    private DistributedLock distributedLock;

    /**
     * 外部缓存
     */
    private RemoteCache remoteCache;

    private SimpleL1CacheController() {
        l1cache = SimpleL1Cache.getInstance();
        cacheEeventBus = SimpleCacheEventBus.getInstance();
        cacheEeventBus.registerCacheEventListner(this);
        pubedEvents = new ConcurrentHashMap<String, CacheEvent>();
        distributedLock = DistributedLockUtil.getInstance();
        remoteCache = RemoteCacheUitl.getInstance();
    }

    public static L1CacheController getL1CacheController() {
        return cacheController;
    }

    /**
     * do start logic
     */
    @Override
    public void Start() {
        getL1CacheController();
    }

    /**
     * check if is start
     *
     * @return
     */
    @Override
    public boolean isStart() {
        return false;
    }

    /**
     * do stop logic
     */
    @Override
    public void stop() {

    }

    @Override
    public synchronized String getCache(String key) throws CacheNotExistsException {
        CacheLine cacheLine = l1cache.ReadCacheLine(key);
        if (null == cacheLine) {
            logger.debug("CacheKey Read Miss key={}",key);
            String loadedKey = CacheLineDistributeAcessLockKey.getCacheDataLoadedLockKey(key);
            String loadedValue = "loaded";
            boolean lock = distributedLock.lock(loadedKey,loadedValue);
            logger.debug("CacheKey {} has bean loaded?{}",key,!lock);
            if (lock) {
                String cacheData = loadCacheDataFromRedis(key);
                logger.debug("CacheKey first time to Read,key={}，value={}",key,cacheData);
                if (null == cacheData){
                    logger.error("load cacheData from redis is Failed,value is null");
                    distributedLock.unLock(loadedKey, loadedValue);
                    throw new CacheNotExistsException("L1Cache Nor Redis contains value for "+key+",please check");
                }
                l1cache.WriteCacheLine(new SimpleL1CacheLine(CacheLineState.Exclusive,key,cacheData));
                return cacheData;
            } else {
                logger.debug("do key Read BroadCast and wait for response，key={}",key);
                CacheEvent event = new SimleCacheEvent(UUIDUtil.getUuid(), CacheEventType.ReadMiss,key,null,true);
                doCacheEventBoradCast(event);
                String cacheData = loadCacheDataFromRedis(key);
                l1cache.WriteCacheLine(new SimpleL1CacheLine(CacheLineState.Exclusive,key,cacheData));
                return cacheData;
            }
        } else if (cacheLine.isStateOf(CacheLineState.Invalid)) {
            logger.debug("CacheKey Read Invalid key={}",key);
            CacheEvent event = new SimleCacheEvent(UUIDUtil.getUuid(), CacheEventType.ReadInValid,key,null,true);
            doCacheEventBoradCast(event);
            String cacheData = loadCacheDataFromRedis(key);
            l1cache.WriteCacheLine(new SimpleL1CacheLine(CacheLineState.Shared,key,cacheData));
            return cacheData;
        }
        return cacheLine.getValue();
    }

    @Override
    public synchronized boolean setCache(String key,String value) {
       logger.debug("ensure cache state correct  before write,key = {}",key);
        try {
            getCache(key);
        } catch (CacheNotExistsException e) {
            logger.info("first time write cache key={}",key);
            SimpleL1CacheLine simpleL1CacheLine = new SimpleL1CacheLine(CacheLineState.Modified, key, value);
            l1cache.WriteCacheLine(simpleL1CacheLine);
            syncCachelineToRedisAndDoEventBroadCast(simpleL1CacheLine,new SimleCacheEvent(UUIDUtil.getUuid(), CacheEventType.Modified,key, null, false));
            return true;
        }
        CacheLine cacheLine = l1cache.ReadCacheLine(key);
        synchronized (cacheLine){
            SimpleL1CacheLine simpleL1CacheLine = new SimpleL1CacheLine(CacheLineState.Modified, key, value);
            l1cache.WriteCacheLine(simpleL1CacheLine);
            syncCachelineToRedisAndDoEventBroadCast(simpleL1CacheLine,new SimleCacheEvent(UUIDUtil.getUuid(), CacheEventType.Modified,key, null, false));
        }
        return true;
    }

    /**
     * 不能是同步方法,其他的读写线程是同步的话，可能会wait在其他的某个cache事件上同时持有本对象的锁
     * 事件响应完成后，需要线程来notify 这个wait的线程
     *
     * @param event
     */
    @Override
    public void handleCacheEvent(CacheEvent event) {
        logger.debug("begin to handle cacheEvent ={}",JSON.toJSON(event));
        if (pubedEvents.containsKey(event.getEventId())) {
            logger.debug("event from self,discard it");
            return;
        } else {
            String cacheKey = event.getCacheKey();
            final CacheLine cacheLine = l1cache.ReadCacheLine(cacheKey);
            final CacheEventType eventType = event.getEventType();
            if (null == cacheLine) {
                logger.debug("have no replicate of key ={},discard it",cacheKey);
                return;
            }
            //保持在当前缓存行上面的线程同步
            synchronized (cacheLine){
                switch (eventType){
                    case ReadMiss:
                        doReadMissReposne(cacheLine,event);
                        return;
                    case ReadInValid:
                        doReadInValidReposne(cacheLine,event);
                        return;
                    case Modified:
                        deModifiedResponse(cacheLine,event);
                    case CacheEventResponse:
                        doCacheEventResponse(cacheLine,event);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    /**
     * 当受到CacheMiss的响应时
     * @param cacheLine
     * @param event
     */
    private void doReadMissReposne(CacheLine cacheLine, CacheEvent event) {
        logger.debug("cache ReadMiss request received");
        if (cacheLine.isStateOf(CacheLineState.Exclusive)) {
            logger.debug("cacheline is stateof Exclusive,update to shared and do response");
        	cacheLine.setState(CacheLineState.Shared);
            l1cache.WriteCacheLine(cacheLine);
            CacheEvent response = new SimleCacheEvent(UUIDUtil.getUuid(), CacheEventType.CacheEventResponse, event.getCacheKey(), event.getEventId(), false);
            doCacheEventBoradCast(response);
            return;
        }
        if (cacheLine.isStateOf(CacheLineState.Shared)) {
            logger.info("");
            String responsekey = CacheEventResponseLockKey.getCacheEventReponseLockKey(event);
            String responselocked = "locked";
            boolean lock = distributedLock.lock(responsekey, responselocked);
            if (lock) {
                logger.debug("cacheline is stateof Shared ,now do ReadMiss Event response");
                CacheEvent response = new SimleCacheEvent(UUIDUtil.getUuid(), CacheEventType.CacheEventResponse, event.getCacheKey(), event.getEventId(), false);
                doCacheEventBoradCast(response);
                return;
            } else {
                logger.debug("cacheline is stateof Shared ,ReadMiss Event had been responsed,discard it");
                return;
            }
        }
        if (cacheLine.isStateOf(CacheLineState.Modified)) {
            logger.debug("cacheline is stateof Modified ,now do ReadMiss Event response");
            CacheEvent response = new SimleCacheEvent(UUIDUtil.getUuid(), CacheEventType.CacheEventResponse, event.getCacheKey(), event.getEventId(), false);
            syncCachelineToRedisAndDoEventBroadCast(cacheLine, response);
            return;
        }
        if (cacheLine.isStateOf(CacheLineState.Invalid)) {
            logger.debug("cacheline is stateof Invalid ,discard it");
            return;
        }
    }

    /**
     *
     * @param cacheLine
     * @param event
     */
    private void doReadInValidReposne(CacheLine cacheLine, CacheEvent event) {
        logger.debug("cache ReadInValid request received");
        if (cacheLine.isStateOf(CacheLineState.Modified)) {
            CacheEvent response = new SimleCacheEvent(UUIDUtil.getUuid(), CacheEventType.CacheEventResponse, event.getCacheKey(), event.getEventId(), false);
            syncCachelineToRedisAndDoEventBroadCast(cacheLine, response);
            return;
        }else if (cacheLine.isStateOf(CacheLineState.Invalid)) {
            return;
        }else {
            logger.error("!!!!!this should not appear, please report to author,thanks a lot!!!!!");
        }
    }

    /**
     *
     * @param cacheLine
     * @param event
     */
    private void deModifiedResponse(CacheLine cacheLine, CacheEvent event) {

    	CacheLine l1 = new SimpleL1CacheLine(CacheLineState.Invalid,cacheLine.getKey(),cacheLine.getValue());
        l1cache.WriteCacheLine(l1);
        return;
    }

    /**
     * 对接收到的事件做出响应
     * @param cacheLine
     * @param event
     */
    private void doCacheEventResponse(CacheLine cacheLine, CacheEvent event) {
        //对cacheEvent的响应
        CacheEvent pubedCacheEvent = pubedEvents.get(event.getRequestEventId());
        //不是对自己出的事件的响应
        if (null == pubedCacheEvent) {
            return;
        }
        synchronized (pubedCacheEvent) {//通知继续运行
            pubedCacheEvent.notifyAll();
            //释放pubedCacheEvent的响应锁
            String key = CacheEventResponseLockKey.getCacheEventReponseLockKey(pubedCacheEvent);
            String value = "locked";
            distributedLock.unLock(key,value);
            return;
        }
    }

    /**
     * 发送事件广播，等待响应
     *
     * @param event
     */
    private void doCacheEventBoradCast(final CacheEvent event) {
        logger.debug("now do CacheEvent BroadCast,event = {}", JSON.toJSON(event));
        if (event.needResponse()) {
            synchronized (event) {
                try {
                    cacheEeventBus.pubCacheEvent(event);
                    pubedEvents.put(event.getEventId(), event);
                    event.wait(200);
                    logger.debug("reponse received or max waiting time reached");
                    pubedEvents.remove(event.getEventId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            cacheEeventBus.pubCacheEvent(event);
        }
    }

    /**
     * 将将缓存数据写到Redis,需要保证同时只有一个JVM在完成写流程,并做事件广播
     * @param cacheline
     * @param event
     */
    private void syncCachelineToRedisAndDoEventBroadCast(CacheLine cacheline, CacheEvent event) {
        logger.debug("sync CacheData to Redis and do EventBroadCast,key={},value={}",cacheline.getKey(),cacheline.getValue());
        String cachelineAccessKey = CacheLineDistributeAcessLockKey.getCacheDataAccessLockKey(cacheline.getKey());
        String locked = "locked";
        boolean lock = distributedLock.tryLock(cachelineAccessKey, locked);
        if (lock) {
            logger.debug("get Cache Key ={} access Lock sucess",cacheline.getKey());
            remoteCache.WriteCacheData(cacheline.getKey(),cacheline.getValue());
            doCacheEventBoradCast(event);
            distributedLock.unLock(cachelineAccessKey, locked);
        }
    }

    /**
     * 从Redis读取缓存数据,并写入本地缓存
     * @param key
     * @return
     */
    private String loadCacheDataFromRedis(String key) {
        String cachelineAccessKey = CacheLineDistributeAcessLockKey.getCacheDataAccessLockKey(key);
        String locked = "locked";
        boolean lock = distributedLock.tryLock(cachelineAccessKey, locked);
        String  cacheData = null;
        if (lock) {
            logger.debug("get Cache Key ={} access Lock sucess?",key,lock);
            cacheData = remoteCache.ReadCacheData(key);
            boolean b = distributedLock.unLock(cachelineAccessKey, locked);
            logger.debug("release Cache Key ={} access Lock sucess?",key,b);
        }
        return cacheData;
    }
}