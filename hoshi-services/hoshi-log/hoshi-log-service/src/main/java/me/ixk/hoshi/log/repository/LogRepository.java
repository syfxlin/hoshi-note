/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.log.repository;

import me.ixk.hoshi.log.entity.Log;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Otstar Lin
 * @date 2021/6/4 20:40
 */
public interface LogRepository extends PagingAndSortingRepository<Log, Long> {}
