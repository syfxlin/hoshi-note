package me.ixk.hoshi.ums.repository;

import java.util.List;
import java.util.Optional;
import me.ixk.hoshi.ums.entity.Follow;
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
     * @return Follow
     */
    @Query("SELECT f FROM Follow f WHERE f.follower.id = ?1")
    List<Follow> findByFollowerId(String follower);

    /**
     * 通过关注中获取 Follow 对象
     *
     * @param following 关注中
     * @return Follow
     */
    @Query("SELECT f FROM Follow f WHERE f.following.id = ?1")
    List<Follow> findByFollowingId(String following);

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
