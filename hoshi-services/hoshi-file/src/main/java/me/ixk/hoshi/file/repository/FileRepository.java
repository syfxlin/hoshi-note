/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.file.repository;

import me.ixk.hoshi.file.entity.File;
import me.ixk.hoshi.mysql.repository.UpdatingRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Otstar Lin
 * @date 2021/11/15 16:03
 */
public interface FileRepository extends UpdatingRepository<File, Long>, JpaSpecificationExecutor<File> {}
