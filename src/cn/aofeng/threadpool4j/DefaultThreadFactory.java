package cn.aofeng.threadpool4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认线程池工厂。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class DefaultThreadFactory implements ThreadFactory {

    private String _namePrefix;
    
    private AtomicInteger _number = new AtomicInteger(1);
    
    private ThreadGroup _threadGroup;
    
    public DefaultThreadFactory() {
        this("aofeng");
    }
    
    public DefaultThreadFactory(String namePrefix) {
        this._namePrefix = namePrefix;
        ThreadGroup root = ThreadUtil.getRootThreadGroup();
        _threadGroup = new ThreadGroup(root, _namePrefix+"-pool");
    }
    
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(_threadGroup, r);
        thread.setName(_namePrefix+"-"+_number.getAndIncrement());
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (Thread.NORM_PRIORITY != thread.getPriority()) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        
        return thread;
    }

}
