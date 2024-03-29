package lyzzcw.work.component.redis.cache.distribute.redis;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lyzzcw.work.component.redis.cache.constant.Constant;
import lyzzcw.work.component.redis.cache.distribute.DistributeCacheService;
import lyzzcw.work.component.redis.cache.data.CacheData;
import lyzzcw.work.component.redis.lock.DistributedLock;
import lyzzcw.work.component.redis.lock.factory.DistributedLockFactory;
import lyzzcw.work.component.redis.utils.ThreadPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author lzy
 * @version 1.0.0
 * @description 基于Redis实现的分布式缓存，在满足分布式缓存的需求时，解决了缓存击穿、穿透和雪崩的问题
 */
@Component
@ConditionalOnProperty(name = "cache.type.distribute", havingValue = "redis")
public class RedisDistributeCacheService implements DistributeCacheService {

    private final Logger logger = LoggerFactory.getLogger(RedisDistributeCacheService.class);


    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DistributedLockFactory distributedLockFactory;

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, this.getValue(value));
    }

    @Override
    public void set(String key, Object value, Long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, this.getValue(value), timeout, unit);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void setWithLogicalExpire(String key, Object value, Long timeout, TimeUnit unit) {
        CacheData redisData = new CacheData(value, LocalDateTime.now().plusSeconds(unit.toSeconds(timeout)));
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T getObject(String key, Class<T> targetClass) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result == null) {
            return null;
        }
        try {
            return JSONUtil.toBean(result.toString(), targetClass);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<String> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public Boolean delete(String key) {
        if (StrUtil.isEmpty(key)) {
            return false;
        }
        return redisTemplate.delete(key);
    }

    @Override
    public <R, ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long timeout, TimeUnit unit) {
        //获取存储到Redis中的数据key
        String key = this.getKey(keyPrefix, id);
        //从Redis查询缓存数据
        String str = redisTemplate.opsForValue().get(key);
        //缓存存在数据，直接返回
        if (StrUtil.isNotBlank(str)){
            //返回数据
            return this.getResult(str, type);
        }
        //缓存中存储的是空字符串
        if (str != null){
            //直接返回空
            return null;
        }
        //从数据库查询数据
        R r = dbFallback.apply(id);
        //数据数据为空
        if (r == null){
            redisTemplate.opsForValue().set(key, Constant.EMPTY_VALUE, Constant.CACHE_NULL_TTL, TimeUnit.SECONDS);
            return null;
        }
        //缓存数据
        this.set(key, r, timeout, unit);
        return r;
    }

    @Override
    public <R, ID> List<R> queryWithPassThroughList(String keyPrefix, ID id, Class<R> type, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit) {
        //获取存储到Redis中的数据key
        String key = this.getKey(keyPrefix, id);
        //从Redis查询缓存数据
        String str = redisTemplate.opsForValue().get(key);
        //缓存存在数据，直接返回
        if (StrUtil.isNotBlank(str)){
            //返回数据
            return this.getResultList(str, type);
        }
        if (str != null){
            //直接返回数据
            return null;
        }
        List<R> r = dbFallback.apply(id);
        //数据库数据为空
        if (r == null || r.isEmpty()){
            redisTemplate.opsForValue().set(key, Constant.EMPTY_VALUE, Constant.CACHE_NULL_TTL, TimeUnit.SECONDS);
            return null;
        }
        this.set(key, r, timeout, unit);
        return r;
    }

    @Override
    public <R, ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long timeout, TimeUnit unit) {
        //获取存储到Redis中的数据key
        String key = this.getKey(keyPrefix, id);
        //从Redis获取缓存数据
        String str = redisTemplate.opsForValue().get(key);
        //判断数据是否存在
        if (StrUtil.isBlank(str)){
            try{
                // 构建缓存数据
                buildCache(id, dbFallback, timeout, unit, key);
                Thread.sleep(Constant.THREAD_SLEEP_MILLISECONDS);
                //重试
                return queryWithLogicalExpire(keyPrefix, id, type, dbFallback, timeout, unit);
            }catch (InterruptedException e){
                logger.error("query data with logical expire|{}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        //命中，需要先把json反序列化为对象
        CacheData redisData = this.getResult(str, CacheData.class);
        R r = this.getResult(redisData.getData(), type);
        LocalDateTime expireTime = redisData.getExpireTime();
        //判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())){
            // 未过期，直接返回数据
            return r;
        }
        //缓存获取，构建缓存数据
        buildCache(id, dbFallback, timeout, unit, key);
        //返回逻辑过期数据
        return r;
    }

    /**
     * 构建缓存逻辑过期数据
     */
    private <R, ID> void buildCache(ID id, Function<ID, R> dbFallback, Long timeout, TimeUnit unit, String key) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        // 分布式锁
        String lockKey = this.getLockKey(key);
        //获取分布式锁
        DistributedLock distributedLock = distributedLockFactory.getDistributedLock(lockKey);
        //获取成功, Double Check
        ThreadPoolUtils.execute(() -> {
            try{
                boolean isLock = distributedLock.tryLock(Constant.LOCK_WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
                if (isLock){
                    R newR = null;
                    //从Redis获取缓存数据
                    String str = redisTemplate.opsForValue().get(key);
                    if (StrUtil.isEmpty(str)){
                        //查询数据库
                        newR = dbFallback.apply(id);
                    }else{
                        //命中，需要先把json反序列化为对象
                        CacheData redisData = this.getResult(str, CacheData.class);
                        LocalDateTime expireTime = redisData.getExpireTime();
                        if (expireTime.isBefore(LocalDateTime.now())){
                            //查询数据库
                            newR = dbFallback.apply(id);
                        }
                    }
                    if (newR != null){
                        // 重建缓存
                        this.setWithLogicalExpire(key, newR, timeout, unit);
                    }
                }
            }catch (InterruptedException e){
                logger.error("build cache | {}", e.getMessage());
                throw new RuntimeException(e);
            }finally {
               distributedLock.unlock();
            }
        });
    }

    //分布式锁Key
    private String getLockKey(String key){
        return key.concat(Constant.LOCK_SUFFIX);
    }

    @Override
    public <R, ID> List<R> queryWithLogicalExpireList(String keyPrefix, ID id, Class<R> type, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit) {
        //获取存储到Redis中的数据key
        String key = this.getKey(keyPrefix, id);
        //从Redis获取缓存数据
        String str = redisTemplate.opsForValue().get(key);
        //判断数据是否存在
        if (StrUtil.isBlank(str)){
            try{
                // 构建缓存数据
                buildCacheList(id, dbFallback, timeout, unit, key);
                Thread.sleep(Constant.THREAD_SLEEP_MILLISECONDS);
                //重试
                return queryWithLogicalExpireList(keyPrefix, id, type, dbFallback, timeout, unit);
            }catch (InterruptedException e){
                logger.error("query data with logical expire|{}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        //命中，需要先把json反序列化为对象
        CacheData redisData = this.getResult(str, CacheData.class);
        List<R> list = this.getResultList(JSONUtil.toJsonStr(redisData.getData()), type);
        LocalDateTime expireTime = redisData.getExpireTime();
        //判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())){
            // 未过期，直接返回数据
            return list;
        }
        //缓存获取，构建缓存数据
        buildCacheList(id, dbFallback, timeout, unit, key);
        //返回逻辑过期数据
        return list;
    }

    /**
     * 构建缓存逻辑过期数据
     */
    private  <R, ID> void buildCacheList(ID id, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit, String key) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        // 分布式锁
        String lockKey = this.getLockKey(key);
        //获取分布式锁
        DistributedLock distributedLock = distributedLockFactory.getDistributedLock(lockKey);
        ThreadPoolUtils.execute(() -> {
            try{
                boolean isLock = distributedLock.tryLock(Constant.LOCK_WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
                if (isLock){
                    List<R> newR = null;
                    //从Redis获取缓存数据
                    String str = redisTemplate.opsForValue().get(key);
                    if (StrUtil.isEmpty(str)){
                        //查询数据库
                        newR = dbFallback.apply(id);
                    }else{
                        //命中，需要先把json反序列化为对象
                        CacheData redisData = this.getResult(str, CacheData.class);
                        LocalDateTime expireTime = redisData.getExpireTime();
                        //缓存已经逻辑过期
                        if (expireTime.isBefore(LocalDateTime.now())){
                            //查询数据库
                            newR = dbFallback.apply(id);
                        }
                    }
                    if (newR != null){
                        // 重建缓存
                        this.setWithLogicalExpire(key, newR, timeout, unit);
                    }
                }
            }catch (InterruptedException e){
                logger.error("build cache list | {}", e.getMessage());
                throw new RuntimeException(e);
            }finally {
                distributedLock.unlock();
            }
        });
    }

    @Override
    public <R, ID> R queryWithMutex(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long timeout, TimeUnit unit) {
        //获取存储到Redis中的数据key
        String key = this.getKey(keyPrefix, id);
        //从Redis获取缓存数据
        String str = redisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(str)){
            //存在数据，直接返回
            return this.getResult(str, type);
        }
        //缓存了空字符串
        if (str != null){
            return null;
        }
        String lockKey = this.getLockKey(key);
        R r = null;
        //获取分布式锁
        DistributedLock distributedLock = distributedLockFactory.getDistributedLock(lockKey);
        try {
            boolean isLock = distributedLock.tryLock(Constant.LOCK_WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
            //获取信号量失败，重试
            if (!isLock){
                Thread.sleep(Constant.THREAD_SLEEP_MILLISECONDS);
                return queryWithMutex(keyPrefix, id, type, dbFallback, timeout, unit);
            }
            //成功获取到锁
            r = dbFallback.apply(id);
            //数据库本身不存在数据
            if (r == null){
                //缓存空数据
                redisTemplate.opsForValue().set(key, Constant.EMPTY_VALUE, Constant.CACHE_NULL_TTL, TimeUnit.SECONDS);
                return null;
            }
            //数据库存在数据
            this.set(key, r, timeout, unit);
        } catch (InterruptedException e) {
            logger.error("query data with mutex |{}", e.getMessage());
            throw new RuntimeException(e);
        }finally {
            distributedLock.unlock();
        }
        return r;
    }

    @Override
    public <R, ID> List<R> queryWithMutexList(String keyPrefix, ID id, Class<R> type, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit) {
        //获取存储到Redis中的数据key
        String key = this.getKey(keyPrefix, id);
        //从Redis获取缓存数据
        String str = redisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(str)){
            //存在数据，直接返回
            return this.getResultList(str, type);
        }
        //缓存了空字符串
        if (str != null){
            return null;
        }
        String lockKey = this.getLockKey(key);
        List<R> list = null;
        // 获取分布式锁
        DistributedLock distributedLock = distributedLockFactory.getDistributedLock(lockKey);
        try {
            boolean isLock = distributedLock.tryLock(Constant.LOCK_WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
            //获取信号量失败，重试
            if (!isLock){
                Thread.sleep(Constant.THREAD_SLEEP_MILLISECONDS);
                //重试
                return queryWithMutexList(keyPrefix, id, type, dbFallback, timeout, unit);
            }
            //成功获取到锁
            list = dbFallback.apply(id);
            //数据库本身不存在数据
            if (list == null){
                //缓存空数据
                redisTemplate.opsForValue().set(key, Constant.EMPTY_VALUE, Constant.CACHE_NULL_TTL, TimeUnit.SECONDS);
                return null;
            }
            //数据库存在数据
            this.set(key, list, timeout, unit);

        } catch (InterruptedException e) {
            logger.error("query data with mutex list |{}", e.getMessage());
            throw new RuntimeException(e);
        }finally {
            distributedLock.unlock();
        }
        return list;
    }

}
