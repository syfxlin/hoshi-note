/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.repository;

import java.util.Optional;
import me.ixk.hoshi.mysql.repository.UpdatingRepository;
import me.ixk.hoshi.ums.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Otstar Lin
 * @date 2021/5/22 20:00
 */
public interface FollowRepository extends UpdatingRepository<Follow, Long>, JpaSpecificationExecutor<Follow> {
    /**
     * 获取关注中的用户
     *
     * @param userId   用户 ID
     * @param pageable 分页
     * @return Follow
     */
    Page<Follow> findByUserId(Long userId, Pageable pageable);

    /**
     * 获取关注者列表
     *
     * @param userId   关注中
     * @param pageable 分页
     * @return Follow
     */
    Page<Follow> findByFollowingId(Long userId, Pageable pageable);

    /**
     * 通过关注者和关注中获取 Follow 对象
     *
     * @param userId      关注者
     * @param followingId 关注中
     * @return Follow
     */
    Optional<Follow> findByUserIdAndFollowingId(Long userId, Long followingId);
}
