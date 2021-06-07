/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.repository;

import java.util.Optional;
import me.ixk.hoshi.ums.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Otstar Lin
 * @date 2021/5/22 20:00
 */
public interface FollowRepository extends PagingAndSortingRepository<Follow, Long>, JpaSpecificationExecutor<Follow> {
    /**
     * 通过关注者获取 Follow 对象
     *
     * @param follower 关注者
     * @param pageable 分页
     * @return Follow
     */
    @Query("SELECT f FROM Follow f WHERE f.follower.id = ?1")
    Page<Follow> findByFollowerId(String follower, Pageable pageable);

    /**
     * 通过关注中获取 Follow 对象
     *
     * @param following 关注中
     * @param pageable  分页
     * @return Follow
     */
    @Query("SELECT f FROM Follow f WHERE f.following.id = ?1")
    Page<Follow> findByFollowingId(String following, Pageable pageable);

    /**
     * 通过关注者和关注中获取 Follow 对象
     *
     * @param follower  关注者
     * @param following 关注中
     * @return Follow
     */
    @Query("SELECT f FROM Follow f WHERE f.follower.id = ?1 AND f.following.id = ?2")
    Optional<Follow> findByFollowerIdAndFollowingId(String follower, String following);
}
