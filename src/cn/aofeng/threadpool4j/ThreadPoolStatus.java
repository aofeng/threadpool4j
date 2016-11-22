package cn.aofeng.threadpool4j;

/**
 * 线程池状态。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPoolStatus {

    /** 未初始化 */
    public final static int UNINITIALIZED = 0;;
    
    /** 初始化成功 */
    public final static int INITIALITION_SUCCESSFUL = 1;
    
    /** 初始化失败 */
    public final static int INITIALITION_FAILED = 2;
    
    /** 已销毁 */
    public final static int DESTROYED = 3;

}
