package cn.aofeng.threadpool4j;

import java.lang.management.ThreadInfo;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
     * @throws IllegalArgumentException 指定的任务（<code>task</code>）为null
     * @throws RejectedExecutionException 当队列满，异步任务无法提交给线程池执行时抛出此异常
     * @see #submit(Runnable, String)
     */
    public Future<?> submit(Runnable task);
    
    /**
     * 提交一个不需要返回值的异步任务给指定的线程池执行。
     * 
     * @param task 实现了{@link Runnable}接口的异步任务
     * @param threadpoolName 线程池名称
     * @return 异步任务执行的结果
     * @throws IllegalArgumentException 出现以下情况时抛出：
     * <ul>
     *     <li>指定的任务（<code>task</code>）为null；</li>
     *     <li>指定的线程池名称（<code>threadpoolName</code>）为null，""或全是空白字符；</li>
     *     <li>指定的线程池不存在。</li>
     * </ul>
     * @throws RejectedExecutionException 当队列满，异步任务无法提交给线程池执行时抛出此异常
     */
    public Future<?> submit(Runnable task, String threadpoolName);
    
    /**
     * 提交一个不需要返回值的异步任务给指定的线程池执行。
     * 
     * @param task 实现了{@link Runnable}接口的异步任务
     * @param threadpoolName 线程池名称
     * @param failHandler 当队列满，异步任务无法提交给线程池执行的"失败处理器"
     * @return 异步任务执行的结果。如果队列满导致任务无法提交，将返回null
     * @throws IllegalArgumentException 出现以下情况时抛出：
     * <ul>
     *     <li>指定的任务（<code>task</code>）为null；</li>
     *     <li>指定的线程池名称（<code>threadpoolName</code>）为null，""或全是空白字符；</li>
     *     <li>指定的线程池不存在。</li>
     * </ul>
     */
    public Future<?> submit(Runnable task, String threadpoolName, 
            FailHandler<Runnable> failHandler);
    
    /**
     * 提交一个需要返回值的异步任务给默认的线程池执行。
     * 
     * @param task 实现了{@link Callable}接口的异步任务
     * @return 异步任务执行的结果
     * @throws IllegalArgumentException 指定的任务（<code>task</code>）为null
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
     * @throws IllegalArgumentException 出现以下情况时抛出：
     * <ul>
     *     <li>指定的任务（<code>task</code>）为null；</li>
     *     <li>指定的线程池名称（<code>threadpoolName</code>）为null，""或全是空白字符；</li>
     *     <li>指定的线程池不存在。</li>
     * </ul>
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
     * @throws IllegalArgumentException 出现以下情况时抛出：
     * <ul>
     *     <li>指定的任务（<code>task</code>）为null；</li>
     *     <li>指定的线程池名称（<code>threadpoolName</code>）为null，""或全是空白字符；</li>
     *     <li>指定的线程池不存在。</li>
     * </ul>
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
     * @throws IllegalArgumentException 出现以下情况时抛出：
     * <ul>
     *     <li>指定的任务列表（<code>tasks</code>）为null或是空列表；</li>
     *     <li>指定的线程池不存在。</li>
     *     <li>指定的超时时间（<code>timeout</code>）小于或等于0</li>
     * </ul>
     * @see #invokeAll(List, String, int, TimeUnit)
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
     * @throws IllegalArgumentException 出现以下情况时抛出：
     * <ul>
     *     <li>指定的任务列表（<code>tasks</code>）为null或是空列表；</li>
     *     <li>指定的线程池名称（<code>threadpoolName</code>）为null，""或全是空白字符；</li>
     *     <li>指定的线程池不存在。</li>
     *     <li>指定的超时时间（<code>timeout</code>）小于或等于0</li>
     * </ul>
     */
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks,  
            long timeout, TimeUnit timeoutUnit, String threadpoolName);

    /**
     * 查询指定名称的线程池是否存在。
     * 
     * @param threadpoolName 线程池名称
     * @return 如果指定的线程池存在，返回true；否则，返回true。
     * @throws IllegalArgumentException 指定的线程池名称（<code>threadpoolName</code>）为null，""或全是空白字符
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
