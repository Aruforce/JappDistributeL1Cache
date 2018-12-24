package com.aruforce.jappl1cache.api;


import com.aruforce.jappl1cache.api.enums.CacheLineState;
import com.aruforce.jappl1cache.api.inter.CacheLine;

/**
 * Cache的简单实现
 * @author Aruforce
 *
 */
public final class SimpleL1CacheLine implements CacheLine {
	private CacheLineState state;
	private String key;
	private String value;
	

	public SimpleL1CacheLine(CacheLineState state, String key, String value) {
		super();
		this.state = state;
		this.key = key;
		this.value = value;
	}
	@Override
	public boolean isStateOf(CacheLineState state) {
		return this.state.equals(state);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public void setState(CacheLineState state) {
		this.state = state;
	}
	@Override
	public CacheLineState getState() {
		return state;
	}
	
	
}
