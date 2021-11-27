/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Otstar Lin
 * @date 2021/11/27 13:33
 */
@Configuration
@ConfigurationProperties(prefix = "pexels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PexelsProperties {

    private String token;
}
