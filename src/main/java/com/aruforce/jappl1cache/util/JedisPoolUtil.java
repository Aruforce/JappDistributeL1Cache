package com.aruforce.jappl1cache.util;

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
        String host = SystemConfigUtil.getRedisHost();
        int port = SystemConfigUtil.getRedisPort();
        int timeout = SystemConfigUtil.getRedisTimeOut();
        String password = SystemConfigUtil.getRedisPass();
        jedisPool = new JedisPool(config,host,port,timeout,password);
    }
}
