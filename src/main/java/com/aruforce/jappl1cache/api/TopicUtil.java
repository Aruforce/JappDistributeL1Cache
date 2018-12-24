package com.aruforce.jappl1cache.api;

import com.aruforce.jappl1cache.api.spi.Topic;
import com.aruforce.jappl1cache.api.util.JedisPoolUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;

/**
 *
 * @author Aruforce
 * @since 0.0.1
 */
public class TopicUtil implements Topic {

    private static final Topic topic  = new TopicUtil();

    private TopicUtil() {
        jedisPool = JedisPoolUtil.getJedisPool();
        puber = jedisPool.getResource();
        suber = jedisPool.getResource();
    }

    public static Topic getInstance(){
        return topic;
    }

    private JedisPool jedisPool;
    private Jedis puber;
    private Jedis suber;
    /**
     * 向某个Topic 发送消息
     * @param topic
     * @param Msg
     */
    @Override
    public void pubMsg(String topic,String Msg){
        try {
            puber.publish(topic, Msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅消息,
     * @param pubSub
     * @param topic
     */
    @Override
    public void subTopic(JedisPubSub pubSub,String topic){
        try {
            suber.subscribe(pubSub,topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this should return resource to the pool
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        jedisPool.returnResource(puber);
        jedisPool.returnResource(suber);
        super.finalize();
    }
}