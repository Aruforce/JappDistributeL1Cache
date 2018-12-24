package com.aruforce.jappl1cache.api.inter;

/**
 * CacheEventListener will handle all events received from redis;
 * generally,will change the state of cacheLine and or do response;
 * @author Aruforce
 * @since  0.0.1
 */
public interface CacheEventListener {
	/**
	 *  handke  event
	 * @param event
	 */
	public void handleCacheEvent(CacheEvent event);
}