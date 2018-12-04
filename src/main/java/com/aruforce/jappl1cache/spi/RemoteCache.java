package com.aruforce.jappl1cache.spi;

/**
 * RemoteCache Interface
 * @author Aruforce
 * @since 0.0.1
 */
public interface RemoteCache{
	/**
	 * load cache from remote Cache,default is redis
	 * @param key
	 * @return
	 */
	public String ReadCacheData(String key);

	/**
	 * write cache to remote Cache,default is redis
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean WriteCacheData(String key,String value);
}
