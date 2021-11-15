/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.repository;

import me.ixk.hoshi.mysql.repository.UpdatingRepository;
import me.ixk.hoshi.ums.entity.Role;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * RoleRepository
 *
 * @author Otstar Lin
 * @date 2021/5/19 下午 3:07
 */
public interface RoleRepository extends UpdatingRepository<Role, String>, JpaSpecificationExecutor<Role> {
    /**
     * 更新角色
     *
     * @param role 角色
     * @return 角色
     */
    default Role update(final Role role) {
        return this.update(role, Role::getName);
    }
}
