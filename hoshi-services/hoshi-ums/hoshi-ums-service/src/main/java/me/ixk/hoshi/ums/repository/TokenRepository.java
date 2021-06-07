/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.repository;

import me.ixk.hoshi.ums.entity.Token;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Otstar Lin
 * @date 2021/6/4 13:07
 */
public interface TokenRepository extends CrudRepository<Token, String> {}
