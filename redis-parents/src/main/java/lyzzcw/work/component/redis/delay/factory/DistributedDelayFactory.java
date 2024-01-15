package lyzzcw.work.component.redis.delay.factory;


import lyzzcw.work.component.redis.delay.DistributedDelay;

/**
 * @author lzy
 * @version 1.0.0
 * @description 分布式延时队列工厂类
 */
public interface DistributedDelayFactory {

    /**
     * 根据key获取分布式可过期信号量
     */
    DistributedDelay getDistributedSemaphore(String key);
}
