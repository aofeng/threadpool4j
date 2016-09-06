在软件架构和设计中，会尽可能地将操作`异步化`，缩短响应时间，提升性能。

将异步任务放入`线程池`，这是许多人都知道的。但是当异步任务多了之后，如果全放在同一个线程池执行，会出现一些问题：
* 不同任务因其执行的操作不同，所需时间不同。如果有大量执行时间较久的异步任务，会阻塞那些执行非常快的异步任务，导致原本很快可以完成的异步任务也变慢。
* 操作本地内容的异步任务和操作远程内容的异步任务。如果放在同一个线程池中，在网络出现故障的情况下，会出现大量的任务积压，导致执行本地内容的异步任务也受影响。

现实生活中，机动车行驶时，会根据行驶的速度划分超车道、快车道、慢车道和应急车道，避免速度慢的车辆阻塞速度快的车辆。

![车道划分](doc/guide/images/多车道.jpg)

同理，将异步任务放入不同的线程池执行，就可以解决上面的两个问题。

`threadpool4j`是一个实现了`多线程池`的类库，解决了上述的问题。可指定不同的异步任务在不同的线程池中运行，不同的线程池可配置不同大小。详细的特性说明如下：

特性
===

1、**模块稳定**。
* 已在实际的业务中应用，使用多线程池模块每天执行超过3亿个异步任务，运行稳定。

2、**支持多个线程池**。
* 每个线程池有独立的名称，可配置不同的线程数。
* 业务可根据异步任务的操作，将它们分发至不同的线程池，避免将所有的异步任务放在一个池中相互影响。

3、**完善的统计和健康状态信息**。
* 每分钟输出一次各个线程池的执行任务数和队列积压情况。
<pre>
[2014-07-08 18:05:54] ~ ThreadPool:default, ActiveThread:0, TotalTask:327563397, CompletedTask:327563397, Queue:0
[2014-07-08 18:05:54] ~ ThreadPool:outer, ActiveThread:0, TotalTask:7033787, CompletedTask:7033787, Queue:0
[2014-07-08 18:05:54] ~ ThreadPool:account, ActiveThread:0, TotalTask:17359, CompletedTask:17359, Queue:0
[2014-07-08 18:05:54] ~ ThreadPool:channel, ActiveThread:0, TotalTask:7037913, CompletedTask:7037913, Queue:0
</pre>

* 每分钟输出一次各个线程池的线程状态信息。
<pre>
[2014-07-08 17:36:58] ~ ThreadGroup:channel-pool, New:0, Runnable:0, Blocked:0, Waiting:30, TimedWaiting:0, Terminated:0
[2014-07-08 17:36:58] ~ ThreadGroup:outer-pool, New:0, Runnable:0, Blocked:0, Waiting:10, TimedWaiting:0, Terminated:0
[2014-07-08 17:36:58] ~ ThreadGroup:account-pool, New:0, Runnable:0, Blocked:0, Waiting:30, TimedWaiting:0, Terminated:0
[2014-07-08 17:36:58] ~ ThreadGroup:default-pool, New:0, Runnable:1, Blocked:0, Waiting:29, TimedWaiting:1, Terminated:0
</pre>

* 输出的日志信息对统计程序和统计脚本友好，容易切分。

4、**使用和配置简单**。
* 从多线程池模块复制biz/threadpool4j.xml配置文件，然后根据项目的实际需要简单修改即可（简单的项目不修改亦可使用）。
* 只需一行代码就可以执行异步任务。

5、**初始化和关闭所有线程池简单**。
* 应用启动时，执行一行代码可初始化配置文件中所有的线程池。
* 应用关闭时，执行一行代码可安全地关闭所有的线程池。

6、**无框架依赖性**。
* 不依赖特定的框架，适用于所有使用Java语言的应用。


文档
===
1、[编译threadpool4j](doc/guide/01-compile.md)。

2、[threadpool4j入门指南](doc/guide/02-guide.md)。

3、[配置日志输出](doc/guide/03-logger.md)。

4、[版本历史](doc/guide/04-history.md)。

