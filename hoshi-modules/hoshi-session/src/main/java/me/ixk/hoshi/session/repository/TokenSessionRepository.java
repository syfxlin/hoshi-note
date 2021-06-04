/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.session.repository;

import me.ixk.hoshi.session.entity.TokenSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Otstar Lin
 * @date 2021/6/4 11:20
 */
@Repository
public interface TokenSessionRepository extends CrudRepository<TokenSession, String> {}
