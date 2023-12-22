package lyzzcw.work.component.redis.cache.local.guava.impl;

import com.google.common.cache.Cache;
import lyzzcw.work.component.redis.cache.local.LocalCacheService;
import lyzzcw.work.component.redis.cache.local.guava.factoty.LocalGuavaCacheFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;


/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 基于Guava实现的本地缓存
 */
@Component
@ConditionalOnProperty(name = "cache.type.local", havingValue = "guava")
public class GuavaLocalCacheService<K, V> implements LocalCacheService<K, V> {
    //本地缓存，基于Guava实现
    private final Cache<K, V> cache = LocalGuavaCacheFactory.getLocalCache();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public V getIfPresent(Object key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void remove(K key) {
        cache.invalidate(key);
    }

    @Override
    public ConcurrentMap<K, V> asMap() {
        return cache.asMap();
    }
}
