注：如果在threadpool4j.xml中开启了状态信息输出（默认关闭），却没有配置专门的日志文件，将输出到应用的默认日志文件中。

**1、配置线程池状态日志输出**。
```properties
# 线程池状态输出日志
log4j.logger.cn.aofeng.threadpool4j.job.ThreadPoolStateJob=INFO, threadpoolstate
log4j.additivity.cn.aofeng.threadpool4j.job.ThreadPoolStateJob=false
log4j.appender.threadpoolstate=org.apache.log4j.DailyRollingFileAppender
log4j.appender.threadpoolstate.File=/home/nieyong/logs/threadpool4j-threadpoolstate.log
log4j.appender.threadpoolstate.DatePattern='.'yyyy-MM-dd
log4j.appender.threadpoolstate.layout=org.apache.log4j.PatternLayout
log4j.appender.threadpoolstate.layout.ConversionPattern=[%d{yyyy-MM-dd HH:mm:ss}] ~ %m%n
```
注：日志输出路径"/home/nieyong/logs/threadpool4j-threadpoolstate.log"由项目根据实际情况修改。


**2、配置线程状态日志输出**。
```properties
# 所有线程组中线程状态输出日志
log4j.logger.cn.aofeng.threadpool4j.job.ThreadStateJob=INFO, threadstate
log4j.additivity.cn.aofeng.threadpool4j.job.ThreadStateJob=false
log4j.appender.threadstate=org.apache.log4j.DailyRollingFileAppender
log4j.appender.threadstate.File=/home/nieyong/logs/threadpool4j-threadstate.log
log4j.appender.threadstate.DatePattern='.'yyyy-MM-dd
log4j.appender.threadstate.layout=org.apache.log4j.PatternLayout
log4j.appender.threadstate.layout.ConversionPattern=[%d{yyyy-MM-dd HH:mm:ss}] ~ %m%n
```
注：日志输出路径"/home/nieyong/logs/threadpool4j-threadstate.log"由项目根据实际情况修改。


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
