#一、依赖
* common4j-0.2.2.jar
* commons-lang-2.6.jar
* log4j-1.2.16.jar

注：所有的依赖类库位于`lib目录`下。

#二、配置
##1、配置多个线程池
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

##2、配置日志输出
注：如果在threadpool4j.xml中开启了状态信息输出（默认开启），却没有配置专门的日志文件，将输出到应用的默认日志文件中。

**1、配置线程池状态日志输出**
```properties
# 线程池状态输出日志
log4j.logger.cn.aofeng.threadpool4j.job.ThreadPoolStateJob=INFO, threadpoolstate
log4j.additivity.cn.aofeng.threadpool4j.job.ThreadPoolStateJob=false
log4j.appender.threadpoolstate=org.apache.log4j.DailyRollingFileAppender
log4j.appender.threadpoolstate.File=/home/nieyong/logs/thread/threadpool4j-threadpoolstate.log 
log4j.appender.threadpoolstate.DatePattern='.'yyyy-MM-dd
log4j.appender.threadpoolstate.layout=org.apache.log4j.PatternLayout
log4j.appender.threadpoolstate.layout.ConversionPattern=[%d{yyyy-MM-dd HH:mm:ss}] ~ %m%n
```
注：日志输出路径"/home/nieyong/logs/thread/threadpool4j-threadpoolstate.log "由项目根据实际情况修改。

**2、配置线程状态日志输出。**
```properties
# 所有线程组中线程状态输出日志
log4j.logger.cn.aofeng.threadpool4j.job.ThreadStateJob=INFO, threadstate
log4j.additivity.cn.aofeng.threadpool4j.job.ThreadStateJob=false
log4j.appender.threadstate=org.apache.log4j.DailyRollingFileAppender
log4j.appender.threadstate.File=/home/nieyong/logs/thread/threadpool4j-threadstate.log 
log4j.appender.threadstate.DatePattern='.'yyyy-MM-dd
log4j.appender.threadstate.layout=org.apache.log4j.PatternLayout
log4j.appender.threadstate.layout.ConversionPattern=[%d{yyyy-MM-dd HH:mm:ss}] ~ %m%n
```
注：日志输出路径"/home/nieyong/logs/thread/threadpool4j-threadstate.log "由项目根据实际情况修改。

**3、配置线程堆栈日志输出。**
```properties
# 所有线程的线程堆栈输出日志
log4j.logger.cn.aofeng.threadpool4j.job.ThreadStackJob=INFO, threadstack
log4j.additivity.cn.aofeng.threadpool4j.job.ThreadStackJob=false
log4j.appender.threadstack=org.apache.log4j.DailyRollingFileAppender
log4j.appender.threadstack.File=/home/nieyong/logs/thread/threadpool4j-threadstack.log 
log4j.appender.threadstack.DatePattern='.'yyyy-MM-dd-HH
log4j.appender.threadstack.layout=org.apache.log4j.PatternLayout
log4j.appender.threadstack.layout.ConversionPattern=[%d{yyyy-MM-dd HH:mm:ss}] ~ %m%n
```
注：日志输出路径"/home/nieyong/logs/thread/threadpool4j-threadstack.log "由项目根据实际情况修改。

#三、使用线程池
##1、启动线程池

在应用的启动过程中执行线程池的初始化操作。
```java
ThreadPoolManager tpm = ThreadPoolManager.getSingleton();
tpm.init();
```

输出的日志类似如下：
<pre>
2014-03-31 21:13:13,925 INFO  initialization 2 thread pool successfully
</pre>

##2、不同场景的使用
###场景1：执行不需要返回值的异步任务
```java
ThreadPoolManager tpm = ThreadPoolManager.getSingleton();
ThreadPool threadPool = tpm.getThreadPool();

Runnable task1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("执行异步任务1");
    }
};
threadPool.submit(task1);   // 未指定线程池名称时，任务会提交到名为"default"的线程池执行

Runnable task2 = new Runnable() {
    @Override
    public void run() {
        System.out.println("执行异步任务2");
    }
};
threadPool.submit(task2, "other");   // 将task2提交到名为"other"的线程池执行
```

###场景2：执行需要返回值的异步任务
**1）编写一个实现Callable接口的异步任务类。**
```java
public class CallableAnsyTask implements Callable<Long> {
    private int[] _arr;
     
    public CallableAnsyTask(int[] arr) {
        _arr = arr;
    }
     
    @Override
    public Long call() throws Exception {
        long result = 0;
        for (int i = 0; i < _arr.length; i++) {
            result += _arr[i];
        }
         
        return result;
    }
}
```

**2）执行异步任务。**
```java
ThreadPoolManager tpm = ThreadPoolManager.getSingleton();
ThreadPool threadPool = tpm.getThreadPool();
int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
CallableAnsyTask task = new CallableAnsyTask(arr);

// 将异步任务交给默认的线程池default执行
threadPool.submit(task);
  
// 将异步任务交给指定的线程池other执行
threadPool.submit(task, "other");
```

###场景3：并行调用多个异步任务
**1）编写一个实现Callable接口的异步任务类。**
```java
public class CallableAnsyTask implements Callable<Long> {
    private int[] _arr;
     
    public CallableAnsyTask(int[] arr) {
        _arr = arr;
    }
     
    @Override
    public Long call() throws Exception {
        long result = 0;
        for (int i = 0; i < _arr.length; i++) {
            result += _arr[i];
        }
         
        return result;
    }
}
```
 
**2）并行调用多个异步任务。**
```java
// 创建多个异步任务
int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
List<Callable<Long>> tasks = new ArrayList<Callable<Long>>();
tasks.add(new CallableAnsyTask(arr));
tasks.add(new CallableAnsyTask(arr));
tasks.add(new CallableAnsyTask(arr));
 
// 并行调用多个异步任务
ThreadPoolManager tpm = ThreadPoolManager.getSingleton();
ThreadPool threadPool = tpm.getThreadPool();
List<Future<Long>> futures = threadPool.invokeAll(tasks, 1, TimeUnit.SECONDS);
for (Future<Long> future : futures) {
    Long result = future.get();   // 如果某个任务执行超时，调用该任务对应的future.get时抛出CancellationException异常
    System.out.println("并行调用，其中一个任务的执行结果为:"+result);
}
```

##3、关闭多线程池
在应用关闭时执行线程池的资源释放操作，释放资源的过程会将队列中的异步任务都执行完成。
```java
ThreadPoolManager tpm = ThreadPoolManager.getSingleton();
tpm.destroy();
```
输出的日志类似如下：
<pre>
2014-03-31 21:16:48,512 INFO  shutdown the thread pool other
2014-03-31 21:16:48,513 INFO  shutdown the thread pool default
</pre>
