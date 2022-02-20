/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.repository;

import me.ixk.hoshi.db.repository.UpdatingRepository;
import me.ixk.hoshi.file.entity.File;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author Otstar Lin
 * @date 2021/11/15 16:03
 */
public interface FileRepository extends UpdatingRepository<File, Long>, JpaSpecificationExecutor<File> {
    /**
     * 通过用户 ID 和文件信息查找
     *
     * @param userId 用户 ID
     * @param disk   文件信息
     * @return 文件
     */
    Optional<File> findByUserAndDisk(Long userId, String disk);

    /**
     * 通过用户 ID 和文件 ID 查找
     *
     * @param userId 用户 ID
     * @param id     文件 ID
     * @return 文件
     */
    Optional<File> findByUserAndId(Long userId, Long id);
}
