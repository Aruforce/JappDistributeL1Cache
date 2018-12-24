package com.aruforce.jappl1cache.api.spi;

import redis.clients.jedis.JedisPubSub;

/**
 * an topic
 * @author Aruforce
 * @since 0.0.1
 */
public interface Topic {
    /**
     * regist a Suber to a topic
     * @param pubSub
     * @param topic
     */
    public void subTopic(JedisPubSub pubSub, String topic);

    /**
     * pub a msg to a topic
     * @param topic
     * @param Msg
     */
    public void pubMsg(String topic,String Msg);
}
