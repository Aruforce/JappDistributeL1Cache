package com.aruforce.jappl1cache;

import java.util.concurrent.ConcurrentHashMap;

import com.aruforce.jappl1cache.inter.CacheLine;
import com.aruforce.jappl1cache.inter.L1Cache;

/**
 * 简单实现L1Cache,每个JVM 只有一个Cache;
 * 每个JVM 相当于一个Core,那么一个Core只有一个L1Cache
 * L1cache无法在具体的缓存行上,做到多线程同同步:
 * 因为每个行上唯一的标识多个线程同时持有
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
