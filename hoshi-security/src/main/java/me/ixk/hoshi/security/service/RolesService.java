package me.ixk.hoshi.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import me.ixk.hoshi.security.entity.Roles;
import me.ixk.hoshi.security.entity.Users;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author syfxlin
 * @since 2021-05-18
 */
public interface RolesService extends IService<Roles> {
    /**
     * 通过用户获取权限列表
     *
     * @param user 用户
     * @return 权限列表
     */
    default List<Roles> queriesByUser(@NotNull final Users user) {
        Assert.notNull(user, "用户对象不能为空");
        return queriesByUser(user.getId());
    }

    /**
     * 通过用户 ID 获取权限列表
     *
     * @param userId 用户 ID
     * @return 权限列表
     */
    List<Roles> queriesByUser(@NotNull Long userId);

    /**
     * 通过名称获取权限
     *
     * @param name 名称
     * @return 权限
     */
    default Roles queryByName(@NotNull final String name) {
        Assert.notNull(name, "用户名不能为空");
        return query().eq(Roles.NAME, name).one();
    }

    /**
     * 添加权限
     *
     * @param role 权限
     * @return 权限，包含 id
     */
    default Roles insert(@NotNull final Roles role) {
        Assert.notNull(role, "插入时规则对象不能为空");
        Assert.isNull(role.getId(), "插入时权限 ID 必须为空（自增生成）");
        save(role);
        return queryByName(role.getName());
    }

    /**
     * 更新权限
     *
     * @param role 权限
     * @return 权限
     */
    default Roles update(@NotNull final Roles role) {
        Assert.notNull(role, "更新时权限对象不能为空");
        Assert.notNull(role.getId(), "更新时权限 ID 不能为空");
        updateById(role);
        return getById(role.getId());
    }
}
