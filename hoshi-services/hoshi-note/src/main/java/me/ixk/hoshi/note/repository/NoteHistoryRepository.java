/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.note.repository;

import me.ixk.hoshi.db.repository.UpdatingRepository;
import me.ixk.hoshi.note.entity.NoteHistory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Otstar Lin
 * @date 2021/11/18 15:04
 */
public interface NoteHistoryRepository
    extends UpdatingRepository<NoteHistory, String>, JpaSpecificationExecutor<NoteHistory> {
}
