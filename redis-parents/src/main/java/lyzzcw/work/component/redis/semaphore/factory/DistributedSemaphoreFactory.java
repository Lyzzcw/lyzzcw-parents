package lyzzcw.work.component.redis.semaphore.factory;


import lyzzcw.work.component.redis.semaphore.DistributedSemaphore;

/**
 * @author lzy
 * @version 1.0.0
 * @description 分布式可过期信号量工厂类
 */
public interface DistributedSemaphoreFactory {

    /**
     * 根据key获取分布式可过期信号量
     */
    DistributedSemaphore getDistributedSemaphore(String key);
}
