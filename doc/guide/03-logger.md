注：如果在threadpool4j.xml中开启了状态信息输出（默认关闭），却没有配置专门的日志输出，将输出到应用的默认日志文件中。

# 配置日志输出

## 配置线程池状态日志输出

```xml
<appender name="THREADPOOLSTATE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>${THREADPOOLSTATE_LOG_FILE}</file>
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss} ~ %m%n</pattern>
    </encoder>
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
        <fileNamePattern>${THREADPOOLSTATE_LOG_FILE}.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
        <maxHistory>14</maxHistory>
        <maxFileSize>200MB</maxFileSize>
    </rollingPolicy>
</appender>
<logger name="cn.aofeng.threadpool4j.job.ThreadPoolStateJob" level="INFO" additivity="false" >
    <appender-ref ref="THREADPOOLSTATE"/>
</logger>
```

注：日志输出路径 ${THREADPOOLSTATE_LOG_FILE} 根据项目实际情况修改。

## 配置线程状态日志输出

```xml
<appender name="THREADSTATE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>${THREADSTATE_LOG_FILE}</file>
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss} ~ %m%n</pattern>
    </encoder>
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
        <fileNamePattern>${THREADSTATE_LOG_FILE}.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
        <maxHistory>14</maxHistory>
        <maxFileSize>200MB</maxFileSize>
    </rollingPolicy>
</appender>
<logger name="cn.aofeng.threadpool4j.job.ThreadStateJob" level="INFO" additivity="false" >
    <appender-ref ref="THREADSTATE"/>
</logger>
```

注：日志输出路径 ${THREADSTATE_LOG_FILE} 根据项目实际情况修改。

## 配置线程堆栈日志输出

```xml
<appender name="THREADSTACK" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>${THREADSTACK_LOG_FILE}</file>
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss} ~ %m%n</pattern>
    </encoder>
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
        <fileNamePattern>${THREADSTACK_LOG_FILE}.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
        <maxHistory>14</maxHistory>
        <maxFileSize>200MB</maxFileSize>
    </rollingPolicy>
</appender>
<logger name="cn.aofeng.threadpool4j.job.ThreadStackJob" level="INFO" additivity="false" >
    <appender-ref ref="THREADSTACK"/>
</logger>
```

注：日志输出路径 ${THREADSTACK_LOG_FILE} 根据项目实际情况修改。

## 完整的日志配置参考

logback-spring.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property name="LOG_PATH" value="${user.home}/logs"/>
    <property name="APPLICATION_LOG_FILE" value="${LOG_PATH}/application.log"/>
    <property name="THREADPOOLSTATE_LOG_FILE" value="${LOG_PATH}/threadpoolstate.log"/>
    <property name="THREADSTATE_LOG_FILE" value="${LOG_PATH}/threadstate.log"/>
    <property name="THREADSTACK_LOG_FILE" value="${LOG_PATH}/threadstack.log"/>

    <appender name="APPLICATION" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${APPLICATION_LOG_FILE}</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p %m%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${APPLICATION_LOG_FILE}.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxHistory>14</maxHistory>
            <maxFileSize>200MB</maxFileSize>
        </rollingPolicy>
    </appender>
    <appender name="THREADPOOLSTATE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${THREADPOOLSTATE_LOG_FILE}</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} ~ %m%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${THREADPOOLSTATE_LOG_FILE}.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxHistory>14</maxHistory>
            <maxFileSize>200MB</maxFileSize>
        </rollingPolicy>
    </appender>
    <appender name="THREADSTATE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${THREADSTATE_LOG_FILE}</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} ~ %m%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${THREADSTATE_LOG_FILE}.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxHistory>14</maxHistory>
            <maxFileSize>200MB</maxFileSize>
        </rollingPolicy>
    </appender>
    <appender name="THREADSTACK" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${THREADSTACK_LOG_FILE}</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} ~ %m%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${THREADSTACK_LOG_FILE}.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxHistory>14</maxHistory>
            <maxFileSize>200MB</maxFileSize>
        </rollingPolicy>
    </appender>
    
    <logger name="cn.aofeng.threadpool4j.job.ThreadPoolStateJob" level="INFO" additivity="false" >
        <appender-ref ref="THREADPOOLSTATE"/>
    </logger>

    <logger name="cn.aofeng.threadpool4j.job.ThreadStateJob" level="INFO" additivity="false" >
        <appender-ref ref="THREADSTATE"/>
    </logger>

    <logger name="cn.aofeng.threadpool4j.job.ThreadStackJob" level="INFO" additivity="false" >
        <appender-ref ref="THREADSTACK"/>
    </logger>

    <root level="INFO">
        <appender-ref ref="APPLICATION"/>
    </root>
</configuration>
```