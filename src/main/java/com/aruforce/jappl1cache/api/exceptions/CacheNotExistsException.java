package com.aruforce.jappl1cache.api.exceptions;
/**
 * cache not exist ,no licache nor redis
 * @author Aruforce
 * @since 0.0.1
 */
public class CacheNotExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CacheNotExistsException() {
		super();
	}
	public CacheNotExistsException(String message) {
		super(message);
	}
	
}
