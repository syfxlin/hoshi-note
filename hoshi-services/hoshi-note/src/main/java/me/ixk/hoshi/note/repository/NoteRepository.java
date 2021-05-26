package me.ixk.hoshi.note.repository;

import me.ixk.hoshi.note.entity.Note;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Otstar Lin
 * @date 2021/5/25 20:01
 */
public interface NoteRepository extends PagingAndSortingRepository<Note, String>, JpaSpecificationExecutor<Note> {}
