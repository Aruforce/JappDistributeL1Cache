package com.aruforce.jappl1cache;

import com.aruforce.jappl1cache.spi.DistributedLock;
import com.aruforce.jappl1cache.util.JedisPoolUtil;
import com.aruforce.jappl1cache.util.SystemConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;

/**
 * DistributedLockUtil
 * @author Aruforce
 * @since 0.0.1
 */
public class DistributedLockUtil implements DistributedLock {

	private static final Logger logger = LoggerFactory.getLogger(DistributedLockUtil.class);
	private static final DistributedLockUtil instance = new DistributedLockUtil();
	private static final String SET_SUCCESS = "OK";
	private static final DateFormat SPDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	private int lockExpireTime;
	private int tryLockCounts;
	private int tryTimeInterval;

	private JedisPool jedisPool;

	private DistributedLockUtil(){
		jedisPool = JedisPoolUtil.getJedisPool();
		lockExpireTime = SystemConfigUtil.getRedisTimeOut();
		tryLockCounts = SystemConfigUtil.getTryLockCounts();
		tryTimeInterval = SystemConfigUtil.getTryLockTimeInterval();
	}

	public static DistributedLock getInstance(){
		return instance;
	}
	/**
	 * 尝试获取锁，如果暂时获取不到，
	 * 等待后重试
	 * @param key
	 * @param lockId
	 * @return
	 */
	@Override
	public boolean tryLock(String key, String lockId){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Long firstTryTime = System.currentTimeMillis();
			do {
				if (SET_SUCCESS.equals(jedis.set(key, lockId, "NX", "EX", lockExpireTime))) {
					logger.debug("Redis Lock key :"+key+",value : "+lockId);
					return true;
				}
				logger.debug("Redis lock failure,waiting to try next");
				try {
					Thread.sleep(tryTimeInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			} while (System.currentTimeMillis()-firstTryTime<tryLockCounts*tryTimeInterval);

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			jedisPool.returnResource(jedis);
		}
		return  false;
	}

	/**
	 * 获取锁，如果获取不到则返回，不仅进行重试
	 * @param key
	 * @param lockId
	 * @return
	 */
	@Override
	public  boolean lock(String key, String lockId){
		Jedis jedis = null;

		try {
			jedis=jedisPool.getResource();
			if (SET_SUCCESS.equals(jedis.set(key, lockId, "NX", "EX", lockExpireTime))) {
				logger.debug("Redis Lock key :"+key+", value : "+lockId);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			jedisPool.returnResource(jedis);
		}
		return false;
	}

	/**
	 *
	 * @param key
	 * @param lockId
	 * @return
	 */
	@Override
	public  boolean unLock(String key,String lockId){
		Long RELEASE_SUCCESS = 1L;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String command = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
			if (RELEASE_SUCCESS.equals(jedis.eval(command, Collections.singletonList(key), Collections.singletonList(lockId)))) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return false;
	}
}
