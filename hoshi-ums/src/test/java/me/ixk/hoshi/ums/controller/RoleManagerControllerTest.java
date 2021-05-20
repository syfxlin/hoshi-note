package me.ixk.hoshi.ums.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Otstar Lin
 * @date 2021/5/20 21:03
 */
@WebMvcTest(RoleManagerController.class)
class RoleManagerControllerTest {

    @Autowired
    private MockMvc mock;

    @Test
    void test() {}
}
