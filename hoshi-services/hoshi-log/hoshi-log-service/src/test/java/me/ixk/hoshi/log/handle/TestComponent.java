package me.ixk.hoshi.log.handle;

import lombok.extern.slf4j.Slf4j;
import me.ixk.hoshi.log.annotation.Log;
import org.springframework.stereotype.Component;

/**
 * @author Otstar Lin
 * @date 2021/7/19 19:48
 */
@Component
@Slf4j
public class TestComponent {

    @Log(type = "TEST", operate = "test", message = "测试")
    public void addLog() {
        log.info("Add log");
    }
}
