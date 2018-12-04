package com.aruforce.jappl1cache.inter;

/**
 * store the cacheLine,not for user interface
 * one should not use this;
 * @author Aruforce
 * @since 0.0.1
 */
public interface L1Cache{
	/**
	 * getCacheLine,which contains value mapped to the key
	 * @param key
	 * @return
	 */
	public CacheLine ReadCacheLine(String key);

	/**
	 * WriteCacheLine
	 * @param cacheLine
	 * @return
	 */
	public boolean WriteCacheLine(CacheLine cacheLine);
}