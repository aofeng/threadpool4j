package cn.aofeng.threadpool4j.job;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cn.aofeng.common4j.lang.SystemUtil;

/**
 * 收集所有线程的堆栈信息并输出到文件。
 * 
 * @author <a href="mailto:nieyong.ny@alibaba-inc.com">聂勇</a>
 */
public class ThreadStackJob extends AbstractJob {

    private static Logger _logger = Logger.getLogger(ThreadStackJob.class);
    
    private String _lineSeparator = SystemUtil.getEndLine();
    
    public ThreadStackJob(int interval) {
        super._interval = interval;
    }
    
    @Override
    protected void execute() {
        Map<Thread, StackTraceElement[]> stackMap = Thread.getAllStackTraces();
        for (Entry<Thread, StackTraceElement[]> entry : stackMap.entrySet()) {
            // 线程基本信息
            Thread thread = entry.getKey();
            StringBuilder buffer = new StringBuilder(4096)
                .append("name:")
                .append(thread.getName())
                .append(", id:")
                .append(thread.getId())
                .append(", status:")
                .append(thread.getState().toString())
                .append(", priority:")
                .append(thread.getPriority())
                .append(_lineSeparator);
            
            // 线程堆栈
            StackTraceElement[] stackList = entry.getValue();
            for (StackTraceElement ste : stackList) {
                buffer.append(ste.toString())
                    .append(_lineSeparator);
            }
            
            _logger.info(buffer.toString());
        }
        
        super.sleep();
    }

}
