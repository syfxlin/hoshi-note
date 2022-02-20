/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.repository;

import me.ixk.hoshi.db.repository.UpdatingRepository;
import me.ixk.hoshi.ums.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * UserRepository
 *
 * @author Otstar Lin
 * @date 2021/5/19 下午 3:07
 */
public interface UserRepository extends UpdatingRepository<User, Long>, JpaSpecificationExecutor<User> {
    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 通过邮箱查找用户
     *
     * @param email 邮箱
     * @return 用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 通过用户名或邮箱查找用户
     *
     * @param username 用户名
     * @param email    邮箱
     * @return 用户
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * 更新用户
     *
     * @param user 用户
     * @return 用户
     */
    default User update(final User user) {
        return this.update(user, User::getId);
    }
}
