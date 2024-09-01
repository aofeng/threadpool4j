package cn.aofeng.threadpool4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cn.aofeng.common4j.ILifeCycle;
import cn.aofeng.common4j.xml.DomUtil;
import cn.aofeng.common4j.xml.NodeParser;

/**
 * 从配置文件（/biz/threadpool4j.xml）读取配置信息并存储在内存中。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPoolConfig implements ILifeCycle {

    public final static String DEFAULT_CONFIG_FILE = "/biz/threadpool4j.xml";
    protected String _configFile =DEFAULT_CONFIG_FILE;
    
    /**
     * key为线程池名称，value为{@link ThreadPoolInfo}实例。
     */
    protected Map<String, ThreadPoolInfo> _multiThreadPoolInfo = new HashMap<String, ThreadPoolInfo>();
    
    /** 线程池状态收集开关 */
    protected boolean _threadPoolStateSwitch = false;
    protected int _threadPoolStateInterval = 60;   // 单位：秒
    
    /** 线程状态收集开关 */
    protected boolean _threadStateSwitch = false;
    protected int _threadStateInterval = 60;   // 单位：秒
    
    /** 线程堆栈收集开关 */
    protected boolean _threadStackSwitch = false;
    protected int _threadStackInterval = 60;   // 单位：秒
    
    @Override
    public void init() {
        initConfig();
    }
    
    private void initConfig() {
        Document document = DomUtil.createDocument(_configFile);
        
        Element root = document.getDocumentElement();
        NodeParser rootParser = new NodeParser(root);
        List<Node> nodeList = rootParser.getChildNodes();
        for (Node node : nodeList) {
            NodeParser nodeParser = new NodeParser(node);
            if ( "pool".equals(node.getNodeName()) ) {
                ThreadPoolInfo info = new ThreadPoolInfo();
                info.setName(nodeParser.getAttributeValue("name"));
                info.setCoreSize(Integer.parseInt(nodeParser.getChildNodeValue("corePoolSize")));
                info.setMaxSize(Integer.parseInt(nodeParser.getChildNodeValue("maxPoolSize")));
                info.setThreadKeepAliveTime(Long.parseLong(nodeParser.getChildNodeValue("keepAliveTime")));
                info.setQueueSize(Integer.parseInt(nodeParser.getChildNodeValue("workQueueSize")));
                
                _multiThreadPoolInfo.put(info.getName(), info);
            } else if ( "threadpoolstate".equals(node.getNodeName()) ) {
                _threadPoolStateSwitch = computeSwitchValue(nodeParser);
                _threadPoolStateInterval = computeIntervalValue(nodeParser);
            } else if ( "threadstate".equals(node.getNodeName()) ) {
                _threadStateSwitch = computeSwitchValue(nodeParser);
                _threadStateInterval = computeIntervalValue(nodeParser);
            } else if ( "threadstack".equals(node.getNodeName()) ) {
                _threadStackSwitch = computeSwitchValue(nodeParser);
                _threadStackInterval = computeIntervalValue(nodeParser);
            }
        } // end of for
    }
    
    private boolean computeSwitchValue(NodeParser nodeParser) {
        return "on".equalsIgnoreCase(
                nodeParser.getAttributeValue("switch"));
    }
    
    private int computeIntervalValue(NodeParser nodeParser) {
        return Integer.parseInt(nodeParser.getAttributeValue("interval"));
    }
    
    /**
     * 指定名称的线程池的配置是否存在。
     * @param poolName 线程池名称
     * 
     * @return 如果指定名称的线程池的配置存在返回true，如果不存在返回false；如果传入的线程池名称为null也返回false。
     */
    public boolean containsPool(String poolName) {
        if (null == poolName || null == _multiThreadPoolInfo || _multiThreadPoolInfo.isEmpty()) {
            return false;
        }
        
        return _multiThreadPoolInfo.containsKey(poolName);
    }
    
    /**
     * 获取指定线程池的配置信息。
     * 
     * @param threadpoolName 线程池名称
     * @return 线程池配置信息（{@link ThreadPoolInfo}）
     */
    public ThreadPoolInfo getThreadPoolConfig(String threadpoolName) {
        return _multiThreadPoolInfo.get(threadpoolName);
    }
    
    /**
     * 获取所有线程池的配置信息。
     * 
     * @return 线程池配置信息（{@link ThreadPoolInfo}）集合
     */
    public Collection<ThreadPoolInfo> getThreadPoolConfig() {
        return _multiThreadPoolInfo.values();
    }
    
    /**
     * @return 输出各个线程池状态信息的开关，true表示开，false表示关
     */
    public boolean getThreadPoolStateSwitch() {
        return _threadPoolStateSwitch;
    }
    
    /**
     * @return 线程池状态信息输出的间隔时间（单位：秒）
     */
    public int getThreadPoolStateInterval() {
        return _threadPoolStateInterval;
    }
    
    /**
     * @return 输出各个线程组中线程状态信息的开关，true表示开，false表示关
     */
    public boolean getThreadStateSwitch() {
        return _threadStateSwitch;
    }
    
    /**
     * @return 线程状态信息输出的间隔时间（单位：秒）
     */
    public int getThreadStateInterval() {
        return _threadStateInterval;
    }
    
    /**
     * @return 输出所有线程堆栈的开关，true表示开，false表示关
     */
    public boolean getThreadStackSwitch() {
        return _threadStackSwitch;
    }
    
    /**
     * @return 线程堆栈信息输出的间隔时间（单位：秒）
     */
    public int getThreadStackInterval() {
        return _threadStackInterval;
    }
    
    @Override
    public void destroy() {
        _threadPoolStateSwitch = false;
        _threadStateSwitch = false;
        _multiThreadPoolInfo.clear();
    }

}
