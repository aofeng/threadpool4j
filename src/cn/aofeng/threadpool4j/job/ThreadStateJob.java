package cn.aofeng.threadpool4j.job;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cn.aofeng.threadpool4j.ThreadStateInfo;
import cn.aofeng.threadpool4j.ThreadUtil;

/**
 * 收集所有线程组中所有线程的状态信息，统计并输出汇总信息。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadStateJob extends AbstractJob {

    private static Logger _logger = Logger.getLogger(ThreadStateJob.class);
    
    private int _interval = 60;
    
    public ThreadStateJob(int interval) {
        this._interval = interval;
    }

    @Override
    protected void execute() {
        Map<String, ThreadStateInfo> statMap = ThreadUtil.statAllGroupThreadState();
        
        for (Entry<String, ThreadStateInfo> entry : statMap.entrySet()) {
            ThreadStateInfo stateInfo = entry.getValue();
            _logger.info( String.format("ThreadGroup:%s, New:%d,  Runnable:%d, Blocked:%d, Waiting:%d, TimedWaiting:%d, Terminated:%d", 
                    entry.getKey(), stateInfo.getNewCount(), stateInfo.getRunnableCount(), stateInfo.getBlockedCount(),
                    stateInfo.getWaitingCount(), stateInfo.getTimedWaitingCount(), stateInfo.getTerminatedCount()) );
        }
        
        try {
            Thread.sleep(_interval * 1000);
        } catch (InterruptedException e) {
            // nothing
        }
    } // end of execute

}
