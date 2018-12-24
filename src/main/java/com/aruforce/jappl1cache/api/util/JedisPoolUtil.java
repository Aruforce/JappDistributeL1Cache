package com.aruforce.jappl1cache.api.util;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * do jedis pool init
 *
 * @author Aruforce
 * @since 0.0.1
 */
public class JedisPoolUtil {

    private static JedisPool jedisPool ;
    public static JedisPool getJedisPool(){
        return  jedisPool;
    }
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setBlockWhenExhausted(true);
        config.setMaxTotal(10);
        config.setMaxIdle(5);
        config.setMinIdle(2);
        String host = SystemConfigUtil.getRedisHost();
        int port = SystemConfigUtil.getRedisPort();
        int timeout = SystemConfigUtil.getRedisTimeOut();
        String password = SystemConfigUtil.getRedisPass();
        jedisPool = new JedisPool(config,host,port,timeout,password);
    }
}
