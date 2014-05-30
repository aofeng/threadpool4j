package cn.aofeng.threadpool4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cn.aofeng.common4j.ILifeCycle;
import cn.aofeng.common4j.xml.DomUtil;
import cn.aofeng.common4j.xml.NodeParser;

/**
 * 多线程池。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ThreadPool implements ILifeCycle {

    private static Logger _logger = Logger.getLogger(ThreadPool.class);
    
    Map<String, ExecutorService> _multiThreadPool = new HashMap<String, ExecutorService>();
    
    String _configFile = "/biz/threadpool4j.xml";
    
    private static ThreadPool _instance = new ThreadPool();
    
    public static ThreadPool getInstance() {
        return _instance;
    }
    
    @Override
    public void init() {
        List<ThreadPoolInfo> threadPoolInfoList = getThreadPoolInfo();
        for (ThreadPoolInfo threadPoolInfo : threadPoolInfoList) {
            BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(threadPoolInfo.getQueueSize());
            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(threadPoolInfo.getCoreSize(), threadPoolInfo.getMaxSize(), 
                    threadPoolInfo.getThreadKeepAliveTime(), TimeUnit.SECONDS, workQueue, 
                    new DefaultThreadFactory(threadPoolInfo.getName()));
            _multiThreadPool.put(threadPoolInfo.getName(), threadPool);
        }
        
        _logger.info( String.format("initialization %d thread pool successfully", threadPoolInfoList.size()) );
    }
    
    private List<ThreadPoolInfo> getThreadPoolInfo() {
        List<ThreadPoolInfo> threadPoolInfoList = new ArrayList<ThreadPoolInfo>();
        Document document = DomUtil.createDocument(_configFile);
        if (null == document) {
            return threadPoolInfoList;
        }
        
        Element root = document.getDocumentElement();
        NodeParser rootParser = new NodeParser(root);
        List<Node> nodeList = rootParser.getChildNodes();
        for (Node node : nodeList) {
            ThreadPoolInfo info = new ThreadPoolInfo();
            NodeParser nodeParser = new NodeParser(node);
            info.setName(nodeParser.getAttributeValue("name"));
            info.setCoreSize(Integer.parseInt(nodeParser.getChildNodeValue("corePoolSize")));
            info.setMaxSize(Integer.parseInt(nodeParser.getChildNodeValue("maxPoolSize")));
            info.setThreadKeepAliveTime(Long.parseLong(nodeParser.getChildNodeValue("keepAliveTime")));
            info.setQueueSize(Integer.parseInt(nodeParser.getChildNodeValue("workQueueSize")));
            
            threadPoolInfoList.add(info);
        }
        
        return threadPoolInfoList;
    }
    
    /**
     * 提交一个异步任务给默认的线程池执行。
     * 
     * @param task 实现了{@link Runnable}接口的异步任务
     * @return 异步任务执行的结果
     * @throws NullPointerException 如果指定的<code>task</code>为null
     * @see #submit(Runnable, String)
     */
    public Future<?> submit(Runnable task) {
        return submit(task, "default");
    }
    
    /**
     * 提交一个异步任务给指定的线程池执行。
     * 
     * @param task 实现了{@link Runnable}接口的异步任务
     * @param threadpoolName 线程池名称
     * @return 异步任务执行的结果
     * @throws IllegalArgumentException 如果指定的<code>task</code>为null，或者指定的线程池名称不存在
     */
    public Future<?> submit(Runnable task, String threadpoolName) {
        ExecutorService threadPool = _multiThreadPool.get(threadpoolName);
        if (null == task) {
            throw new IllegalArgumentException("task is null");
        }
        if (null == threadPool) {
            throw new IllegalArgumentException( String.format("thread pool %s not exists", threadpoolName) );
        }
        
        if (_logger.isDebugEnabled()) {
            _logger.debug("submit a task to thread pool "+threadpoolName);
        }
        
        return threadPool.submit(task);
    }
    
    @Override
    public void destroy() {
        for (Entry<String, ExecutorService> entry : _multiThreadPool.entrySet()) {
            _logger.info("shutdown the thread pool "+entry.getKey());
            entry.getValue().shutdown();
        }
    }

}
