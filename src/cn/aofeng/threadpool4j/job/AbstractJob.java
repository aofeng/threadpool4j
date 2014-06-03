package cn.aofeng.threadpool4j.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.aofeng.common4j.ILifeCycle;

/**
 * 抽象job类。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public abstract class AbstractJob implements Runnable, ILifeCycle {

    protected String _lineSeparator = System.getProperty("line.separator"); 
    
    protected volatile AtomicBoolean _run = new AtomicBoolean(true);

    @Override
    public void init() {
        _run.set(true);
    }

    @Override
    public void run() {
        while (_run.get()) {
            execute();
        }
    }
    
    protected abstract void execute();
    
    /**
     * @return 返回"yyyy-MM-dd HH:mm:ss"格式的当前日期时间字符串
     */
    protected String currentTime() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        
        return format.format(date);
    }
    
    @Override
    public void destroy() {
        _run.set(false);
    }

}
