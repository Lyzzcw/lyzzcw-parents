package lyzzcw.work.component.redis.cache.local.guava.factoty;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0.0
 * @description 基于Guava的本地缓存工厂类
 */
public class LocalGuavaCacheFactory {

    public static <K, V> Cache<K, V> getLocalCache(){
        return CacheBuilder.newBuilder().initialCapacity(200).concurrencyLevel(5).expireAfterWrite(300, TimeUnit.SECONDS).build();
    }

    public static <K, V> Cache<K, V> getLocalCache(long duration){
        return CacheBuilder.newBuilder().initialCapacity(200).concurrencyLevel(5).expireAfterWrite(duration, TimeUnit.SECONDS).build();
    }

    public static <K, V> Cache<K, V> getLocalCache(int initialCapacity, long duration){
        return CacheBuilder.newBuilder().initialCapacity(initialCapacity).concurrencyLevel(5).expireAfterWrite(duration, TimeUnit.SECONDS).build();
    }
}
