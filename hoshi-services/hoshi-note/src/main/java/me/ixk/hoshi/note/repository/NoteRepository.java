/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import me.ixk.hoshi.mysql.repository.UpdatingRepository;
import me.ixk.hoshi.note.entity.Note;
import me.ixk.hoshi.note.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Otstar Lin
 * @date 2021/11/18 15:03
 */
public interface NoteRepository extends UpdatingRepository<Note, String>, JpaSpecificationExecutor<Note> {
    /**
     * 通过工作区 ID 和 ID 查找
     *
     * @param workspaceId 工作区 ID
     * @param id          笔记 ID
     * @return 笔记
     */
    Optional<Note> findByWorkspaceIdAndId(String workspaceId, String id);

    /**
     * 通过工作区 ID 和父 ID 查找
     *
     * @param workspaceId 工作区 ID
     * @param parentId    父 ID
     * @param status      状态
     * @return 笔记
     */
    Set<Note> findByWorkspaceIdAndParentIdAndStatus(String workspaceId, String parentId, Note.Status status);

    /**
     * 通过工作区 ID 和父 ID 查找
     *
     * @param workspaceId 工作区 ID
     * @param parentId    父 ID
     * @return 笔记
     */
    Set<Note> findByWorkspaceIdAndParentId(String workspaceId, String parentId);

    /**
     * 通过工作区 ID 查找
     *
     * @param workspaces 工作区 ID
     * @param status     状态
     * @param pageable   分页
     * @return 笔记
     */
    Page<Note> findByWorkspaceInAndStatus(Collection<Workspace> workspaces, Note.Status status, Pageable pageable);
}
