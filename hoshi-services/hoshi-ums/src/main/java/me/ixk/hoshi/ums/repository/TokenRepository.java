/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.repository;

import java.util.List;
import java.util.Optional;
import me.ixk.hoshi.mysql.repository.UpdatingRepository;
import me.ixk.hoshi.ums.entity.Token;

/**
 * @author Otstar Lin
 * @date 2021/6/4 13:07
 */
public interface TokenRepository extends UpdatingRepository<Token, Long> {
    /**
     * 通过 Token 取得 Token 对象
     *
     * @param token Token
     * @return Token
     */
    Optional<Token> findByToken(String token);

    /**
     * 通过 User ID 取得所有 Token
     *
     * @param userId 用户 ID
     * @return Token 列表
     */
    List<Token> findByUserId(final Long userId);

    /**
     * 通过 User ID 和 Token 获取 Token
     *
     * @param token  Token
     * @param userId 用户 ID
     * @return Token
     */
    Optional<Token> findByTokenAndUserId(final String token, final Long userId);

    /**
     * 通过 User ID 和名称获取
     *
     * @param name   Token 名称
     * @param userId 用户 ID
     * @return Token
     */
    Optional<Token> findByNameAndUserId(final String name, final Long userId);
}
