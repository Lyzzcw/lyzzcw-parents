package lyzzcw.work.component.minio.oss.util;

import org.springframework.util.DigestUtils;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/8/31 13:49
 * Description: No Description
 */
public class UUID {
    public static String getUUID() {
        java.util.UUID uuid = java.util.UUID.randomUUID();
        return uuid.toString();
    }

    public static String get32UUID() {
        java.util.UUID uuid = java.util.UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static String getMd5UUID(){
        return DigestUtils.md5DigestAsHex(java.util.UUID.randomUUID().toString().getBytes());
    }
}
