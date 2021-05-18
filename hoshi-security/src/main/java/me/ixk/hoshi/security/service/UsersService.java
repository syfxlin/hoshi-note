package me.ixk.hoshi.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.ixk.hoshi.security.entity.Users;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author syfxlin
 * @since 2021-05-18
 */
public interface UsersService extends IService<Users> {
    /**
     * 查询指定用户名的用户
     *
     * @param username 用户名
     * @return 用户
     */
    default Users queryByName(final String username) {
        return query().eq(Users.USERNAME, username).one();
    }

    /**
     * 添加用户
     *
     * @param user 用户
     * @return 添加后的用户，包含 id
     */
    @Transactional(rollbackFor = { Exception.class, Error.class })
    default Users insert(final Users user) {
        save(user);
        return queryByName(user.getUsername());
    }

    /**
     * 更新用户
     *
     * @param user 用户
     * @return 用户
     */
    @Transactional(rollbackFor = { Exception.class, Error.class })
    default Users update(final Users user) {
        updateById(user);
        return getById(user.getId());
    }
}
