package com.aruforce.jappl1cache.api.inter;

import com.aruforce.jappl1cache.api.enums.CacheEventType;

/**
 * CacheEvent interface:
 * must has Id ,type, about which key,and whether need response;
 * if is response for other events, then RequestEventId must not be null'
 * ReadMiss，ReadInvalid;Miss & Invalid need CacheEvent BroadCastToAllJVM
 * WriteMiss,WriteInvalid,Modified; this all need to BroadCastToAllJVM
 * @author Aruforce
 * @since 0.0.1
 */
public interface CacheEvent {
	/**
	 * getEventId
	 * has to be unique
	 * @return
	 */
	public String getEventId();
	/**
	 * getEventType
	 * @see CacheEventType
	 * @return
	 */
	public CacheEventType getEventType();
	/**
	 * getCacheKey
	 * @return
	 */
	public String getCacheKey();
	/**
	 * the eventId which this eveny respose
	 * @return
	 */
	public  String getRequestEventId();
	
	/**
	 * whether need response
	 * @return
	 */
	public boolean needResponse();
	
}
