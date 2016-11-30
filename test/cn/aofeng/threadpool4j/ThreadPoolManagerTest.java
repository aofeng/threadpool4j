package cn.aofeng.threadpool4j;

import static org.easymock.EasyMock.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.aofeng.common4j.ILifeCycle;

/**
 * {@link ThreadPoolManager}的单元测试用例。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPoolManagerTest {

    private ILifeCycle _mock = createMock(ThreadPoolImpl.class);
    private ThreadPoolManager _tpm = ThreadPoolManager.getSingleton();
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInit() {
        _tpm.setThreadPool((ThreadPool) _mock);
        _mock.init();
        expectLastCall().times(1);
        replay(_mock);
        
        // 连续调用两次初始化，但内部的线程池应该只执行一次初始化
        _tpm.init();
        _tpm.init();
        verify(_mock);
    }

    @Test
    public void testDestroy() {
        _tpm.setThreadPool((ThreadPool) _mock);
        _mock.destroy();
        expectLastCall().times(1);
        replay(_mock);
        
        // 连续调用两次销毁，但内部的线程池应该只执行一次销毁
        _tpm.destroy();
        _tpm.destroy();
        verify(_mock);
    }

}
