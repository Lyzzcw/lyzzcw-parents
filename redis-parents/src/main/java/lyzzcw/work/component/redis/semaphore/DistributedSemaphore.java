package lyzzcw.work.component.redis.semaphore;


import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0.0
 * @description 分布式可过期信号量
 */
public interface DistributedSemaphore {

    String acquire() throws InterruptedException;

    String acquire(long leaseTime, TimeUnit unit) throws InterruptedException;

    String tryAcquire();

    String tryAcquire(long waitTime, TimeUnit unit) throws InterruptedException;

    String tryAcquire(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;

    boolean tryRelease(String permitId);

    void release(String permitId);

    int availablePermits();

    boolean trySetPermits(int permits);

    void addPermits(int permits);

    boolean updateLeaseTime(String permitId, long leaseTime, TimeUnit unit);
}
