/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.repository;

import me.ixk.hoshi.db.repository.UpdatingRepository;
import me.ixk.hoshi.note.entity.Note;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.Set;

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
     * 获取分享的笔记
     *
     * @param id 笔记 ID
     * @return 笔记
     */
    Optional<Note> findByIdAndShareIsTrue(String id);
}
