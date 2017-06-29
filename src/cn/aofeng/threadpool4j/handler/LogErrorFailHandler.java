package cn.aofeng.threadpool4j.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.aofeng.threadpool4j.FailHandler;

/**
 * 当队列满，异步任务无法提交给线程池执行时，输出一条错误日志记录处理失败的任务信息。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class LogErrorFailHandler<T> implements FailHandler<T> {

    private static Logger _logger = LoggerFactory.getLogger(LogErrorFailHandler.class);  
    
    /**
     * 处理无法提交线程池执行的异步任务。
     * 
     * @param task 无法提交线程池执行的异步任务
     * @return null
     */
    @Override
    public void execute(T task) {
        _logger.error("queue is full, a task cannot be submit to threadpool, task information:{}", task);
    }

}
