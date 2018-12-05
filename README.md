# 1.Introduction
> 0. This is a framework based on redis,work as MESI Protocol;
> 1. L1Cache restore only String data ,just like Map<String,String>

# 2.Concept

## 2.1 Basic Components

### 2.1.1 L1CacheController：
 > 0. providing an interface for cache read and write, 
 > 1. regist self to the cache event bus as a listener for handling external cache events
 
### 2.1.2 L1Cache：
> 0. just like Map<String,String>

### 2.1.3 RemoteCache：
> remote cache implement,default redis

### 2.1.4 CacheEventBus: 
> CacheEventBus:
> 0. Send your own events to the external Topic (redis as default);
> 1. Responsible for distributing received events to event listeners (such as cache controllers) within the JVM APP

### 2.1.5 Topic
> Accept the Pub\Sub of the JVM app CacheEventBus

## 2.2 Concepts

### 2.2.1 LocalCache
>  JVM local Cache,use like Map<String,String>

### 2.2.1 LocalCacheLine
> 0. CacheDataKey+CacheDataValue+CacheLineState
> 1. here is the state list :Modified,Exclusive,Shared,Invalid

### 2.2.1 CacheEvent
> 0. When reading and writing the local cache, 
> 1. it is necessary to issue events according to different states of local cache
> 2. to ensure cache in suitable state for use

# 3.UseAge

> please read test cases
> it's really simple
