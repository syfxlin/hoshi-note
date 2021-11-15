/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.file.properties.MinioProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio 配置
 *
 * @author Otstar Lin
 * @date 2021/11/14 21:09
 */
@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        final MinioClient client = MinioClient
            .builder()
            .endpoint(minioProperties.getUrl())
            .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
            .build();
        client.setTimeout(
            minioProperties.getConnectTimeout().toMillis(),
            minioProperties.getWriteTimeout().toMillis(),
            minioProperties.getReadTimeout().toMillis()
        );
        return client;
    }
}
