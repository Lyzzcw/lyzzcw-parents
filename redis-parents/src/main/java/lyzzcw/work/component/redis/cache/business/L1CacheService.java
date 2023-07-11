package lyzzcw.work.component.redis.cache.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.redis.cache.data.CacheData;
import lyzzcw.work.component.redis.cache.distribute.DistributeCacheService;
import lyzzcw.work.component.redis.cache.distribute.redis.RedisDistributeCacheService;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/10 15:17
 * Description:
 *  一级缓存查询更新逻辑
 *  适用于更新频率高的数据
 *  redis ->  mysql
 */
@Component
@ConditionalOnBean({RedisDistributeCacheService.class})
@RequiredArgsConstructor
@Slf4j
public class L1CacheService {

    final DistributeCacheService distributeCacheService;
    final RedissonClient redissonClient;

}
