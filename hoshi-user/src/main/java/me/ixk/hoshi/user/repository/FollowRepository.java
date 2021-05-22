package me.ixk.hoshi.user.repository;

import java.util.List;
import java.util.Optional;
import me.ixk.hoshi.user.entity.Follow;
import me.ixk.hoshi.user.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Otstar Lin
 * @date 2021/5/22 20:00
 */
public interface FollowRepository extends CrudRepository<Follow, Long>, JpaSpecificationExecutor<Follow> {
    /**
     * 通过关注者获取 Follow 对象
     *
     * @param follower 关注者
     * @return Follow
     */
    List<Follow> findByFollower(User follower);

    /**
     * 通过关注中获取 Follow 对象
     *
     * @param following 关注中
     * @return Follow
     */
    List<Follow> findByFollowing(User following);

    /**
     * 通过关注者和关注中获取 Follow 对象
     *
     * @param follower  关注者
     * @param following 关注中
     * @return Follow
     */
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
}
