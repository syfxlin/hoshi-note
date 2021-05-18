package me.ixk.hoshi.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import me.ixk.hoshi.security.entity.Roles;
import me.ixk.hoshi.security.entity.Users;

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
    default List<Roles> queriesByUser(final Users user) {
        return queriesByUser(user.getId());
    }

    /**
     * 通过用户 ID 获取权限列表
     *
     * @param userId 用户 ID
     * @return 权限列表
     */
    List<Roles> queriesByUser(Long userId);
}
