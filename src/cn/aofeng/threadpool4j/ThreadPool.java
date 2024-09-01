package cn.aofeng.threadpool4j;

import java.lang.management.ThreadInfo;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * 线程池。
 * 
 *  @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public interface ThreadPool {

    /**
     * 提交一个不需要返回值的异步任务给默认的线程池执行。
     * 
     * @param task 实现了{@link Runnable}接口的异步任务
     * @return 异步任务执行的结果
     * @throws IllegalArgumentException 指定的任务（&lt;code&gt;task&lt;/code&gt;）为null
     * @throws RejectedExecutionException 当队列满，异步任务无法提交给线程池执行时抛出此异常
     * @see #submit(Runnable, String)
     */
    public Future<?> submit(Runnable task) throws RejectedExecutionException;
    
    /**
     * 提交一个不需要返回值的异步任务给指定的线程池执行。
     * 
     * @param task 实现了{@link Runnable}接口的异步任务
     * @param threadpoolName 线程池名称
     * @return 异步任务执行的结果
     * @throws IllegalArgumentException 出现以下情况时抛出：
     * &lt;ul&gt;
     *     &lt;li&gt;指定的任务（&lt;code&gt;task&lt;/code&gt;）为null；&lt;/li&gt;
     *     &lt;li&gt;指定的线程池名称（&lt;code&gt;threadpoolName&lt;/code&gt;）为null，""或全是空白字符；&lt;/li&gt;
     *     &lt;li&gt;指定的线程池不存在。&lt;/li&gt;
     * &lt;/ul&gt;
     * @throws RejectedExecutionException 当队列满，异步任务无法提交给线程池执行时抛出此异常
     */
    public Future<?> submit(Runnable task, String threadpoolName) throws RejectedExecutionException;
    
    /**
     * 提交一个不需要返回值的异步任务给指定的线程池执行。
     * 
     * @param task 实现了{@link Runnable}接口的异步任务
     * @param threadpoolName 线程池名称
     * @param failHandler 当队列满，异步任务无法提交给线程池执行的"失败处理器"
     * @return 异步任务执行的结果。如果队列满导致任务无法提交，将返回null
     * @throws IllegalArgumentException 出现以下情况时抛出：
     * &lt;ul&gt;
     *     &lt;li&gt;指定的任务（&lt;code&gt;task&lt;/code&gt;）为null；&lt;/li&gt;
     *     &lt;li&gt;指定的线程池名称（&lt;code&gt;threadpoolName&lt;/code&gt;）为null，""或全是空白字符；&lt;/li&gt;
     *     &lt;li&gt;指定的线程池不存在。&lt;/li&gt;
     * &lt;/ul&gt;
     */
    public Future<?> submit(Runnable task, String threadpoolName, 
            FailHandler<Runnable> failHandler);
    
    /**
     * 提交一个需要返回值的异步任务给默认的线程池执行。
     * 
     * @param task 实现了{@link Callable}接口的异步任务
     * @return 异步任务执行的结果
     * @param <T> 异步任务执行的结果类型
     * @throws IllegalArgumentException 指定的任务（&lt;code&gt;task&lt;/code&gt;）为null
     * @throws RejectedExecutionException 当队列满，异步任务无法提交给线程池执行时抛出此异常
     * @see #submit(Callable, String)
     */
    public <T> Future<T> submit(Callable<T> task);
    
    /**
     * 提交一个需要返回值的异步任务给指定的线程池执行。
     * 
     * @param task 实现了{@link Callable}接口的异步任务
     * @param threadpoolName 线程池名称
     * @return 异步任务执行的结果
     * @param <T> 异步任务执行的结果类型
     * @throws IllegalArgumentException 出现以下情况时抛出：
     * &lt;ul&gt;
     *     &lt;li&gt;指定的任务（&lt;code&gt;task&lt;/code&gt;）为null；&lt;/li&gt;
     *     &lt;li&gt;指定的线程池名称（&lt;code&gt;threadpoolName&lt;/code&gt;）为null，""或全是空白字符；&lt;/li&gt;
     *     &lt;li&gt;指定的线程池不存在。&lt;/li&gt;
     * &lt;/ul&gt;
     * @throws RejectedExecutionException 当队列满，异步任务无法提交给线程池执行时抛出此异常
     */
    public <T> Future<T> submit(Callable<T> task, String threadpoolName);
    
    /**
     * 提交一个需要返回值的异步任务给指定的线程池执行。
     * 
     * @param task 实现了{@link Callable}接口的异步任务
     * @param threadpoolName 线程池名称
     * @param failHandler 当队列满，异步任务无法提交给线程池执行的"失败处理器"
     * @return 异步任务执行的结果。如果队列满导致任务无法提交，将返回null
     * @param <T> 异步任务执行的结果类型
     * @throws IllegalArgumentException 出现以下情况时抛出：
     * &lt;ul&gt;
     *     &lt;li&gt;指定的任务（&lt;code&gt;task&lt;/code&gt;）为null；&lt;/li&gt;
     *     &lt;li&gt;指定的线程池名称（&lt;code&gt;threadpoolName&lt;/code&gt;）为null，""或全是空白字符；&lt;/li&gt;
     *     &lt;li&gt;指定的线程池不存在。&lt;/li&gt;
     * &lt;/ul&gt;
     */
    public <T> Future<T> submit(Callable<T> task, String threadpoolName, 
            FailHandler<Callable<T>> failHandler);
    
    /**
     * 在线程池"default"中执行多个需要返回值的异步任务，并设置超时时间。
     * 
     * @param tasks 实现了{@link Runnable}接口的异步任务列表
     * @param timeout 任务执行超时时间
     * @param timeoutUnit 超时时间的单位
     * @return {@link Future}列表。注：如果在指定的时间内，有任务没有执行完，在执行Future.get操作时将抛出{@link CancellationException}。
     * @param <T> 异步任务执行的结果类型
     * @throws IllegalArgumentException 出现以下情况时抛出：
     * &lt;ul&gt;
     *     &lt;li&gt;指定的任务列表（&lt;code&gt;tasks&lt;/code&gt;）为null或是空列表；&lt;/li&gt;
     *     &lt;li&gt;指定的线程池不存在。&lt;/li&gt;
     *     &lt;li&gt;指定的超时时间（&lt;code&gt;timeout&lt;/code&gt;）小于或等于0&lt;/li&gt;
     * &lt;/ul&gt;
     * @see #invokeAll(Collection, long, TimeUnit, String) 
     */
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, 
            long timeout, TimeUnit timeoutUnit);
    
    /**
     * 在指定的线程池中执行多个需要返回值的异步任务，并设置超时时间。
     * 
     * @param tasks 实现了{@link Runnable}接口的异步任务列表
     * @param timeout 任务执行超时时间
     * @param timeoutUnit 超时时间的单位
     * @param threadpoolName 线程池名称
     * @return {@link Future}列表。注：如果在指定的时间内，有任务没有执行完，在执行Future.get操作时将抛出{@link CancellationException}。
     * @param <T> 异步任务执行的结果类型
     * @throws IllegalArgumentException 出现以下情况时抛出：
     * &lt;ul&gt;
     *     &lt;li&gt;指定的任务列表（&lt;code&gt;tasks&lt;/code&gt;）为null或是空列表；&lt;/li&gt;
     *     &lt;li&gt;指定的线程池名称（&lt;code&gt;threadpoolName&lt;/code&gt;）为null，""或全是空白字符；&lt;/li&gt;
     *     &lt;li&gt;指定的线程池不存在。&lt;/li&gt;
     *     &lt;li&gt;指定的超时时间（&lt;code&gt;timeout&lt;/code&gt;）小于或等于0&lt;/li&gt;
     * &lt;/ul&gt;
     */
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks,  
            long timeout, TimeUnit timeoutUnit, String threadpoolName);

    /**
     * 查询指定名称的线程池是否存在。
     * 
     * @param threadpoolName 线程池名称
     * @return 如果指定的线程池存在，返回true；否则，返回true。
     * @throws IllegalArgumentException 指定的线程池名称（&lt;code&gt;threadpoolName&lt;/code&gt;）为null，""或全是空白字符
     */
    public boolean isExists(String threadpoolName);
    
    /**
     * 获取线程池的信息。如：线程池容量，队列容量。
     * 
     * @param threadpoolName 线程池名称
     * @return 线程池的信息({@link ThreadInfo})
     */
    public ThreadPoolInfo getThreadPoolInfo(String threadpoolName);

}
