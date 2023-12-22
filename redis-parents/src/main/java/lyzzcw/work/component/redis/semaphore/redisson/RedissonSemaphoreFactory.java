package lyzzcw.work.component.redis.semaphore.redisson;


import lyzzcw.work.component.redis.semaphore.DistributedSemaphore;
import lyzzcw.work.component.redis.semaphore.factory.DistributedSemaphoreFactory;
import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0.0
 * @description 基于Redisson的分布式可过期信号量工厂
 */
@Component
@ConditionalOnProperty(name = "distribute.type.semaphore", havingValue = "redisson")
public class RedissonSemaphoreFactory implements DistributedSemaphoreFactory {

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public DistributedSemaphore getDistributedSemaphore(String key) {
        RPermitExpirableSemaphore permitExpirableSemaphore = redissonClient.getPermitExpirableSemaphore(key);
        return new DistributedSemaphore() {
            @Override
            public String acquire() throws InterruptedException {
                return permitExpirableSemaphore.acquire();
            }

            @Override
            public String acquire(long leaseTime, TimeUnit unit) throws InterruptedException {
                return permitExpirableSemaphore.acquire(leaseTime, unit);
            }

            @Override
            public String tryAcquire() {
                return permitExpirableSemaphore.tryAcquire();
            }

            @Override
            public String tryAcquire(long waitTime, TimeUnit unit) throws InterruptedException {
                return permitExpirableSemaphore.tryAcquire(waitTime, unit);
            }

            @Override
            public String tryAcquire(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
                return permitExpirableSemaphore.tryAcquire(waitTime, leaseTime, unit);
            }

            @Override
            public boolean tryRelease(String permitId) {
                return permitExpirableSemaphore.tryRelease(permitId);
            }

            @Override
            public void release(String permitId) {
                permitExpirableSemaphore.release(permitId);
            }

            @Override
            public int availablePermits() {
                return permitExpirableSemaphore.availablePermits();
            }

            @Override
            public boolean trySetPermits(int permits) {
                return permitExpirableSemaphore.trySetPermits(permits);
            }

            @Override
            public void addPermits(int permits) {
                permitExpirableSemaphore.addPermits(permits);
            }

            @Override
            public boolean updateLeaseTime(String permitId, long leaseTime, TimeUnit unit) {
                return permitExpirableSemaphore.updateLeaseTime(permitId, leaseTime, unit);
            }
        };
    }
}
