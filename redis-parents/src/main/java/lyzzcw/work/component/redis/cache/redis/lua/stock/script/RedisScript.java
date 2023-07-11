package lyzzcw.work.component.redis.cache.redis.lua.stock.script;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/10 18:29
 * Description: 使用时把lua脚本放到项目resource目录下
 */
public class RedisScript {

    /**
     * LUA脚本商品库存不存在
     */
    public static final int LUA_RESULT_GOODS_STOCK_NOT_EXISTS = -1;

    /**
     * LUA脚本要扣减的商品数量小于等于0
     */
    public static final int LUA_RESULT_GOODS_STOCK_PARAMS_LT_ZERO = -2;

    /**
     * LUA脚本库存不足
     */
    public static final int LUA_RESULT_GOODS_STOCK_LT_ZERO = -3;


    public static final DefaultRedisScript<Long> DECREASE_STOCK_SCRIPT;
    public static final DefaultRedisScript<Long> INCREASE_STOCK_SCRIPT;
    public static final DefaultRedisScript<Long> INIT_STOCK_SCRIPT;

    static {
        //扣减库存
        DECREASE_STOCK_SCRIPT = new DefaultRedisScript<>();
        DECREASE_STOCK_SCRIPT.setLocation(new ClassPathResource("lua/decrement_goods_stock.lua"));
        DECREASE_STOCK_SCRIPT.setResultType(Long.class);

        //增加库存
        INCREASE_STOCK_SCRIPT = new DefaultRedisScript<>();
        INCREASE_STOCK_SCRIPT.setLocation(new ClassPathResource("lua/increment_goods_stock.lua"));
        INCREASE_STOCK_SCRIPT.setResultType(Long.class);

        //初始化库存
        INIT_STOCK_SCRIPT = new DefaultRedisScript<>();
        INIT_STOCK_SCRIPT.setLocation(new ClassPathResource("lua/init_goods_stock.lua"));
        INIT_STOCK_SCRIPT.setResultType(Long.class);
    }
}
