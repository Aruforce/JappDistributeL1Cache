package com.aruforce.jappl1cache.api;

import com.aruforce.jappl1cache.api.spi.DistributedLock;
import com.aruforce.jappl1cache.api.util.JedisPoolUtil;
import com.aruforce.jappl1cache.api.util.SystemConfigUtil;
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
	private static final String KeyExpiretCommand = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
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
	 * try lock
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
	 * lock for one time,return lock result
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
			if (RELEASE_SUCCESS.equals(jedis.eval(KeyExpiretCommand, Collections.singletonList(key), Collections.singletonList(lockId)))) {
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
