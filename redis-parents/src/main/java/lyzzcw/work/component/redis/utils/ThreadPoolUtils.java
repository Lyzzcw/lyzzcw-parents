package lyzzcw.work.component.redis.utils;


import java.util.concurrent.*;

/**
 * @author lzy
 * @version 1.0.0
 * @description 线程工具类
 */
public class ThreadPoolUtils {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(16,
            16,
            30,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(4096),
            new ThreadPoolExecutor.CallerRunsPolicy());
    /**
     * execute task in thread pool
     */
    public static void execute(Runnable command){
        executor.execute(command);
    }

    public static <T> Future<T> submit(Callable<T> task){
        return executor.submit(task);
    }

    public static void shutdown(){
        if (executor != null){
            executor.shutdown();
        }
    }
}
