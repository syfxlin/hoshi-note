/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.mysql.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2021/6/5 14:26
 */
class JpaTest {

    @Test
    void merge() {
        final Entity e1 = Entity.builder().username("123").nickname("456").message("789").build();
        final Entity e2 = Entity.builder().nickname("1").build();
        final Entity e3 = Jpa.merge(e2, e1);
        assertEquals("123", e3.getUsername());
        assertEquals("1", e3.getNickname());
        assertEquals("789", e3.getMessage());
    }

    @Data
    @Builder
    static class Entity {

        private String username;
        private String nickname;
        private String message;
    }
}
