package lyzzcw.work.component.redis.cache.constant;

import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/8 22:28
 * Description: 缓存常量设置
 */
public class Constant {

    //缓存空数据的时长，单位秒
    public static final Long CACHE_NULL_TTL = 60L;
    //缓存的空数据
    public static final String EMPTY_VALUE = "";
    //分布式锁key的后缀
    public static final String LOCK_SUFFIX = "_lock";
    //等待锁时间，默认2秒
    public static final Long LOCK_WAIT_TIMEOUT = 2000L;
    //线程休眠的毫秒数
    public static final long THREAD_SLEEP_MILLISECONDS = 50;

    //缓存正常数据的时长、单位秒
    public static final long REDIS_TIME_OUT = 24*60*60*7;
    public static final TimeUnit REDIS_TIME_OUT_UNIT = TimeUnit.SECONDS;

    //本地锁key的后缀
    public static final String LOCAL_LOCK_SUFFIX = "_local_lock";
}
