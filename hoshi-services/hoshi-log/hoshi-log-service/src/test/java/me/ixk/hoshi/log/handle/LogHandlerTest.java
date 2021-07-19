package me.ixk.hoshi.log.handle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Otstar Lin
 * @date 2021/7/19 19:48
 */
@SpringBootTest
class LogHandlerTest {

    @Autowired
    TestComponent component;

    @Test
    void add() {
        component.addLog();
    }
}
