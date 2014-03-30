package cn.aofeng.threadpool4j;

/**
 * 线程池信息。
 * 
 * @author <a href="mailto:nieyong@ucweb.com">聂勇</a>
 */
public class ThreadPoolInfo {

    private String name;
    
    private int coreSize = 5;
    
    private int maxSize = 30;
    
    private long threadKeepAliveTime = 5;
    
    private int queueSize = 10000;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public long getThreadKeepAliveTime() {
        return threadKeepAliveTime;
    }

    public void setThreadKeepAliveTime(long threadKeepAliveTime) {
        this.threadKeepAliveTime = threadKeepAliveTime;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

}
