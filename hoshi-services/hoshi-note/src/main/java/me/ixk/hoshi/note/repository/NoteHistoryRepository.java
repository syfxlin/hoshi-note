/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.repository;

import me.ixk.hoshi.note.entity.NoteHistory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Otstar Lin
 * @date 2021/5/25 20:03
 */
public interface NoteHistoryRepository
    extends PagingAndSortingRepository<NoteHistory, Long>, JpaSpecificationExecutor<NoteHistory> {}
