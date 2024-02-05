package lyzzcw.work.component.minio.oss.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ToString
@ConfigurationProperties(prefix = "spring.minio", ignoreInvalidFields = true)
public class MinioConfig{

    /**
     * host+ port
     */
    private String endpoint;

    /**
     * 访问地址
     */
    private String visitPath;


    /**
     * 账号
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;

    /**
     * 当前服务器的地区
     */
    private String region;

}
