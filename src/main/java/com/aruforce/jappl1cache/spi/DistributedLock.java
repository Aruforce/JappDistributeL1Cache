package com.aruforce.jappl1cache.spi;
/**
 * DistributedLock
 * @author Aruforce
 * @since 0.0.1 
 */
public interface DistributedLock {
	/**
	 * try lock one key with given value;
	 * must success
	 * @param key
	 * @param lockId
	 * @return
	 */
	public boolean tryLock(String key,String lockId);
	/**
	 * lock one key with given value for one time;
	 * @param key
	 * @param lockId
	 * @return
	 */
	public boolean lock(String key,String lockId);
	/**
	 * unLock one key with given value ;
	 *
	 * @param key
	 * @param lockId
	 * @return
	 */
	public boolean unLock(String key,String lockId);
}
