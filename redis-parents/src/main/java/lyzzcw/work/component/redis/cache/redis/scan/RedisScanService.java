package lyzzcw.work.component.redis.cache.redis.scan;

import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/6 13:49
 * Description: No Description
 */
@Component
@Slf4j
public class RedisScanService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Set<String> doScanKey(Long count, String pattern) {
        Set<String> result = stringRedisTemplate.execute((RedisCallback<Set<String>>) con -> {
            Set set = new HashSet();
            ScanOptions.ScanOptionsBuilder scanOptionsBuilder = ScanOptions.scanOptions();
            if (count != null) {
                scanOptionsBuilder = scanOptionsBuilder.count(count);
            }
            if (StringUtils.isNotBlank(pattern)) {
                scanOptionsBuilder = scanOptionsBuilder.match("*" + pattern + "*");
            }
            try {
                Cursor<byte[]> cursor = con.scan(scanOptionsBuilder.build());
                while (cursor.hasNext()) {
                    set.add(new String(cursor.next()));
                }
            } catch (Exception e) {
                log.error("scan error:{}",e);
            }
            return set;
        });
        return result;
    }


    public Set<String> doScanSet(Long count, String pattern) {
        return null;
    }


}
