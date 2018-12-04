package com.aruforce.jappl1cache.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.aruforce.jappl1cache.util.ConfigableParamNames.APPLIACTION_NAME;

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
     * 获取系统名称
     * @return
     */
    public static String getApplicationName(){
        return properties.get(APPLIACTION_NAME).toString();
    }

    /**
     * 获取redis的IP
     * @return
     */
    public static String getRedisHost() {
        return properties.getProperty(ConfigableParamNames.REDIS_HOST.toString());
    }

    /**
     * 获取redis的Port
     * @return
     */
    public static int getRedisPort() {
        return Integer.valueOf(properties.getProperty(ConfigableParamNames.REDIS_PORT.toString()));
    }

    /**
     * 获取redis的password
     * @return
     */
    public static String getRedisPass() {
        String pass = properties.getProperty(ConfigableParamNames.REDIS_PASSWORD.toString());
        return (null ==pass || "".equals(pass))? null:pass;
    }

    /**
     * 获取redis的time out
     * @return
     */
    public static int getRedisTimeOut() {
        return Integer.valueOf(properties.getProperty(ConfigableParamNames.REDIS_TIME_OUT.toString()));
    }

    /**
     * 获取lock的默认过期时间
     * @return
     */
    public static int getLockExpireTime() {
        return Integer.valueOf(properties.getProperty(ConfigableParamNames.LOCK_EXPIRE_TIME.toString()));
    }

    /**
     * 获取lock的尝试次数
     * @return
     */
    public static int getTryLockCounts() {
        return Integer.valueOf(properties.getProperty(ConfigableParamNames.LOCK_TRY_COUNT.toString()));
    }
    /**
     * 获取lock的尝试获取锁的尝试间隔
     * @return
     */
    public static int getTryLockTimeInterval() {
        return Integer.valueOf(properties.getProperty(ConfigableParamNames.LOCK_TRY_TIME_INTERVAL.toString()));
    }
}
