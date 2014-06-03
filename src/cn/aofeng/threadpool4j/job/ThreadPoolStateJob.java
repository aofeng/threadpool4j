package cn.aofeng.threadpool4j.job;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

/**
 * 收集所有线程池的状态信息，统计并输出汇总信息。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPoolStateJob extends AbstractJob {

    private static Logger _logger = Logger.getLogger(ThreadPoolStateJob.class);
    
    private Map<String, ExecutorService> _multiThreadPool;
    
    private int _interval = 60;
    
    public ThreadPoolStateJob(Map<String, ExecutorService> multiThreadPool, int interval) {
        this._multiThreadPool = multiThreadPool;
        this._interval = interval;
    }
    
    @Override
    protected void execute() {
        Set<Entry<String, ExecutorService>> poolSet = _multiThreadPool.entrySet();
        for (Entry<String, ExecutorService> entry : poolSet) {
            ThreadPoolExecutor pool = (ThreadPoolExecutor) entry.getValue();
            _logger.info( String.format("ThreadPool:%s, ActiveThread:%d, TotalTask:%d, CompletedTask:%d, Queue:%d", 
                    entry.getKey(), pool.getActiveCount(), pool.getTaskCount(), pool.getCompletedTaskCount(), pool.getQueue().size()) );
        }
        
        try {
            Thread.sleep(_interval * 1000);
        } catch (InterruptedException e) {
            // nothing
        }
    }

}
