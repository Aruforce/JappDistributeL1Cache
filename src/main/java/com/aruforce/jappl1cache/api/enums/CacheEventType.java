package com.aruforce.jappl1cache.api.enums;
/**
 * CacheEventType
 * @author Aruforce
 * @since 0.0.1
 */
public enum CacheEventType {
	/**
	 * Read Cache From L1Cache,if null then ReadMiss happen
	 */
	ReadMiss,
	/**
	 * Read Cache From L1Cache,if cacheLine is stateOf Invalid then ReadInValid happen
	 */
	ReadInValid,
	/**
	 * OnCacheWrite ,then Modified event will dispatch or receive
	 */
	Modified,
	/**
	 * in some cases,one Jvm dispatch an read Event and need repsonse,then CacheEventResponse will be  received
	 */
	CacheEventResponse;
}
