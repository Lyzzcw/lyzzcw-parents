package lyzzcw.work.component.minio.oss.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lyzzcw.work.component.minio.oss.template.MinioTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioConfiguration.class)
@ComponentScan(basePackages = "lyzzcw.work.component.minio")
@RequiredArgsConstructor
public class MinioConfig {

    final MinioConfiguration configuration;

    @Bean
    public MinioClient createMinioClient() {
        return MinioClient.builder()
                .endpoint(this.configuration.getEndpoint())
                .credentials(this.configuration.getAccessKey(), this.configuration.getSecretKey())
                .build();
    }

    @Bean
    public MinioTemplate createMinioTemplate(MinioClient minioClient) {
        return new MinioTemplate(minioClient,configuration);
    }

}
