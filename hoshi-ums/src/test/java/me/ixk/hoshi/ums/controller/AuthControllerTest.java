package me.ixk.hoshi.ums.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Map;
import me.ixk.hoshi.common.util.Json;
import me.ixk.hoshi.security.service.UsersService;
import me.ixk.hoshi.ums.entity.RegisterVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Otstar Lin
 * @date 2021/5/16 下午 3:53
 */
@SpringBootTest
class AuthControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    UsersService usersService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    ResultActions registerUtil(final RegisterVO vo) throws Exception {
        final MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
            .post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.stringify(vo));
        return mockMvc.perform(builder).andDo(print());
    }

    @Test
    void register() throws Exception {
        final RegisterVO v1 = new RegisterVO("test", "password", "nickname", "email@ixk.me", "avatar");
        registerUtil(v1).andExpect(MockMvcResultMatchers.status().isOk());
        final RegisterVO v2 = new RegisterVO("te", "password", "nickname", "email@ixk.me", "avatar");
        registerUtil(v2).andExpect(MockMvcResultMatchers.status().is4xxClientError());
        final RegisterVO v3 = new RegisterVO("test", null, "nickname", "email@ixk.me", "avatar");
        registerUtil(v3).andExpect(MockMvcResultMatchers.status().is4xxClientError());
        final RegisterVO v4 = new RegisterVO("test", "password", "nickname", "email@ixk.me", "avatar");
        registerUtil(v4).andExpect(MockMvcResultMatchers.status().is4xxClientError());
        usersService.removeByMap(Map.of("username", "test"));
    }
}
