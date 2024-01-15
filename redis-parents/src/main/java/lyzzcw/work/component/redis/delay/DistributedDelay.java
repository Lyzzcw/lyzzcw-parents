package lyzzcw.work.component.redis.delay;


import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0.0
 * @description 分布式延时队列
 */
public interface DistributedDelay {

    void produce(String queue,String message,long leaseTime, TimeUnit unit);

    String consumer(String queue) throws InterruptedException;
}
