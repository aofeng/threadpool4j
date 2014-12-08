package cn.aofeng.threadpool4j;

import java.util.concurrent.Callable;

/**
 * 需要返回值的异步任务。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class CallableAnsyTask implements Callable<Long> {

    private int[] _arr;
    
    public CallableAnsyTask(int[] arr) {
        _arr = arr;
    }
    
    @Override
    public Long call() throws Exception {
        long result = 0;
        for (int i = 0; i < _arr.length; i++) {
            result += _arr[i];
        }
        
        return result;
    }

}
