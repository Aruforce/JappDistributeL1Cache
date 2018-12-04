package com.aruforce.jappl1cache.util;

import com.aruforce.jappl1cache.inter.CacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CacheEventResponseLockKey
 * when do response for an event ,while lock this
 *
 * @author Aruforce
 * @since 0.0.1
 */
public class CacheEventResponseLockKey {
    private static Logger logger = LoggerFactory.getLogger(CacheEventResponseLockKey.class);
    private static  String system = SystemConfigUtil.getApplicationName();
    private static  String prefix = "CacheEventResponseLock:";
    private static  String splitmark=":";

    /**
     * determine a event has been response key name
     * @param event
     * @return
     */
    public static String getCacheEventReponseLockKey(CacheEvent event){
        if (null==system||"".equals(system)){
            logger.warn("it's highly recommended to use an system name,or there is a threaten to your data and application");
        }
        String cacheKey = event.getCacheKey();
        String requestEventId = event.getEventId();
        return system+splitmark+prefix+cacheKey+splitmark+requestEventId;
    }
}
