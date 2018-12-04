package com.aruforce.jappl1cache.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 外部缓存Acess锁KeyUtil
 *
 * @author Aruforce
 */
public class CacheLineDistributeAcessLockKey {
    private static Logger logger = LoggerFactory.getLogger(CacheLineDistributeAcessLockKey.class);

    private final static String system = SystemConfigUtil.getApplicationName();
    private final static String AccessPrefix = "CacheLineDistributeAccessLockKey:";
    private final static String LoadedPrefix = "CacheLineDistributeLoadedLockKey:";
    private final static String splitmark = ":";

    /**
     * determine a key is on modifying by other jvm (same app) key name
     * @param key
     * @return
     */
    public static String getCacheDataAccessLockKey(String key){
        if (null==system||"".equals(system)){
            logger.warn("it's highly recommended to use an system name,or there is a threaten to your data and application");
        }
        return system+splitmark+AccessPrefix+key;
    }

    /**
     * determine a key has been load by other jvm (same app) key name
     * @param key
     * @return
     */
    public static String getCacheDataLoadedLockKey(String key){
        if (null==system||"".equals(system)){
            logger.warn("it's highly recommended to use an system name,or there is a threaten to your data and application");
        }
        return system+splitmark+LoadedPrefix+key;
    }
}
