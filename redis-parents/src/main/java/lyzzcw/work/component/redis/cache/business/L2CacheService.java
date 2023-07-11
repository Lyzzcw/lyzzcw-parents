package lyzzcw.work.component.redis.cache.business;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.redis.cache.constant.Constant;
import lyzzcw.work.component.redis.cache.data.CacheData;
import lyzzcw.work.component.redis.cache.distribute.DistributeCacheService;
import lyzzcw.work.component.redis.cache.distribute.redis.RedisDistributeCacheService;
import lyzzcw.work.component.redis.cache.local.guava.impl.GuavaLocalCacheService;
import lyzzcw.work.component.redis.lock.DistributedLock;
import lyzzcw.work.component.redis.utils.SystemClock;
import lyzzcw.work.component.redis.utils.ThreadPoolUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/8 15:56
 * Description:\
 * 二级缓存查询更新逻辑
 * 设置本地缓存获取和更新机制
 * 适用于更新频率不高的数据
 * guava -> redis ->  mysql
 */
@Component
@ConditionalOnBean({RedisDistributeCacheService.class, GuavaLocalCacheService.class})
@RequiredArgsConstructor
@Slf4j
public class L2CacheService {

    final DistributeCacheService distributeCacheService;
    final GuavaLocalCacheService<String,CacheData> guavaLocalCacheService;
    final RedissonClient redissonClient;


    /**
     * 混合型缓存 getObject 逻辑
     * @param id
     * @param dbFallback
     * @param keyPrefix
     * @param version
     * @param <R>
     * @param <ID>
     * @return
     */
    public <R, ID> CacheData getCacheData(ID id, Function<ID, R> dbFallback, String keyPrefix, Long version) {
        //判断本地缓存是否命中
        String key = distributeCacheService.getKey(keyPrefix, id);

        CacheData localCacheData = guavaLocalCacheService.getIfPresent(key);

        if(localCacheData != null){
            //本地缓存命中，校验版本号
            //如果当前请求版本号 <= 本地缓存版本号，即缓存命中
            if(version == null || version.compareTo(localCacheData.getVersion()) <= 0)  {
                if(log.isDebugEnabled()){
                    log.debug("local cache hits,current version : {},cache version : {}",version,localCacheData.getVersion());
                }
                return localCacheData;
            }
            if(version.compareTo(localCacheData.getVersion()) > 0){
                //当前缓存已失效
                CacheData rebuildCacheData = this.rebuildCacheData(
                        id,dbFallback,Constant.REDIS_TIME_OUT,Constant.REDIS_TIME_OUT_UNIT,key);
                if(log.isDebugEnabled()){
                    log.debug("rebuild cache success: {}",rebuildCacheData);
                }
                return rebuildCacheData;
            }
        }
        //本地缓存未命中，通过redis查找
        CacheData buildCacheData = this.rebuildCacheData(
                id,dbFallback,Constant.REDIS_TIME_OUT,Constant.REDIS_TIME_OUT_UNIT,key);
        if(log.isDebugEnabled()){
            log.debug("build cache success: {}",buildCacheData);
        }
        return buildCacheData;
    }

    /**
     * 混合型缓存 getObjectList 逻辑
     * @param id
     * @param dbFallback
     * @param keyPrefix
     * @param version
     * @param <R>
     * @param <ID>
     * @return
     */
    public <R, ID> CacheData getCacheDataList(ID id, Function<ID, List<R>> dbFallback, String keyPrefix, Long version) {
        //判断本地缓存是否命中
        String key = distributeCacheService.getKey(keyPrefix, id);

        CacheData localCacheData = guavaLocalCacheService.getIfPresent(key);

        if(localCacheData != null){
            //本地缓存命中，校验版本号
            //如果当前请求版本号 <= 本地缓存版本号，即缓存命中
            if(version == null || version.compareTo(localCacheData.getVersion()) <= 0)  {
                if(log.isDebugEnabled()){
                    log.debug("local cache hits,current version : {},cache version : {}",version,localCacheData.getVersion());
                }
                return localCacheData;
            }
            if(version.compareTo(localCacheData.getVersion()) > 0){
                //当前缓存已失效
                CacheData rebuildCacheData = this.rebuildCacheData(
                        id,dbFallback,Constant.REDIS_TIME_OUT,Constant.REDIS_TIME_OUT_UNIT,key);
                if(log.isDebugEnabled()){
                    log.debug("rebuild cache success: {}",rebuildCacheData);
                }
                return rebuildCacheData;
            }
        }
        //本地缓存未命中，通过redis查找
        CacheData buildCacheData = this.rebuildCacheData(
                id,dbFallback,Constant.REDIS_TIME_OUT,Constant.REDIS_TIME_OUT_UNIT,key);
        if(log.isDebugEnabled()){
            log.debug("build cache success: {}",buildCacheData);
        }
        return buildCacheData;
    }

