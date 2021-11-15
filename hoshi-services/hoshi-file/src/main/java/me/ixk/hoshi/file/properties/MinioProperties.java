/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.properties;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Minio 配置
 *
 * @author Otstar Lin
 * @date 2021/11/14 21:02
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinioProperties {

    private String url = "https://play.min.io";
    private String accessKey = "Q3AM3UQ867SPQQA43P2F";
    private String secretKey = "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG";
    private String bucket;

    private Duration connectTimeout = Duration.ofSeconds(10);
    private Duration writeTimeout = Duration.ofSeconds(60);
    private Duration readTimeout = Duration.ofSeconds(10);
}
