package me.ixk.hoshi.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.ixk.hoshi.security.entity.Users;

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
    Users queryByName(final String username);
}
