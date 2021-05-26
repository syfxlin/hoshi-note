package me.ixk.hoshi.file.config;

import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.file.service.FileStorageService;
import me.ixk.hoshi.file.service.StorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Otstar Lin
 * @date 2021/5/23 10:15
 */
@Configuration
@RequiredArgsConstructor
public class StorageConfig {

    private final StorageProperties properties;

    @Bean
    @ConditionalOnMissingBean(StorageService.class)
    public StorageService storageService() {
        return new FileStorageService(Paths.get(this.properties.getUploadDir()));
    }
}
