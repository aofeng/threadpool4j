package cn.aofeng.threadpool4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认线程池工厂。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class DefaultThreadFactory implements ThreadFactory {

    private String _namePrefix = "aofeng";
    
    private AtomicInteger _number = new AtomicInteger(1);
    
    public DefaultThreadFactory() {
        // nothing
    }
    
    public DefaultThreadFactory(String namePrefix) {
        this._namePrefix = namePrefix;
    }
    
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(_namePrefix+"-pool-"+_number.getAndIncrement());
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (Thread.NORM_PRIORITY != thread.getPriority()) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        
        return thread;
    }

}
