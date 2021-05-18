package me.ixk.hoshi.security.security;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Otstar Lin
 * @date 2021/5/14 下午 8:33
 */
public enum Roles {
    /**
     * 用户
     */
    USER,
    /**
     * 管理员
     */
    ADMIN,
    /**
     * Spring Actuator 访问权限
     */
    ACTUATOR,
    /**
     * Spring Boot Admin 访问权限
     */
    BOOT_ADMIN;

    private static final Set<String> ROLES = Arrays
        .stream(values())
        .map(Enum::name)
        .collect(Collectors.toUnmodifiableSet());

    public static boolean contains(final String role) {
        return ROLES.contains(role);
    }
}
