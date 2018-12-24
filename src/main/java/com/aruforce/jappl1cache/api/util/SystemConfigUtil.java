package com.aruforce.jappl1cache.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Read and Load system configs
 *
 * @author Aruforce
 * @since 0.0.1
 */
public class SystemConfigUtil {
    private static final Logger logger = LoggerFactory.getLogger(SystemConfigUtil.class);
    private static final String CONFIG_NAME = "jappl1cacheconfig.properties";
    private static Properties properties ;

    static {
        properties = new Properties();
        InputStream resourceAsStream = SystemConfigUtil.class.getClassLoader().getResourceAsStream(CONFIG_NAME);
        try {
            properties.load(resourceAsStream);
            resourceAsStream.close();
        } catch (IOException e) {
            System.err.println("can not load jappl1cacheconfig.properties ,system.exit(-1)");
            System.exit(-1);
        }

    }
    /**
     * get application name
     * @return
     */
    public static String getApplicationName(){
        return properties.get(ConfigableParamNames.APPLIACTION_NAME.toString()).toString();
    }

    /**
     * get redis ip
     * @return
     */
    public static String getRedisHost() {
        return properties.getProperty(ConfigableParamNames.REDIS_HOST.toString());
    }

    /**
     * get redis port
     * @return
     */
    public static int getRedisPort() {
        return Integer.valueOf(properties.getProperty(ConfigableParamNames.REDIS_PORT.toString()));
    }

    /**
     * get redis password,if null or "" ,then null is returned
     * @return
     */
    public static String getRedisPass() {
        String pass = properties.getProperty(ConfigableParamNames.REDIS_PASSWORD.toString());
        return (null ==pass || "".equals(pass))? null:pass;
    }

    /**
     * get redis connnect time out
     * @return
     */
    public static int getRedisTimeOut() {
        return Integer.valueOf(properties.getProperty(ConfigableParamNames.REDIS_TIME_OUT.toString()));
    }

    /**
     * get default lock expire time ,in case of thread faliure (which Lead to distributed lock deadlock )
     * @return
     */
    public static int getLockExpireTime() {
        return Integer.valueOf(properties.getProperty(ConfigableParamNames.LOCK_EXPIRE_TIME.toString()));
    }

    /**
     * get the number of attempts when try lock a lock
     * @return
     */
    public static int getTryLockCounts() {
        return Integer.valueOf(properties.getProperty(ConfigableParamNames.LOCK_TRY_COUNT.toString()));
    }
    /**
     * Get the attempt interval of the lock to get the lock
     * @return
     */
    public static int getTryLockTimeInterval() {
        return Integer.valueOf(properties.getProperty(ConfigableParamNames.LOCK_TRY_TIME_INTERVAL.toString()));
    }
}
