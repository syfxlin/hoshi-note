package me.ixk.hoshi.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Otstar Lin
 * @date 2021/5/23 10:09
 */
@Configuration
@ConfigurationProperties(prefix = "hoshi")
@Data
public class StorageProperties {

    private String uploadDir;
}
