package me.ixk.hoshi.security.config;

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
     * Spring Actuator 和 Spring Boot Admin 访问权限
     */
    ACTUATOR_ADMIN,
}
