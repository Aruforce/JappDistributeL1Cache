package com.aruforce.jappl1cache.api.inter;

import com.aruforce.jappl1cache.api.enums.CacheLineState;

/**
 * CacheLIne interface
 * @author Aruforce
 * @since 0.0.1
 */
public interface CacheLine{
	/**
	 * check state of  cache Line
	 * @param state
	 * @return
	 */
	public boolean isStateOf(CacheLineState state);
	
	/**
	 * CacheKey
	 * @return
	 */
	public String getKey();
	
	/**
	 * getCacheValue
	 * @return
	 */
	public String getValue();
	
	/**
	 * update CacheValue
	 * @return
	 */
	public void setValue(String value);

	/**
	 * update State
	 * @param state
	 */
	public void setState(CacheLineState state);

	/**
	 * get State
	 * @return
	 */
	public CacheLineState getState();
}
