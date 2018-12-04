package com.aruforce.jappl1cache.exceptions;

import com.aruforce.jappl1cache.enums.CacheEventType;

/**
 * Invalid CacheEvent,this should not happen,if happened ,please report bug
 * @author Aruforce
 * @since  0.0.1
 */
public class InvalidCacheEventTypeException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public InvalidCacheEventTypeException() {
    }
    public InvalidCacheEventTypeException(CacheEventType type) {
        super("Invalid CacheEventType"+type);
    }
}