    public <R, ID> CacheData rebuildCacheData(ID id, Function<ID, R> dbFallback, Long timeout, TimeUnit unit, String key) {
        //从Redis获取缓存数据
        String str = distributeCacheService.get(key);
        //判断数据是否存在
        if (StrUtil.isNotBlank(str)){
            //缓存命中
            CacheData redisData = distributeCacheService.getResult(str, CacheData.class);
            this.updateLocalCache(key, redisData);
            return redisData;
        }
        //从数据库中获取
        R newR = dbFallback.apply(id);
        //数据库无数据，设置空缓存，防止缓存击穿
        if(newR == null){
            //设置空的缓存数据 返回
            CacheData emptyCacheData = new CacheData()
                    .empty()
                    .withVersion(SystemClock.millisClock().now())
                    .withExpireTime(Constant.CACHE_NULL_TTL,unit);
            distributeCacheService.set(key,emptyCacheData,Constant.CACHE_NULL_TTL,unit);
            this.updateLocalCache(key, emptyCacheData);
            return emptyCacheData;
        }
        //命中,重建缓存
        CacheData cacheData = new CacheData()
                .with(newR)
                .withVersion(SystemClock.millisClock().now())
                .withExpireTime(timeout,unit);

        this.updateRedisCache(key,cacheData,timeout,unit);
        this.updateLocalCache(key,cacheData);
        return cacheData;
    }

    /**
     * 更新本地缓存
     * @param key
     * @param cacheData
     */
    private void updateLocalCache(String key, CacheData cacheData){
        String lockKey = key.concat(Constant.LOCAL_LOCK_SUFFIX);
        synchronized (lockKey.intern()){
            //对key加锁 不锁全部
            guavaLocalCacheService.put(key, cacheData);
        }
        if(log.isDebugEnabled()){
            log.debug(" ----->  update local cache success");
            log.debug(" ----->  current local cache : {}",guavaLocalCacheService.asMap());
        }
    }

    /**
     * 更新redis缓存
     * @param key
     * @param cacheData
     * @param timeout
     * @param unit
     */
    private void updateRedisCache(String key, CacheData cacheData,Long timeout, TimeUnit unit){
        // 分布式锁
        String lockKey = key.concat(Constant.LOCK_SUFFIX);
        //获取分布式锁
        RLock rLock = redissonClient.getLock(lockKey);
        ThreadPoolUtils.execute(() -> {
            try {
                if (rLock.tryLock(Constant.LOCK_WAIT_TIMEOUT, TimeUnit.MILLISECONDS)) {
                    distributeCacheService.set(key, cacheData, timeout, unit);
                    if(log.isDebugEnabled()){
                        log.debug(" ----->  update redis cache success");
                    }
                }
            } catch (InterruptedException e) {
                log.error("build cache | {}", e.getMessage());
                throw new RuntimeException(e);
            } finally {
                if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }
        });
    }


    /**
     * 刷新本地缓存 + redis缓存
     * @param id
     * @param r
     * @param keyPrefix
     * @param <R>
     * @param <ID>
     * @return
     */
    public <R, ID> CacheData updateCacheData(ID id, R r, String keyPrefix) {

        String key = distributeCacheService.getKey(keyPrefix, id);

        CacheData cacheData = new CacheData()
                .with(r)
                .withVersion(SystemClock.millisClock().now())
                .withExpireTime(Constant.REDIS_TIME_OUT,Constant.REDIS_TIME_OUT_UNIT);

        this.updateRedisCache(key,cacheData,Constant.REDIS_TIME_OUT,Constant.REDIS_TIME_OUT_UNIT);

        this.updateLocalCache(key,cacheData);
        return cacheData;
    }
}
