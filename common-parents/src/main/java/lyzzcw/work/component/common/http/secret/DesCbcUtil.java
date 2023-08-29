package lyzzcw.work.component.common.http.secret;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/7/27
 * Time: 10:41
 * Description: No Description
 */
@Slf4j
public class DesCbcUtil {

    private static final String DES_SECRET_KEY = "b2c17b46e2b1415392aab5a82869856c";

    private static final String DES_IV = "61960842";

    private static final DES des
            = new DES(Mode.CBC, Padding.PKCS5Padding, DES_SECRET_KEY.getBytes(), DES_IV.getBytes());

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return 加密后的文本，失败返回null
     */
    public static String encode(String plainText) {
        String result = null;
        try {
            result = des.encryptBase64(plainText);
        } catch (Exception e) {
            log.error("DesCbcUtil encode error : {}", e);
        }
        return result;
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return 解密后明文，失败返回null
     */
    public static String decode(String encryptText) {
        String result = null;
        try {
            result = des.decryptStr(encryptText);
        } catch (Exception e) {
            log.error("DesCbcUtil decode error : {}", e.getMessage());
        }
        return result;
    }

}
