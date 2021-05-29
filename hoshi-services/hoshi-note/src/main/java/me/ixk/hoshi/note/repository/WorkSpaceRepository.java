/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.repository;

import me.ixk.hoshi.note.entity.WorkSpace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;

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
    Page<WorkSpace> findByUserIdEquals(@NonNull String userId, Pageable pageable);
}
