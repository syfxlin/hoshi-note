/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.repository;

import java.util.List;
import java.util.Optional;
import me.ixk.hoshi.ums.entity.Token;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Otstar Lin
 * @date 2021/6/4 13:07
 */
public interface TokenRepository extends CrudRepository<Token, String> {
    /**
     * 通过 User ID 取得所有 Token
     *
     * @param userId 用户 ID
     * @return Token 列表
     */
    List<Token> findByUserId(final String userId);

    /**
     * 通过 User ID 和 Token 获取 Token
     *
     * @param token  Token
     * @param userId 用户 ID
     * @return Token
     */
    Optional<Token> findByTokenAndUserId(final String token, final String userId);
}
