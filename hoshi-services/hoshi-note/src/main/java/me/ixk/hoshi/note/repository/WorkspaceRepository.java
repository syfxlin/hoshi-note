/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.repository;

import me.ixk.hoshi.db.repository.UpdatingRepository;
import me.ixk.hoshi.note.entity.Workspace;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.Set;

/**
 * @author Otstar Lin
 * @date 2021/11/18 15:04
 */
public interface WorkspaceRepository
    extends UpdatingRepository<Workspace, String>, JpaSpecificationExecutor<Workspace> {
    /**
     * 通过用户 ID 查找
     *
     * @param user 用户 ID
     * @return 工作区
     */
    Set<Workspace> findByUser(Long user);

    /**
     * 通过域名查找工作区
     *
     * @param domain 域名
     * @return 工作区
     */
    Optional<Workspace> findByDomain(String domain);

    /**
     * 通过工作区 ID 和用户 ID 查找
     *
     * @param id     工作区 ID
     * @param userId 用户 ID
     * @return 工作区
     */
    Optional<Workspace> findByIdAndUser(String id, Long userId);
}
