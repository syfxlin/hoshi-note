package me.ixk.hoshi.security.security;

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
    BOOT_ADMIN,
}
