package com.aruforce.jappl1cache.inter;

import com.aruforce.jappl1cache.exceptions.CacheNotExistsException;
import com.aruforce.jappl1cache.life.LifeCycle;

/**
 * L1Cache User Interface;
 * CacheEevent Handler;
 * @author Aruforce
 * 
 */
public interface L1CacheController extends CacheEventListener,LifeCycle {
	/**
	 * read cache from l1cache
	 * @param key
	 * @return value mapped to the key
	 * @throws CacheNotExistsException onl if value not exist in l1cache and redis
	 */
	public String getCache(String key) throws CacheNotExistsException;

	/**
	 * write cache;
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setCache(String key,String value) ;

}
