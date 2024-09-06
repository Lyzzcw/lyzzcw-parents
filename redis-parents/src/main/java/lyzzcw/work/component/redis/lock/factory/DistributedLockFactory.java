package lyzzcw.work.component.redis.lock.factory;


import lyzzcw.work.component.redis.lock.DistributedLock;

/**
 * @author lzy
 * @version 1.0.0
 * @description 分布式锁工程接口
 */
public interface DistributedLockFactory {

    /**
     * 根据key获取分布式锁
     */
    DistributedLock getDistributedLock(String key);

    /**
     * 获取多个分布式锁
     *
     * @param keys 钥匙
     * @return {@link DistributedLock}
     */
    DistributedLock getMultDistributedLock(String ... keys);
}
