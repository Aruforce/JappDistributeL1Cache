# 1.介绍

	这是一个基于Redis的分布式的本地缓存(String:String)一致性框架(作用以及用途和MESI协议差不多),但是性能不高(:doge;

# 2.架构

### 2.1基本组件

1. L1CacheController：本地缓存控制器，提供操作界面进行缓存读和写,同时是缓存事件总线的监听器用于处理外部的缓存事件
2. L1Cache：本地缓存
3. RemoteCache：外部缓存，默认为Redis实现
4. CacheEventBus: 缓存事件总线，JVM APP 内部负责分发接收到的事件给事件监听器(比如缓存控制器)，并向外部Topic发送自己发出的事件
5. Topic：接收JVM app CacheEventBus的Pub\Sub

### 2.2 概念

1. 本地缓存：JVM APP自己的HashMap，key为ClassName+DataId，Value为缓存行
2. 缓存行：有缓存行状态和缓存的数据组成
3. 缓存事件: 对本地缓存进行读写的时，根据不同的状态需要发布事件进行本地缓存和外部缓存的同步

# 3.使用

//TODO