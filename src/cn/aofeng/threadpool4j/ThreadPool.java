package cn.aofeng.threadpool4j;

import java.lang.management.ThreadInfo;
import java.util.concurrent.Future;

/**
 * 线程池。
 * 
 *  @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public interface ThreadPool {

    /**
     * 提交一个异步任务给默认的线程池执行。
     * 
     * @param task 实现了{@link Runnable}接口的异步任务
     * @return 异步任务执行的结果
     * @throws IllegalArgumentException 如果指定的<code>task</code>为null
     * @see #submit(Runnable, String)
     */
    public Future<?> submit(Runnable task);
    
    /**
     * 提交一个异步任务给指定的线程池执行。
     * 
     * @param task 实现了{@link Runnable}接口的异步任务
     * @param threadpoolName 线程池名称
     * @return 异步任务执行的结果
     * @throws IllegalArgumentException 如果指定的<code>task</code>为null，或者指定的线程池名称不存在
     */
    public Future<?> submit(Runnable task, String threadpoolName);

    /**
     * 获取线程池的信息。如：线程池容量，队列容量。
     * 
     * @param threadpoolName 线程池名称
     * @return 线程池的信息({@link ThreadInfo})
     */
    public ThreadPoolInfo getThreadPoolInfo(String threadpoolName);

}
