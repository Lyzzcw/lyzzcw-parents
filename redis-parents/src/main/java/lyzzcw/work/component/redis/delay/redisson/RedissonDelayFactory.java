package lyzzcw.work.component.redis.delay.redisson;


import lyzzcw.work.component.redis.delay.DistributedDelay;
import lyzzcw.work.component.redis.delay.factory.DistributedDelayFactory;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0.0
 * @description 基于Redisson的分布式延时队列工厂
 */
@Component
@ConditionalOnProperty(name = "distribute.type.delay", havingValue = "redisson")
public class RedissonDelayFactory implements DistributedDelayFactory {

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public DistributedDelay getDistributedSemaphore(String queue) {
        RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue(queue);
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
        return new DistributedDelay() {

            @Override
            public void produce(String queue, String message, long leaseTime, TimeUnit unit) {
                delayedQueue.offer(message, leaseTime, unit);
            }

            @Override
            public String consumer(String queue) throws InterruptedException {
                return blockingQueue.take();
            }
        };
    }
}
