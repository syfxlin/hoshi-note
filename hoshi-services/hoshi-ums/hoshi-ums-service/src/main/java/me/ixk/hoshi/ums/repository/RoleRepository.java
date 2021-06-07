/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.repository;

import me.ixk.hoshi.mysql.repository.UpdateRepository;
import me.ixk.hoshi.ums.entity.Role;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * RoleRepository
 *
 * @author Otstar Lin
 * @date 2021/5/19 下午 3:07
 */
public interface RoleRepository
    extends PagingAndSortingRepository<Role, String>, UpdateRepository<Role, String>, JpaSpecificationExecutor<Role> {
    /**
     * 更新权限
     *
     * @param role 权限
     * @return 权限
     */
    default Role update(final Role role) {
        return this.update(role, Role::getName);
    }
}
