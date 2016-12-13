package cn.aofeng.threadpool4j.handler;

import org.apache.log4j.Logger;

import cn.aofeng.threadpool4j.FailHandler;

/**
 * 当队列满，异步任务无法提交给线程池执行时，输出一条错误日志记录处理失败的任务信息。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class LogErrorFailHandler implements FailHandler<Object> {

    private static Logger _logger = Logger.getLogger(LogErrorFailHandler.class);  
    
    /**
     * 处理无法提交线程池执行的异步任务。
     * 
     * @param task 无法提交线程池执行的异步任务
     * @return null
     */
    @Override
    public void execute(Object task) {
        _logger.error("queue is full, a task cannot be submit to threadpool, task information:"+task);
    }

}
