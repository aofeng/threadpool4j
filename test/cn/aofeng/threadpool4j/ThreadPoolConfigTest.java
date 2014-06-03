package cn.aofeng.threadpool4j;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link ThreadPoolConfig}的单元测试用例。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPoolConfigTest {

    private ThreadPoolConfig _threadPoolConfig = new ThreadPoolConfig();
    
    @Before
    public void setUp() throws Exception {
        _threadPoolConfig._configFile = "/biz/threadpool4j.xml";
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * 测试用例：读取线程池配置文件 <br/>
     * 前置条件：
     * <pre>
     * 1.0.0版本的配置文件，没有线程状态输出开关（threadstate节点）
     * </pre>
     * 
     * 测试结果：
     * <pre>
     * 1、有两个线程池的配置信息，分别是default和other。
     * 2、线程状态输出开关的值为false。
     * </pre>
     */
    @Test
    public void testInit4NoThreadStateSwitch() {
        _threadPoolConfig._configFile = "/cn/aofeng/threadpool4j/threadpool4j_1.0.0.xml";
        _threadPoolConfig.init();
        assertEquals(2, _threadPoolConfig._multiThreadPoolInfo.size());
        assertTrue(_threadPoolConfig._multiThreadPoolInfo.containsKey("default"));
        assertTrue(_threadPoolConfig._multiThreadPoolInfo.containsKey("other"));
        assertFalse(_threadPoolConfig._threadStateSwitch);
    }

    /**
     * 测试用例：读取线程池配置文件 <br/>
     * 前置条件：
     * <pre>
     * 1.5.0版本的配置文件，有线程池状态输出开关（threadpoolstate节点）和线程状态输出开关（threadstate节点）
     * </pre>
     * 
     * 测试结果：
     * <pre>
     * 1、有两个线程池的配置信息，分别是default和other。
     * 2、线程池状态输出开关的值为true，输出间隔为120秒。
     * 3、线程状态输出开关的值为true，输出间隔为180秒。
     * </pre>
     */
    @Test
    public void testInit4HasThreadStateSwitch() {
        _threadPoolConfig._configFile = "/cn/aofeng/threadpool4j/threadpool4j_1.5.0.xml";
        _threadPoolConfig.init();
        assertEquals(2, _threadPoolConfig._multiThreadPoolInfo.size());
        assertTrue(_threadPoolConfig._multiThreadPoolInfo.containsKey("default"));
        assertTrue(_threadPoolConfig._multiThreadPoolInfo.containsKey("other"));
        assertTrue(_threadPoolConfig.getThreadPoolStateSwitch());
        assertEquals(120, _threadPoolConfig.getThreadPoolStateInterval());
        assertTrue(_threadPoolConfig.getThreadStateSwitch());
        assertEquals(180, _threadPoolConfig.getThreadStateInterval());
    }

}
