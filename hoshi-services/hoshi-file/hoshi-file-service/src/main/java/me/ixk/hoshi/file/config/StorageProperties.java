/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.config;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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

    @Bean
    public StorageMimeTypes storageMimeTypes() {
        return new StorageMimeTypes(Arrays.stream(ALLOW_MIME_TYPES).collect(Collectors.toSet()));
    }

    private static final String[] ALLOW_MIME_TYPES = new String[] {
        "image/jpeg",
        "image/gif",
        "image/png",
        "image/bmp",
        "image/tiff",
        "image/x-icon",
        "image/svg+xml",
        "video/x-ms-asf",
        "video/x-ms-wmv",
        "video/x-ms-wmx",
        "video/x-ms-wm",
        "video/avi",
        "video/divx",
        "video/x-flv",
        "video/quicktime",
        "video/mpeg",
        "video/mp4",
        "video/ogg",
        "video/webm",
        "video/x-matroska",
        "video/3gpp",
        "video/3gpp2",
        "text/plain",
        "text/csv",
        "text/tab-separated-values",
        "text/calendar",
        "text/richtext",
        "text/css",
        "text/html",
        "text/vtt",
        "application/ttaf+xml",
        "audio/mpeg",
        "audio/x-realaudio",
        "audio/wav",
        "audio/ogg",
        "audio/midi",
        "audio/x-ms-wma",
        "audio/x-ms-wax",
        "audio/x-matroska",
        "application/rtf",
        "application/javascript",
        "application/pdf",
        "application/x-shockwave-flash",
        "application/java",
        "application/x-tar",
        "application/zip",
        "application/x-gzip",
        "application/rar",
        "application/x-7z-compressed",
        "application/octet-stream",
        "application/postscript",
        "application/x-indesign",
        "application/msword",
        "application/vnd.ms-powerpoint",
        "application/vnd.ms-write",
        "application/vnd.ms-excel",
        "application/vnd.ms-access",
        "application/vnd.ms-project",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.ms-word.document.macroEnabled.12",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.template",
        "application/vnd.ms-word.template.macroEnabled.12",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "application/vnd.ms-excel.sheet.macroEnabled.12",
        "application/vnd.ms-excel.sheet.binary.macroEnabled.12",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.template",
        "application/vnd.ms-excel.template.macroEnabled.12",
        "application/vnd.ms-excel.addin.macroEnabled.12",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
        "application/vnd.ms-powerpoint.presentation.macroEnabled.12",
        "application/vnd.openxmlformats-officedocument.presentationml.slideshow",
        "application/vnd.ms-powerpoint.slideshow.macroEnabled.12",
        "application/vnd.openxmlformats-officedocument.presentationml.template",
        "application/vnd.ms-powerpoint.template.macroEnabled.12",
        "application/vnd.ms-powerpoint.addin.macroEnabled.12",
        "application/vnd.openxmlformats-officedocument.presentationml.slide",
        "application/vnd.ms-powerpoint.slide.macroEnabled.12",
        "application/onenote",
        "application/oxps",
        "application/vnd.ms-xpsdocument",
        "application/vnd.oasis.opendocument.text",
        "application/vnd.oasis.opendocument.presentation",
        "application/vnd.oasis.opendocument.spreadsheet",
        "application/vnd.oasis.opendocument.graphics",
        "application/vnd.oasis.opendocument.chart",
        "application/vnd.oasis.opendocument.database",
        "application/vnd.oasis.opendocument.formula",
        "application/wordperfect",
        "application/vnd.apple.keynote",
        "application/vnd.apple.numbers",
        "application/vnd.apple.pages",
    };
}
