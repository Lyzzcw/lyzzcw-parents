package lyzzcw.work.component.minio.oss.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lyzzcw.work.component.minio.oss.template.MinioTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioConfig.class)
@ComponentScan(basePackages = "lyzzcw.work.component.minio")
@RequiredArgsConstructor
public class MinioConfiguration  {

    final MinioConfig config;

    @Bean
    public MinioClient createMinioClient() {
        return MinioClient.builder()
                .endpoint(this.config.getEndpoint())
                .credentials(this.config.getAccessKey(), this.config.getSecretKey())
                .build();
    }

    @Bean
    public MinioTemplate createMinioTemplate(MinioClient minioClient) {
        return new MinioTemplate(minioClient,config);
    }

}
