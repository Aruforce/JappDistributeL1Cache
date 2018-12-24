package com.aruforce.jappl1cache.api.inter;

/**
 * CacheEventBus ,
 * responsible for pub event to redis and sub from redis,
 * if cache event received from redis,then dispatch to CacheEventListner to handle it;
 * @author Aruforce
 * @since 0.0.1
 */
public interface CacheEeventBus {
	/**
	 * accept CacheEventListner
	 * 
	 * @param cacheEventListener
	 */
	public void registerCacheEventListner(CacheEventListener cacheEventListener);
	/**
	 * do cacheEvent Pub
	 * @param cacheEvent
	 */
	public void pubCacheEvent(CacheEvent cacheEvent);
}
