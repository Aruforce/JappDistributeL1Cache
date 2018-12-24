package com.aruforce.jappl1cache.api.spi;

/**
 * RemoteCache Interface
 * @author Aruforce
 * @since 0.0.1
 */
public interface RemoteCache{
	/**
	 * get Cache data with a key
	 * @param key
	 * @return
	 */
	public String ReadCacheData(String key);

	/**
	 * write value to the specific key
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean WriteCacheData(String key,String value);
}
