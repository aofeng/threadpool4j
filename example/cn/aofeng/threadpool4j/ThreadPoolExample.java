package cn.aofeng.threadpool4j;

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
        ThreadPoolImpl threadPool = ThreadPoolImpl.getInstance();
        threadPool.init();
        
        for (int i = 0; i < 1000000; i++) {
            Runnable task1 = new Runnable() {
                @Override
                public void run() {
                    System.out.println("执行异步任务1");
                }
            };
            threadPool.submit(task1);
            
            Runnable task2 = new Runnable() {
                @Override
                public void run() {
                    System.out.println("执行异步任务2");
                }
            };
            threadPool.submit(task2, "other");
            
            Thread.sleep(50);
        }
        
        threadPool.destroy();
    }

}
