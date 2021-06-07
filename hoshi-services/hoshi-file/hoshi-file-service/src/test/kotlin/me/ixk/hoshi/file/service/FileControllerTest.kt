/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.service

import me.ixk.hoshi.test.MockSecurityConfig
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithUserDetails

/**
 * @author Otstar Lin
 * @date 2021/5/26 22:00
 */
@SpringBootTest(classes = [MockSecurityConfig::class])
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class FileControllerTest {

    @Test
    @WithUserDetails("admin")
    fun test() {
        println()
    }
}
