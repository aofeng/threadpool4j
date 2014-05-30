package cn.aofeng.threadpool4j;

/**
 * 线程实用操作方法集合。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>u
 */
public class ThreadUtil {

    /**
     * 获取当前线程的Top Level线程组。
     * 
     * @return Top Level线程组。
     */
    public static ThreadGroup getRootThreadGroup() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        while (null != threadGroup.getParent()) {
            threadGroup = threadGroup.getParent();
        }
        
        return threadGroup;
    }

}
