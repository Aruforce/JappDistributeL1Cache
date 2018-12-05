package com.aruforce.jappl1cache;

import com.aruforce.jappl1cache.spi.Topic;
import com.aruforce.jappl1cache.util.JedisPoolUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

/**
 *
 * @author Aruforce
 * @since 0.0.1
 */
public class TopicUtil implements Topic {

    private static final Topic topic  = new TopicUtil();

    private TopicUtil() {
        jedisPool = JedisPoolUtil.getJedisPool();
    }

    public static Topic getInstance(){
        return topic;
    }

    private JedisPool jedisPool;

    /**
     * 向某个Topic 发送消息
     * @param topic
     * @param Msg
     */
    @Override
    public void pubMsg(String topic,String Msg){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.publish(topic, Msg);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 订阅消息
     * @param pubSub
     * @param topic
     */
    @Override
    public void subTopic(JedisPubSub pubSub,String topic){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.subscribe(pubSub,topic);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedisPool.returnResource(jedis);
        }
    }
}