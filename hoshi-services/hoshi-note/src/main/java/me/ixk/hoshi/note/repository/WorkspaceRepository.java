/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.repository;

import me.ixk.hoshi.note.entity.Workspace;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Otstar Lin
 * @date 2021/5/25 17:29
 */
public interface WorkspaceRepository
    extends PagingAndSortingRepository<Workspace, String>, JpaSpecificationExecutor<Workspace> {}
