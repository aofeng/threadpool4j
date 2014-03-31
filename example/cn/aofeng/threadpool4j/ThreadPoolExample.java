package cn.aofeng.threadpool4j;

/**
 * 多线程池调用代码示例。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPoolExample {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ThreadPool.getInstance().init();
        
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("执行异步任务1");
            }
        };
        ThreadPool.getInstance().submit(task1);
        
        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("执行异步任务2");
            }
        };
        ThreadPool.getInstance().submit(task2, "other");
        
        ThreadPool.getInstance().destroy();
    }

}
