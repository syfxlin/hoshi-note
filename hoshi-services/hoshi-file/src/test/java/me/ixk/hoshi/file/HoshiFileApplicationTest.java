/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file;

import io.minio.messages.Item;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import me.ixk.hoshi.file.exception.MinioException;
import me.ixk.hoshi.file.service.MinioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class HoshiFileApplicationTest {

    @Autowired
    MinioService minioService;

    @Test
    void test() {
        try {
            List<Item> items = minioService.list("user_123").collect(Collectors.toList());
            log.info("Items: {}", items);
        } catch (MinioException e) {
            e.printStackTrace();
        }
    }
}
