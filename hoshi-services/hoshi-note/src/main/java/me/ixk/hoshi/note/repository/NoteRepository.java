/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.repository;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import me.ixk.hoshi.note.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;

/**
 * @author Otstar Lin
 * @date 2021/5/25 20:01
 */
public interface NoteRepository extends PagingAndSortingRepository<Note, String>, JpaSpecificationExecutor<Note> {
    /**
     * 通过工作区 ID 获取笔记
     *
     * @param userId      用户 ID
     * @param workspaceId 工作区 ID
     * @param pageable    分页
     * @return 笔记
     */
    @Query("SELECT n FROM Note n WHERE n.workspace.userId = ?1 AND n.workspace.id = ?2")
    Page<Note> findByUserIdAndWorkspaceId(@NotNull String userId, @NonNull String workspaceId, Pageable pageable);

    /**
     * 通过笔记 ID 获取笔记
     *
     * @param userId      用户 ID
     * @param workspaceId 工作区 ID
     * @param noteId      笔记 ID
     * @return 笔记
     */
    @Query("select n from Note n where n.workspace.userId = ?1 and n.workspace.id = ?2 and n.id = ?3")
    Optional<Note> findByUserIdAndWorkSpaceIdAndNoteId(
        @NonNull String userId,
        @NonNull String workspaceId,
        @NonNull String noteId
    );
}
