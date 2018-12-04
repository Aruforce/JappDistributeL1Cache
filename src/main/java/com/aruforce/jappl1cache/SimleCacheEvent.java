package com.aruforce.jappl1cache;

import com.aruforce.jappl1cache.enums.CacheEventType;
import com.aruforce.jappl1cache.inter.CacheEvent;

/**
 * 
 * @author Aruforce
 *
 */
public class SimleCacheEvent implements CacheEvent {
	private String eventId;
	private CacheEventType eventType;
	private String cacheKey;
	private String requestEventId;
	private boolean needResponse;
	
	
	public SimleCacheEvent(String eventId, CacheEventType eventType, String cacheKey, String requestEventId,boolean needResponse) {
		super();
		this.eventId = eventId;
		this.eventType = eventType;
		this.cacheKey = cacheKey;
		this.requestEventId = requestEventId;
		this.needResponse = needResponse;
	}

	@Override
	public String getEventId() {
		return eventId;
	}

	@Override
	public CacheEventType getEventType() {
		return eventType;
	}

	@Override
	public String getCacheKey() {
		return cacheKey;
	}

	@Override
	public String getRequestEventId() {
		return requestEventId;
	}

	@Override
	public boolean needResponse() {
		return needResponse;
	}
	
}
