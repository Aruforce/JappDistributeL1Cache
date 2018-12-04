package com.aruforce.jappl1cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aruforce.jappl1cache.inter.CacheEventListener;
import com.aruforce.jappl1cache.inter.CacheEeventBus;
import com.aruforce.jappl1cache.inter.CacheEvent;
import com.aruforce.jappl1cache.spi.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

/**
 * 缓存事件总线,
 * @author Aruforce
 * @since 0.0.1
 */
public class SimpleCacheEventBus extends JedisPubSub implements CacheEeventBus{
	Logger logger = LoggerFactory.getLogger(SimpleCacheEventBus.class);
	private static final String eventTopic = "eventTopic";

 	private String systemName = "MyTest";
	private Topic topic;

	//注册的全部的事件处理器
	private CacheEventListener listener;

	@Override
	public void registerCacheEventListner(CacheEventListener cacheEventListener) {
		this.listener  = cacheEventListener;
		//创建一个线程，订阅消息及处理
		Thread t =  new Thread(new Runnable() {
			@Override
			public void run() {
				topic.subTopic(SimpleCacheEventBus.this,systemName+eventTopic);
			}
		});
		t.start();
	}

	@Override
	public void pubCacheEvent(CacheEvent cacheEvent) {
		logger.debug("now Publish cachEvent to topic;{}",systemName+eventTopic);
		topic.pubMsg(systemName+eventTopic, JSON.toJSONString(cacheEvent));
	}

	@Override
	public void onMessage(String channel, String message) {
		logger.debug("received message from {},massage={}",channel,message);
		CacheEvent event = JSONObject.parseObject(message,SimleCacheEvent.class);
		listener.handleCacheEvent(event);
	}

}
