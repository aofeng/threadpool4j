package cn.aofeng.threadpool4j.handler;

import cn.aofeng.threadpool4j.FailHandler;

/**
 * 当队列满，异步任务无法提交给线程池执行时，丢弃此任务并不做任何记录。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class DiscardFailHandler implements FailHandler<Object> {

    /**
     * 处理无法提交线程池执行的异步任务。
     * 
     * @param task 无法提交线程池执行的异步任务
     * @return null
     */
    @Override
    public void execute(Object task) {
        // nothing
    }

}
