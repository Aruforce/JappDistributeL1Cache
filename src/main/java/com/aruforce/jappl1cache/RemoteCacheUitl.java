package com.aruforce.jappl1cache;

import com.aruforce.jappl1cache.spi.RemoteCache;
import com.aruforce.jappl1cache.util.JedisPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * 外部缓存实现
 * @author Aruforce
 * @since 0.0.1
 */
public class RemoteCacheUitl implements RemoteCache {
    private static final Logger logger = LoggerFactory.getLogger(RemoteCacheUitl.class);
    private static final RemoteCacheUitl instance = new RemoteCacheUitl();
    private static final String SET_SUCCESS = "OK";

    private static JedisPool jedisPool;

    private RemoteCacheUitl() {
        jedisPool = JedisPoolUtil.getJedisPool();
    }

    public static  RemoteCache getInstance(){
        return instance;
    }

    @Override
    public String ReadCacheData(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedisPool.returnResource(jedis);
        }

        return null;
    }

    @Override
    public boolean WriteCacheData(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (SET_SUCCESS.equals(jedis.set(key, value))) {
                logger.debug("redis set key : "+key+",value : "+value);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedisPool.returnResource(jedis);
        }
        return false;
    }
}
