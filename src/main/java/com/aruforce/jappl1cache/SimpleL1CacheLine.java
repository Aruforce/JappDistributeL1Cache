package com.aruforce.jappl1cache;

import com.aruforce.jappl1cache.inter.CacheLine;
import com.aruforce.jappl1cache.enums.CacheLineState;

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
