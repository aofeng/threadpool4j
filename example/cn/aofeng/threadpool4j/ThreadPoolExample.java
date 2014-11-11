package cn.aofeng.threadpool4j;

import cn.aofeng.common4j.ILifeCycle;

/**
 * 多线程池调用代码示例。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPoolExample {
    
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        ThreadPoolImpl temp = new ThreadPoolImpl();
        ILifeCycle th = temp;
        th.init();
        
        ThreadPool threadPool = temp;
        for (int i = 0; i < 10000; i++) {
            threadPool.submit(new AsynTask());
            threadPool.submit(new AsynTask(), "other");
            
            Thread.sleep(50);
        }
        
        th.destroy();
    }

}
