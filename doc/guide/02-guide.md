**1、配置多线程池。**
在应用的CLASSPATH的任意路径（如：应用的classes目录）下新建一个threadpool4j.xml的配置文件，其内容为：
```xml
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
**2、启动多线程池。**在应用的启动过程中执行线程池的初始化操作。
```java
ThreadPool.getInstance().init();   // 只需执行一次
```
输出的日志类似如下：
<pre>
2014-03-31 21:13:13,925 INFO  initialization 2 thread pool successfully
</pre>
**3、向不同的线程池提交异步任务。**
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
**4、关闭多线程池。**在应用关闭时执行线程池的资源释放操作，释放资源的过程会将队列中的异步任务都执行完成。
```java
ThreadPool.getInstance().destroy();
```
输出的日志类似如下：
<pre>
2014-03-31 21:16:48,512 INFO  shutdown the thread pool other
2014-03-31 21:16:48,513 INFO  shutdown the thread pool default
</pre>