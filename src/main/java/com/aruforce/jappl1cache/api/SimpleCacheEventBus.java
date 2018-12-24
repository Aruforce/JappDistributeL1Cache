package com.aruforce.jappl1cache.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aruforce.jappl1cache.api.inter.CacheEeventBus;
import com.aruforce.jappl1cache.api.inter.CacheEvent;
import com.aruforce.jappl1cache.api.inter.CacheEventListener;
import com.aruforce.jappl1cache.api.spi.Topic;
import com.aruforce.jappl1cache.api.util.SystemConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

/**
 * CacheEventBus
 * @author Aruforce
 * @since 0.0.1
 */
public class SimpleCacheEventBus extends JedisPubSub implements CacheEeventBus{
	Logger logger = LoggerFactory.getLogger(SimpleCacheEventBus.class);
	private static final CacheEeventBus eventBus = new SimpleCacheEventBus();
	/**
	 * registed cache event listener
	 */
	private CacheEventListener listener;
	private static final String eventTopic = "eventTopic";
 	private String systemName = SystemConfigUtil.getApplicationName();
	private Topic topic;

	private SimpleCacheEventBus(){
		topic = TopicUtil.getInstance();
	}

	public static CacheEeventBus getInstance(){
		return eventBus;
	}
	@Override
	public void registerCacheEventListner(CacheEventListener cacheEventListener) {
		this.listener  = cacheEventListener;
		//creeat a new  thread,do sub topic msg handle logic
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
		logger.debug("new Publish cachEvent to topic:{}",systemName+eventTopic);
		topic.pubMsg(systemName+eventTopic, JSONObject.toJSONString(cacheEvent));
	}

	@Override
	public void onMessage(String channel, String message) {
		logger.debug("received message from {},massage={}",channel,message);
		CacheEvent event = JSONObject.parseObject(message,SimleCacheEvent.class);
		listener.handleCacheEvent(event);
	}

}
