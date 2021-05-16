package me.ixk.hoshi.ums.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Map;
import me.ixk.hoshi.common.util.Json;
import me.ixk.hoshi.security.service.UsersService;
import me.ixk.hoshi.ums.entity.RegisterUserView;
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

    ResultActions registerUtil(final RegisterUserView vo) throws Exception {
        final MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
            .post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.stringify(vo));
        return mockMvc.perform(builder).andDo(print());
    }

    @Test
    void register() throws Exception {
        final RegisterUserView v1 = new RegisterUserView("test", "password", "nickname", "email@ixk.me", "avatar");
        registerUtil(v1).andExpect(MockMvcResultMatchers.status().isOk());
        final RegisterUserView v2 = new RegisterUserView("te", "password", "nickname", "email@ixk.me", "avatar");
        registerUtil(v2).andExpect(MockMvcResultMatchers.status().is4xxClientError());
        final RegisterUserView v3 = new RegisterUserView("test", null, "nickname", "email@ixk.me", "avatar");
        registerUtil(v3).andExpect(MockMvcResultMatchers.status().is4xxClientError());
        final RegisterUserView v4 = new RegisterUserView("test", "password", "nickname", "email@ixk.me", "avatar");
        registerUtil(v4).andExpect(MockMvcResultMatchers.status().is4xxClientError());
        usersService.removeByMap(Map.of("username", "test"));
    }
}
