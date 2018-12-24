package com.aruforce.jappl1cache.api;

import com.aruforce.jappl1cache.api.inter.CacheLine;
import com.aruforce.jappl1cache.api.inter.L1Cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单实现L1Cache,每个JVM APP只有一个Cache;
 * 每个JVM APP相当于一个Core,那么一个Core只有一个L1Cache
 * L1cache无法在具体的缓存行Value上做到同步;而在key上同步无意义
 * @author Aruforce
 *
 */
public class SimpleL1Cache implements L1Cache {
	private static SimpleL1Cache instance = new SimpleL1Cache();
	
	private ConcurrentHashMap<String, CacheLine> cache = new ConcurrentHashMap<String, CacheLine>();
	
	private SimpleL1Cache() {
		super();
	}
	
	public static L1Cache getInstance(){
		return instance;
	}
	
	@Override
	public CacheLine ReadCacheLine(String key) {
		return cache.get(key);
	}
	
	@Override
	public boolean WriteCacheLine(CacheLine cacheLine) {
		cache.compute(cacheLine.getKey(),(k,v)->{
			if(null == v){
				return cacheLine;
			}else{
				v.setState(cacheLine.getState());
				v.setValue(cacheLine.getValue());
				return v;
			}
		});
		return true;
	}
	
}
