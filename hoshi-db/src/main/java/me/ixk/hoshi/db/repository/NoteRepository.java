package me.ixk.hoshi.db.repository;

import me.ixk.hoshi.db.entity.Note;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Otstar Lin
 * @date 2021/5/25 20:01
 */
public interface NoteRepository extends PagingAndSortingRepository<Note, String>, JpaSpecificationExecutor<Note> {}
