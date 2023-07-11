package lyzzcw.work.component.redis.cache.redis.lua.stock;

import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import lyzzcw.work.component.redis.cache.redis.lua.stock.script.RedisScript;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/10 18:33
 * Description: No Description
 */
@Component
@RequiredArgsConstructor
public class RedisLuaService {

    final RedisTemplate<String, Object> redisTemplate;

    public Long decrementByLua(String key, Integer quantity) {
        return redisTemplate.execute(RedisScript.DECREASE_STOCK_SCRIPT, Collections.singletonList(key), quantity);
    }

    public Long incrementByLua(String key, Integer quantity) {
        return redisTemplate.execute(RedisScript.INCREASE_STOCK_SCRIPT, Collections.singletonList(key), quantity);
    }

    public Long initByLua(String key, Integer quantity) {
        return redisTemplate.execute(RedisScript.INIT_STOCK_SCRIPT, Collections.singletonList(key), quantity);
    }

    public void checkResult(Long result) {

        Assert.isFalse(result == RedisScript.LUA_RESULT_GOODS_STOCK_NOT_EXISTS,
                "LUA脚本商品库存不存在");

        Assert.isFalse(result == RedisScript.LUA_RESULT_GOODS_STOCK_PARAMS_LT_ZERO,
                "LUA脚本要扣减的商品数量小于等于0");

        Assert.isFalse(result == RedisScript.LUA_RESULT_GOODS_STOCK_LT_ZERO,
                "LUA脚本库存不足");
    }

}
