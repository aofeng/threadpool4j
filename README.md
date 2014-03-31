threadpool4j
============

在软件架构和设计中，会尽可能地将操作异步化，缩短响应时间，提升性能。

将异步任务放入线程池，这是许多人都知道的。但是当异步任务多了之后，如果全放在同一个线程池执行，会出现一些问题：
* 不同任务因其执行的操作不同，所需时间不同。如果有大量执行时间较久的异步任务，会阻塞那些执行非常快的异步任务，导致原本很快可以完成的异步任务也变慢。
* 操作本地内容的异步任务和操作远程内容的异步任务。如果放在同一个线程池中，在网络出现故障的情况下，会出现大量的任务积压，导致执行本地内容的异步也受影响。

现实生活中，机动车行驶时，会根据行驶的速度划分超车道、快车道、慢车道和应急车道，避免速度慢的车辆阻塞速度快的车辆。
![车道划分](http://img0.ph.126.net/JgUtSzhdAatg_5B5mne0KQ==/6608414527631781351.png)

那么异步任务放入不同的线程池执行，就可以解决上面的问题。`threadpool4j`就是这一个`多线程池`的类库，使用方法如下：

1、获取threadpool4j源码。
```shell
git clone https://github.com/aofeng/threadpool4j
```
2、编译源码生成jar。

进入项目根目录，执行ant脚本：
```shell
ant
```
会生成一个dist目录，下面有两个文件。如：
<pre>
threadpool4j-1.0.0-src.jar    源码jar
threadpool4j-1.0.0.jar        用于发布的二进制jar
</pre>
它的依赖类库在lib目录下。有common4j-*.jar，log4j-1.*.jar。

3、配置多线程池。
在应用的CLASSPATH的任意路径（如：应用的classes目录）下新建一个`threadpool4j.xml`的配置文件，其内容为：
```java
<?xml version="1.0" encoding="UTF-8"?>
<threadpool4j>
    <!-- 至少要有一个线程池default -->
    <pool name="default">
        <corePoolSize>30</corePoolSize>
        <maxPoolSize>150</maxPoolSize>
        <!-- 线程空闲存话的时间。单位：秒 -->
        <keepAliveTime>5</keepAliveTime>
        <workQueueSize>100000</workQueueSize>
    </pool>
    
    <pool name="other">
        <corePoolSize>10</corePoolSize>
        <maxPoolSize>100</maxPoolSize>
        <keepAliveTime>10</keepAliveTime>
        <workQueueSize>10000</workQueueSize>
    </pool>
</threadpool4j>
```

4、在应用的启动过程中执行线程池的初始化操作。
```java
ThreadPool.getInstance().init();   // 只需执行一次
```
输出的日志类似如下：
<pre>
2014-03-31 21:13:13,925 INFO  initialization 2 thread pool successfully
</pre>

5、向不同的线程池提交异步任务。
```java
Runnable task1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("执行异步任务1");
    }
};
ThreadPool.getInstance().submit(task1);   // 未指定线程池名称时，任务会提交到名为"default"的线程池执行

Runnable task2 = new Runnable() {
    @Override
    public void run() {
        System.out.println("执行异步任务2");
    }
};
ThreadPool.getInstance().submit(task2, "other");   // 将task2提交到名为"other"的线程池执行
```

6、在应用关闭时执行线程池的资料释放操作，释放资源的过程会将队列中的异步任务都执行完成。
```java
ThreadPool.getInstance().destroy();
```
输出的日志类似如下：
<pre>
2014-03-31 21:16:48,512 INFO  shutdown the thread pool other
2014-03-31 21:16:48,513 INFO  shutdown the thread pool default
<pre>
