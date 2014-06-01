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

    protected String _configFile = "/biz/threadpool4j.xml";
    
    protected Map<String, ThreadPoolInfo> _multiThreadPoolInfo = new HashMap<String, ThreadPoolInfo>();
    protected boolean _threadStateSwitch = false;
    protected int _threadStateInterval = 60;   // 单位：秒
    
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
     * @return 输出各个线程池中线程状态的开关，true表示开，false表示关
     */
    public boolean getThreadStateSwitch() {
        return _threadStateSwitch;
    }
    
    /**
     * @return 线程状态输出的间隔时间（单位：秒）
     */
    public int getThreadStateInterval() {
        return _threadStateInterval;
    }
    
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
            } else if ( "threadstate".equals(node.getNodeName()) ) {
                String temp = nodeParser.getAttributeValue("switch");
                _threadStateSwitch = "on".equalsIgnoreCase(temp);
                _threadStateInterval = Integer.parseInt(nodeParser.getAttributeValue("interval"));
            }
        } // end of for
    }
    
    @Override
    public void destroy() {
        // nothing
    }

}
