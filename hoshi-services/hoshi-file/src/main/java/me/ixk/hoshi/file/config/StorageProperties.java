package me.ixk.hoshi.file.config;

import cn.hutool.core.io.IoUtil;
import java.io.IOException;
import java.util.Set;
import lombok.Data;
import me.ixk.hoshi.common.util.Json;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * @author Otstar Lin
 * @date 2021/5/23 10:09
 */
@Configuration
@ConfigurationProperties(prefix = "hoshi")
@Data
public class StorageProperties {

    private String uploadDir;

    private Resource allowMimeType;

    @Bean
    @SuppressWarnings("unchecked")
    public StorageMimeTypes storageMimeTypes() throws IOException {
        final Set<String> types = Json.parse(IoUtil.readUtf8(this.getAllowMimeType().getInputStream()), Set.class);
        return new StorageMimeTypes(types);
    }
}
