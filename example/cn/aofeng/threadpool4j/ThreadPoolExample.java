package cn.aofeng.threadpool4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import cn.aofeng.common4j.ILifeCycle;

/**
 * 多线程池调用代码示例。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPoolExample {
    
    public static void main(String[] args) throws Exception {
        ThreadPoolImpl temp = new ThreadPoolImpl();
        ILifeCycle th = temp;
        th.init();
        
        ThreadPool threadPool = temp;
        executeRunnableAnsyTask(threadPool);
        executeCallableAnsyTask(threadPool);
        parallelExecuteAnsyTask(threadPool);
        
        th.destroy();
    }

    /**
     * 执行不需要返回值的异步任务。
     */
    private static void executeRunnableAnsyTask(ThreadPool threadPool)
            throws InterruptedException {
        for (int i = 0; i < 10000; i++) {
            threadPool.submit(new RunnableAsynTask());
            threadPool.submit(new RunnableAsynTask(), "other");
            
            Thread.sleep(50);
        }
    }
    
    /**
     * 执行需要返回值的异步任务。
     */
    private static void executeCallableAnsyTask(ThreadPool threadPool)
            throws InterruptedException, ExecutionException {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        CallableAnsyTask task = new CallableAnsyTask(arr);
        Future<Long> future = threadPool.submit(task);
        System.out.println("异步任务在线程池default的执行结果为:"+future.get());
        threadPool.submit(task, "other");
        System.out.println("异步任务在线程池other的执行结果为:"+future.get());
    }

    /**
     * 并行调用多个异步任务。
     */
    private static void parallelExecuteAnsyTask(ThreadPool threadPool) 
            throws InterruptedException, ExecutionException {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        List<Callable<Long>> tasks = new ArrayList<Callable<Long>>();
        tasks.add(new CallableAnsyTask(arr));
        tasks.add(new CallableAnsyTask(arr));
        tasks.add(new CallableAnsyTask(arr));
        
        List<Future<Long>> futures = threadPool.invokeAll(tasks, 1, TimeUnit.SECONDS);
        for (Future<Long> future : futures) {
            Long result = future.get();   // 如果某个任务执行超时，调用该任务对应的future.get时抛出CancellationException异常
            System.out.println("并行调用，其中一个任务的执行结果为:"+result);
        }
        
    }

}
