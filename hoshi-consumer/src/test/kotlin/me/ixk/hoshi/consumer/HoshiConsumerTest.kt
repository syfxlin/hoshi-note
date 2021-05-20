package me.ixk.hoshi.consumer

import me.ixk.hoshi.consumer.controller.TestController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertNotNull

/**
 * @author Otstar Lin
 * @date 2021/5/20 22:48
 */
@SpringBootTest
class HoshiConsumerTest {
    @Autowired
    private lateinit var testController: TestController

    @Test
    fun test() {
        assertNotNull(testController)
    }
}
