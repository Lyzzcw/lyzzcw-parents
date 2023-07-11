package lyzzcw.work.component.redis.cache.data;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/8 14:16
 * Description: No Description
 */
@Getter
@Setter
public abstract class AbstractCache {

    //缓存数据是否存在(适用于缓存击穿校验)
    protected Boolean exist;

    //缓存版本号
    protected Long version;

    //稍后再试
    protected Boolean retryLater;

}
