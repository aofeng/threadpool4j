package cn.aofeng.threadpool4j;

import java.util.Arrays;
import java.util.Random;

import cn.aofeng.common4j.ILifeCycle;

/**
 * 多线程池调用代码示例。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPoolExample {

    // 耗时的操作（配置低一些的机器小心CPU 100％，反应慢）
    public void needSomeTime() {
        int len = 100000;
        String[] intArray = new String[len];
        Random random = new Random(len);
        
        // 初始化数组
        for (int i = 0; i < len; i++) {
            intArray[i] = String.valueOf(random.nextInt());
        }
        
        // 排序
        Arrays.sort(intArray);
    }
    
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        ILifeCycle th = ThreadPoolImpl.getInstance();
        th.init();
        ThreadPool threadPool = ThreadPoolImpl.getInstance();
        final ThreadPoolExample self = new ThreadPoolExample();
        
        for (int i = 0; i < 1000000; i++) {
            Runnable task1 = new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    self.needSomeTime();
                    long endTime = System.currentTimeMillis();
                    System.out.println("执行异步任务1，耗时："+(endTime-startTime));
                }
            };
            threadPool.submit(task1);
            
            Runnable task2 = new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    self.needSomeTime();
                    long endTime = System.currentTimeMillis();
                    System.out.println("执行异步任务2，耗时："+(endTime-startTime));
                }
            };
            threadPool.submit(task2, "other");
            
            Thread.sleep(50);
        }
        
        th.destroy();
    }

}
