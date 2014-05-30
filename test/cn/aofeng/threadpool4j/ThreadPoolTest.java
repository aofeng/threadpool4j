package cn.aofeng.threadpool4j;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link ThreadPool}的单元测试用例。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPoolTest {

    private static ThreadPool _threadPool = ThreadPool.getInstance();
    
    @Before
    public void setUp() throws Exception {
        _threadPool._configFile = "/biz/threadpool4j.xml";
        _threadPool.init();
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testInit() {
        _threadPool._configFile = "/cn/aofeng/threadpool4j/threadpool4j_test.xml";
        _threadPool.init();
        
        assertEquals(2, _threadPool._multiThreadPool.size());
        assertTrue(_threadPool._multiThreadPool.containsKey("default"));
        assertTrue(_threadPool._multiThreadPool.containsKey("other"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSubmitRunnable4TaskIsNull() {
        _threadPool.submit(null);
    }
    
    @Test
    public void testSubmitRunnable() {
        ExecutorService mock = EasyMock.createMock(ExecutorService.class);
        mock.submit(EasyMock.anyObject(Runnable.class));
        EasyMock.expectLastCall().andReturn(null).once(); // 期望被调用submit方法1次
        EasyMock.replay(mock);
        _threadPool._multiThreadPool.put("default", mock);
        
        _threadPool.submit(createTask());
        
        EasyMock.verify(mock);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSubmitRunnableString4ThreadpoolNameIsNull() {
        Runnable task = createTask();
        _threadPool.submit(task, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSubmitRunnableString4ThreadpoolNameNotExists() {
        Runnable task = createTask();
        _threadPool.submit(task, "ThreadpoolNotExists");
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
