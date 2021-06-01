/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.repository;

import java.util.Optional;
import me.ixk.hoshi.common.util.Jpa;
import me.ixk.hoshi.note.entity.WorkSpace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * @author Otstar Lin
 * @date 2021/5/25 17:29
 */
public interface WorkSpaceRepository
    extends PagingAndSortingRepository<WorkSpace, String>, JpaSpecificationExecutor<WorkSpace> {
    /**
     * 通过用户 ID 查找工作区
     *
     * @param userId   用户 ID
     * @param pageable 分页
     * @return 工作区
     */
    Page<WorkSpace> findByUserId(@NonNull String userId, Pageable pageable);

    /**
     * 通过 ID 和用户 ID 查找工作区
     *
     * @param userId      用户 ID
     * @param workspaceId ID
     * @return 工作区
     */
    Optional<WorkSpace> findByUserIdAndId(String userId, String workspaceId);

    /**
     * 更新工作区
     *
     * @param workspace 工作区
     * @return 工作区
     */
    default WorkSpace update(final WorkSpace workspace) {
        final String id = workspace.getId();
        Assert.notNull(id, "更新时 ID 必须设置");
        final Optional<WorkSpace> optional = this.findById(id);
        if (optional.isEmpty()) {
            return this.save(workspace);
        }
        final WorkSpace original = optional.get();
        return this.save(Jpa.updateNull(workspace, original));
    }
}
