package cn.aofeng.threadpool4j;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;

import static org.easymock.EasyMock.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link ThreadPoolImpl}的单元测试用例。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPoolTest {

    private ThreadPoolImpl _threadPool = new ThreadPoolImpl();
    
    @Before
    public void setUp() throws Exception {
        _threadPool.init();
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testInit() {
        assertEquals(2, _threadPool._multiThreadPool.size());
        assertTrue(_threadPool._multiThreadPool.containsKey("default"));
        assertTrue(_threadPool._multiThreadPool.containsKey("other"));
        assertTrue(_threadPool._threadPoolConfig._threadStateSwitch);
        assertEquals(60, _threadPool._threadPoolConfig._threadStateInterval);
    }
    
    /**
     * 测试用例：提交一个异步任务给默认的线程池执行 <br/>
     * 前置条件：
     * <pre>
     * 任务对象为null
     * </pre>
     * 
     * 测试结果：
     * <pre>
     * 抛出{@link IllegalArgumentException}异常
     * </pre>
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSubmitRunnable4TaskIsNull() {
        _threadPool.submit(null);
    }
    
    /**
     * 测试用例：提交一个异步任务给默认的线程池执行 <br/>
     * 前置条件：
     * <pre>
     * 任务对象为{@link Runnable}
     * </pre>
     * 
     * 测试结果：
     * <pre>
     * 线程池default的submit方法被调用1次
     * </pre>
     */
    @Test
    public void testSubmitRunnable() {
        callThreadPool("default");
    }
    
    /**
     * 测试用例：提交一个异步任务给指定的线程池执行 <br/>
     * 前置条件：
     * <pre>
     * 任务对象为null；线程池为default
     * </pre>
     * 
     * 测试结果：
     * <pre>
     * 抛出{@link IllegalArgumentException}异常
     * </pre>
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSubmitRunnableString4TaskIsNull() {
        _threadPool.submit(null, "default");
    }
    
    /**
     * 测试用例：提交一个异步任务给指定的线程池执行 <br/>
     * 前置条件：
     * <pre>
     * 任务对象为{@link Runnable}；线程池名为null
     * </pre>
     * 
     * 测试结果：
     * <pre>
     * 抛出{@link IllegalArgumentException}异常
     * </pre>
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSubmitRunnableString4ThreadpoolNameIsNull() {
        Runnable task = createTask();
        _threadPool.submit(task, null);
    }
    
    /**
     * 测试用例：提交一个异步任务给指定的线程池执行 <br/>
     * 前置条件：
     * <pre>
     * 任务对象为{@link Runnable}；线程池名为"ThreadpoolNotExists"，但实际不存在这个线程池
     * </pre>
     * 
     * 测试结果：
     * <pre>
     * 抛出{@link IllegalArgumentException}异常
     * </pre>
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSubmitRunnableString4ThreadpoolNameNotExists() {
        Runnable task = createTask();
        _threadPool.submit(task, "ThreadpoolNotExists");
    }
    
    /**
     * 测试用例：提交一个异步任务给指定的线程池执行 <br/>
     * 前置条件：
     * <pre>
     * 为{@link Runnable}；线程池名为"other"且实际存在
     * </pre>
     * 
     * 测试结果：
     * <pre>
     * 线程池other的submit方法被调用1次
     * </pre>
     */
    @Test
    public void testSubmitRunnableString() {
        callThreadPool("other");
    }
    
    /**
     * 测试用例：提交一个异步任务给指定的线程池执行 <br/>
     * 前置条件：
     * <pre>
     * 任务对象为{@link Runnable}，提交给线程池名"default"执行
     * </pre>
     * 
     * 测试结果：
     * <pre>
     *  任务对象的run方法被调用1次
     * </pre>
     */
    @Test
    public void testSubmitRunnableString4RunTask() throws InterruptedException {
        Runnable mock = createMock(Runnable.class);
        mock.run();
        expectLastCall().once(); // 期望任务的run方法被调用1次
        replay(mock);
        _threadPool.submit(mock, "default");
        Thread.sleep(1000); // 异步操作，需等待一会儿
        
        verify(mock);
    }

    private void callThreadPool(String threadpoolName) {
        ExecutorService mock = createMock(ExecutorService.class);
        mock.submit(anyObject(Runnable.class));
        expectLastCall().andReturn(null).once();   // 期望线程池的submit方法被调用1次
        replay(mock);
        _threadPool._multiThreadPool.put(threadpoolName, mock);
        _threadPool.submit(createTask(), threadpoolName);
        
        verify(mock);
    }

    private Runnable createTask() {
        return new Runnable() {
            @Override
            public void run() {
                // nothing
            }
        };
    }

}
