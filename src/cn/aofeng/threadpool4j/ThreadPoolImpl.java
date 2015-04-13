package cn.aofeng.threadpool4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import cn.aofeng.common4j.ILifeCycle;
import cn.aofeng.common4j.lang.StringUtil;
import cn.aofeng.threadpool4j.job.ThreadPoolStateJob;
import cn.aofeng.threadpool4j.job.ThreadStateJob;

/**
 * 多线程池。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPoolImpl implements ILifeCycle, ThreadPool {

    /** 默认的线程池名称 */
    private static final String DEFAULT_THREAD_POOL = "default";

    private static Logger _logger = Logger.getLogger(ThreadPoolImpl.class);    
    
    ThreadPoolConfig _threadPoolConfig = new ThreadPoolConfig();
    
    Map<String, ExecutorService> _multiThreadPool = new HashMap<String, ExecutorService>();
    private ThreadPoolStateJob _threadPoolStateJob;
    private ThreadStateJob _threadStateJob;
    
    public ThreadPoolImpl() {
        // nothing
    }
    
    @Override
    public void init() {
        initThreadPool();
        startThreadPoolStateJob();
        startThreadStateJob();
    }
    
    /**
     * 初始化所有线程池。
     */
    private void initThreadPool() {
        boolean defaultPoolExists = false;
        _threadPoolConfig.init();
        Collection<ThreadPoolInfo> threadPoolInfoList = _threadPoolConfig.getThreadPoolConfig();
        for (ThreadPoolInfo threadPoolInfo : threadPoolInfoList) {
            if (DEFAULT_THREAD_POOL.equals(threadPoolInfo.getName())) {
                defaultPoolExists = true; 
            }
            
            BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(threadPoolInfo.getQueueSize());
            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(threadPoolInfo.getCoreSize(), threadPoolInfo.getMaxSize(), 
                    threadPoolInfo.getThreadKeepAliveTime(), TimeUnit.SECONDS, workQueue, 
                    new DefaultThreadFactory(threadPoolInfo.getName()));
            _multiThreadPool.put(threadPoolInfo.getName(), threadPool);
            _logger.info( String.format("initialization thread pool %s success", threadPoolInfo.getName()) );
        }
        
        if (! defaultPoolExists) {
            throw new IllegalStateException( String.format("the default thread pool not exists, please check the config file '%s'", _threadPoolConfig._configFile) );
        }
    }
    
    /**
     * 初始化并启动线程池状态统计Job。
     */
    private void startThreadPoolStateJob() {
        if (_threadPoolConfig.getThreadPoolStateSwitch()) {
            _threadPoolStateJob = new ThreadPoolStateJob(
                    _multiThreadPool,
                    _threadPoolConfig.getThreadPoolStateInterval() );
            _threadPoolStateJob.init();
            Thread jobThread = new Thread(_threadPoolStateJob);
            jobThread.setName("threadpool4j-threadpoolstate");
            jobThread.start();
            
            _logger.info("start  job 'threadpool4j-threadpoolstate' success");
        }
    }
    
    /**
     * 初始化并启动线程状态统计Job。
     */
    private void startThreadStateJob() {
        if (_threadPoolConfig.getThreadStateSwitch()) {
            _threadStateJob = new ThreadStateJob(_threadPoolConfig.getThreadStateInterval());
            _threadStateJob.init();
            Thread jobThread = new Thread(_threadStateJob);
            jobThread.setName("threadpool4j-threadstate");
            jobThread.start();
            
            _logger.info("start job 'threadpool4j-threadstate' success");
        }
    }
    
    public Future<?> submit(Runnable task) {
        return submit(task, DEFAULT_THREAD_POOL);
    }
    
    public Future<?> submit(Runnable task, String threadpoolName) {
        if (null == task) {
            throw new IllegalArgumentException("task is null");
        }
        
        ExecutorService threadPool = getExistsThreadPool(threadpoolName);
        if (_logger.isDebugEnabled()) {
            _logger.debug("submit a task to thread pool "+threadpoolName);
        }
        
        return threadPool.submit(task);
    }

    private ExecutorService getThreadPool(String threadpoolName) {
        if (StringUtil.isBlank(threadpoolName)) {
            throw new IllegalArgumentException("thread pool name is empty");
        }
        
        ExecutorService threadPool = _multiThreadPool.get(threadpoolName);
        
        return threadPool;
    }
    
    private ExecutorService getExistsThreadPool(String threadpoolName) {
        ExecutorService threadPool = getThreadPool(threadpoolName);
        if (null == threadPool) {
            throw new IllegalArgumentException( String.format("thread pool %s not exists", threadpoolName) );
        }
        
        return threadPool;
    }
    
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return submit(task, DEFAULT_THREAD_POOL);
    }
    
    @Override
    public <T> Future<T> submit(Callable<T> task, String threadpoolName) {
        if (null == task) {
            throw new IllegalArgumentException("task is null");
        }
        
        ExecutorService threadPool = getExistsThreadPool(threadpoolName);
        if (_logger.isDebugEnabled()) {
            _logger.debug("submit a task to thread pool "+threadpoolName);
        }
        
        return threadPool.submit(task);
    }
    
    @Override
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, 
            long timeout, TimeUnit timeoutUnit) {
        return invokeAll(tasks, timeout, timeoutUnit, DEFAULT_THREAD_POOL);
    }
    
    @Override
    public <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks,
            long timeout, TimeUnit timeoutUnit, String threadpoolName) {
        if (null == tasks || tasks.isEmpty()) {
            throw new IllegalArgumentException("task list is null or empty");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout less than or equals zero");
        }
        
        ExecutorService threadPool = getExistsThreadPool(threadpoolName);
        if (_logger.isDebugEnabled()) {
            _logger.debug("invoke task list in thread pool "+threadpoolName);
        }
        
        try {
            return threadPool.invokeAll(tasks, timeout, timeoutUnit);
        } catch (InterruptedException e) {
            _logger.error("invoke task list occurs error", e);
        }
        
        return null;
    }
    
    @Override
    public boolean isExists(String threadpoolName) {
        ExecutorService threadPool = getThreadPool(threadpoolName);
        
        return (null == threadPool ? false : true);
    }
    
    @Override
    public ThreadPoolInfo getThreadPoolInfo(String threadpoolName) {
        ThreadPoolInfo info = _threadPoolConfig.getThreadPoolConfig(threadpoolName);
        
        return info.clone();
    }
    
    @Override
    public void destroy() {
        for (Entry<String, ExecutorService> entry : _multiThreadPool.entrySet()) {
            _logger.info("shutdown the thread pool "+entry.getKey());
            entry.getValue().shutdown();
        }
        
        if (null != _threadPoolStateJob) {
            _threadPoolStateJob.destroy();
            _logger.info("stop job 'threadpool4j-threadpoolstate' success");
            _threadPoolStateJob = null;
        }
        
        if (null != _threadStateJob) {
            _threadStateJob.destroy();
            _logger.info("stop job 'threadpool4j-threadstate' success");
            _threadStateJob = null;
        }
        
        _threadPoolConfig.destroy();
    }

}
