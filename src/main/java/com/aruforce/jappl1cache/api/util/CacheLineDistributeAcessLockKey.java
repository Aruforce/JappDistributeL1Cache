package com.aruforce.jappl1cache.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Key related lock key
 *
 * @author Aruforce
 * @since 0.0.1
 */
public class CacheLineDistributeAcessLockKey {
    private static Logger logger = LoggerFactory.getLogger(CacheLineDistributeAcessLockKey.class);

    private final static String SYSTEM = SystemConfigUtil.getApplicationName();
    private final static String ACCESS_PREFIX = "CacheLineDistributeAccessLockKey:";
    private final static String LOADED_PREFIX = "CacheLineDistributeLoadedLockKey:";
    private final static String SPLITMARK = ":";

    /**
     * determine a key is on modifying by other jvm (same app) key name
     * @param key
     * @return
     */
    public static String getCacheDataAccessLockKey(String key){
        if (null== SYSTEM ||"".equals(SYSTEM)){
            logger.warn("it's highly recommended to use an SYSTEM name,or there is a threaten to your data and application");
        }
        return SYSTEM + SPLITMARK + ACCESS_PREFIX +key;
    }

    /**
     * determine a key has been load by other jvm (same app) key name
     * @param key
     * @return
     */
    public static String getCacheDataLoadedLockKey(String key){
        if (null== SYSTEM ||"".equals(SYSTEM)){
            logger.warn("it's highly recommended to use an SYSTEM name,or there is a threaten to your data and application");
        }
        return SYSTEM + SPLITMARK + LOADED_PREFIX +key;
    }
}
