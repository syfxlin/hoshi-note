/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.repository;

import java.util.Optional;
import me.ixk.hoshi.mysql.repository.UpdateRepository;
import me.ixk.hoshi.ums.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * UserRepository
 *
 * @author Otstar Lin
 * @date 2021/5/19 下午 3:07
 */
public interface UserRepository
    extends PagingAndSortingRepository<User, String>, UpdateRepository<User, String>, JpaSpecificationExecutor<User> {
    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    Optional<User> findByUsername(String username);

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
