package me.ixk.hoshi.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.ixk.hoshi.security.entity.Roles;
import me.ixk.hoshi.security.entity.UserRoleRelation;
import me.ixk.hoshi.security.entity.Users;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

/**
 * <p>
 * 用户权限关系表 服务类
 * </p>
 *
 * @author syfxlin
 * @since 2021-05-18
 */
public interface UserRoleRelationService extends IService<UserRoleRelation> {
    /**
     * 保存关系
     *
     * @param user 用户
     * @param role 权限
     * @return 是否成功
     */
    default boolean save(@NotNull final Users user, @NotNull final Roles role) {
        Assert.notNull(user, "用户对象不能为空");
        Assert.notNull(role, "规则对象不能为空");
        return save(user.getId(), role.getName());
    }

    /**
     * 保存关系
     *
     * @param userId   用户 ID
     * @param roleName 权限名
     * @return 是否成功
     */
    boolean save(@NotNull Long userId, @NotNull String roleName);
}
