/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Otstar Lin
 * @date 2021/5/27 17:35
 */
@RestController
public class IndexController {

    @GetMapping("/index")
    @PreAuthorize("isAuthenticated()")
    public String index() {
        return "index";
    }
}
