package cn.aofeng.threadpool4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import cn.aofeng.common4j.ILifeCycle;
import cn.aofeng.threadpool4j.job.ThreadPoolStateJob;
import cn.aofeng.threadpool4j.job.ThreadStateJob;

/**
 * 多线程池。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPoolImpl implements ILifeCycle, ThreadPool {

    private static Logger _logger = Logger.getLogger(ThreadPoolImpl.class);    
    
    ThreadPoolConfig _threadPoolConfig = new ThreadPoolConfig();
    
    Map<String, ExecutorService> _multiThreadPool = new HashMap<String, ExecutorService>();
    private ThreadPoolStateJob _threadPoolStateJob;
    private ThreadStateJob _threadStateJob;
    
    private static ThreadPoolImpl _instance = new ThreadPoolImpl();
    
    public static ThreadPoolImpl getInstance() {
        return _instance;
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
        _threadPoolConfig.init();
        Collection<ThreadPoolInfo> threadPoolInfoList = _threadPoolConfig.getThreadPoolConfig();
        for (ThreadPoolInfo threadPoolInfo : threadPoolInfoList) {
            BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(threadPoolInfo.getQueueSize());
            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(threadPoolInfo.getCoreSize(), threadPoolInfo.getMaxSize(), 
                    threadPoolInfo.getThreadKeepAliveTime(), TimeUnit.SECONDS, workQueue, 
                    new DefaultThreadFactory(threadPoolInfo.getName()));
            _multiThreadPool.put(threadPoolInfo.getName(), threadPool);
            _logger.info( String.format("initialization thread pool %s successfully", threadPoolInfo.getName()) );
        }
        _logger.info( String.format("initialization %d thread pool successfully", threadPoolInfoList.size()) );
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
            
            _logger.info("start threadpoolstate job successfully");
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
            
            _logger.info("start threadstate job successfully");
        }
    }
    
    public Future<?> submit(Runnable task) {
        return submit(task, "default");
    }
    
    public Future<?> submit(Runnable task, String threadpoolName) {
        ExecutorService threadPool = _multiThreadPool.get(threadpoolName);
        if (null == task) {
            throw new IllegalArgumentException("task is null");
        }
        if (null == threadPool) {
            throw new IllegalArgumentException( String.format("thread pool %s not exists", threadpoolName) );
        }
        
        if (_logger.isDebugEnabled()) {
            _logger.debug("submit a task to thread pool "+threadpoolName);
        }
        
        return threadPool.submit(task);
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
        
        _threadPoolStateJob.destroy();
        _logger.info("stop threadpoolstate job successfully");
        
        _threadStateJob.destroy();
        _logger.info("stop threadstate job successfully");
    }

}
