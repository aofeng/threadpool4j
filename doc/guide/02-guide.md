# 配置

## 配置多个线程池

在应用的CLASSPATH的任意路径（如：应用的classes目录）下新建一个threadpool4j.xml的配置文件，其内容为：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<threadpool4j>
    <!-- 至少要有一个线程池default -->
    <pool name="default">
        <corePoolSize>10</corePoolSize>
        <maxPoolSize>100</maxPoolSize>
        <!-- 线程空闲存话的时间。单位：秒 -->
        <keepAliveTime>15</keepAliveTime>
        <workQueueSize>100000</workQueueSize>
    </pool>

    <pool name="other">
        <corePoolSize>10</corePoolSize>
        <maxPoolSize>100</maxPoolSize>
        <keepAliveTime>15</keepAliveTime>
        <workQueueSize>100000</workQueueSize>
    </pool>

    <!-- 线程池状态收集汇总配置
    switch: on-开; off-关
    interval: 单位(秒)
     -->
    <threadpoolstate switch="on" interval="60"></threadpoolstate>

    <!-- 线程状态收集汇总配置
    switch: on-开; off-关
    interval: 单位(秒)
     -->
    <threadstate switch="on" interval="60"></threadstate>
    
    <!-- 线程堆栈收集配置
    switch: on-开; off-关
    interval: 单位(秒)
     -->
    <threadstack switch="on" interval="60"></threadstack>
</threadpool4j>
```

## 配置日志输出

注：如果在threadpool4j.xml中开启了状态信息输出（默认开启），却没有配置专门的日志文件，将输出到应用的默认日志文件中。

### 配置线程池状态日志输出

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

### 配置线程状态日志输出

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

### 配置线程堆栈日志输出

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

# 使用线程池

## 初始化多线程池

在应用的启动的时候执行线程池的初始化操作。

```java
ThreadPoolManager tpm = ThreadPoolManager.getSingleton();
tpm.init();
```

输出的日志类似如下：

<pre>
2017-06-29 14:19:16,252 INFO  initialization thread pool 'other' success
2017-06-29 14:19:16,253 INFO  initialization thread pool 'default' success
2017-06-29 14:19:16,255 INFO  start job 'threadpool4j-threadpoolstate' success
2017-06-29 14:19:16,260 INFO  start job 'threadpool4j-threadstate' success
2017-06-29 14:19:16,263 INFO  start job 'threadpool4j-threadstack' success
</pre>

## 不同场景的使用

### 场景1：执行不需要返回值的异步任务

**1）编写一个实现Runnable接口的异步任务类** 

```java
public class RunnableAsynTask implements Runnable {

    // 耗时的操作（配置低一些的机器小心CPU 100％，反应慢）
    public void needSomeTime() {
        int len = 50000;
        String[] intArray = new String[len];
        Random random = new Random(len);
        
        // 初始化数组
        for (int i = 0; i < len; i++) {
            intArray[i] = String.valueOf(random.nextInt());
        }
        
        // 排序
        Arrays.sort(intArray);
    }
    
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        needSomeTime();
        long endTime = System.currentTimeMillis();
        System.out.println("执行任务耗时："+(endTime-startTime));
    }
}
```

**2）执行异步任务**

```java
ThreadPoolManager tpm = ThreadPoolManager.getSingleton();
ThreadPool threadPool = tpm.getThreadPool();

// 未指定线程池名称时，任务会提交到名为"default"的线程池执行
threadPool.submit(new RunnableAsynTask());

// 将task2提交到名为"other"的线程池执行
threadPool.submit(new RunnableAsynTask(), "other");
```

### 场景2：执行需要返回值的异步任务

**1）编写一个实现Callable接口的异步任务类**

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

**2）执行异步任务**

```java
ThreadPoolManager tpm = ThreadPoolManager.getSingleton();
ThreadPool threadPool = tpm.getThreadPool();

int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
CallableAnsyTask task = new CallableAnsyTask(arr);

// 将异步任务交给默认的线程池default执行
Future<Long> future = threadPool.submit(task);
System.out.println("异步任务在线程池default的执行结果为:"+future.get());
  
// 将异步任务交给指定的线程池other执行
threadPool.submit(task, "other");
System.out.println("异步任务在线程池other的执行结果为:"+future.get());
```

### 场景3：并行调用多个异步任务

**1）编写一个实现Callable接口的异步任务类**

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
 
**2）并行调用多个异步任务**

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

### 场景4：线程池队列满，任务提交失败时可自定义处理

```java
// 队列满时，提交失败的任务直接丢弃
threadPool.submit(new RunnableAsynTask(), "default", new DiscardFailHandler<Runnable>());

// 队列满时，提交失败的任务信息会输出ERROR日志
int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
threadPool.submit(new CallableAnsyTask(arr), "other", new LogErrorFailHandler<Callable<Long>>());
```

说明：

* 可以实现`FailHandler`接口，自行处理提交失败的任务。

## 关闭多线程池

在应用关闭时执行线程池的资源释放操作，释放资源的过程会将队列中的异步任务都执行完成。

```java
ThreadPoolManager tpm = ThreadPoolManager.getSingleton();
tpm.destroy();
```

输出的日志类似如下：

<pre>
2017-06-29 14:20:57,170 INFO  shutdown the thread pool 'default'
2017-06-29 14:20:57,171 INFO  shutdown the thread pool 'other'
2017-06-29 14:20:57,171 INFO  stop job 'threadpool4j-threadpoolstate' success
2017-06-29 14:20:57,171 INFO  stop job 'threadpool4j-threadstate' success
2017-06-29 14:20:57,172 INFO  stop job 'threadpool4j-threadstack' success
</pre>
