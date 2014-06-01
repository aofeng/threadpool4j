package cn.aofeng.threadpool4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import cn.aofeng.common4j.ILifeCycle;

/**
 * 收集所有线程组中所有线程的状态信息，统计并输出汇总信息。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadStateJob implements Runnable, ILifeCycle {

    private static Logger _logger = Logger.getLogger(ThreadStateJob.class);
    
    private String _lineSeparator = System.getProperty("line.separator"); 
    
    private int _interval = 60;
    
    private volatile AtomicBoolean _run = new AtomicBoolean(true);
    
    public ThreadStateJob(int interval) {
        this._interval = interval;
    }
    
    @Override
    public void init() {
        _run.set(true);
    }
    
    @Override
    public void run() {
        while (_run.get()) {
            StringBuilder buffer = new StringBuilder(2048);
            buffer.append(currentTime())
                    .append(" thread state:")
                    .append(_lineSeparator);
            
            Map<String, ThreadStateInfo> statMap = ThreadUtil.statAllGroupThreadState();
            
            for (Entry<String, ThreadStateInfo> entry : statMap.entrySet()) {
                ThreadStateInfo stateInfo = entry.getValue();
                buffer.append( String.format("ThreadGroup:%s, NEW:%d,  RUNNABLE:%d, BLOCKED:%d, WAITING:%d, TIMED_WAITING:%d, TERMINATED:%d", 
                            entry.getKey(), stateInfo.getNewCount(), stateInfo.getRunnableCount(), stateInfo.getBlockedCount(),
                            stateInfo.getWaitingCount(), stateInfo.getTimedWaitingCount(), stateInfo.getTerminatedCount()) )
                        .append(_lineSeparator);
            }
            _logger.info(buffer.toString());
            
            try {
                Thread.sleep(_interval * 1000);
            } catch (InterruptedException e) {
                // nothing
            }
        }
    }
    
    private String currentTime() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        
        return format.format(date);
    }
    
    @Override
    public void destroy() {
        _run.set(false);
    }

}
