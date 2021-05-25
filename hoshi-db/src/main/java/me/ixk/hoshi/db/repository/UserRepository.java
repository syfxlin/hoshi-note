package me.ixk.hoshi.db.repository;

import java.util.Optional;
import me.ixk.hoshi.common.util.Jpa;
import me.ixk.hoshi.db.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.util.Assert;

/**
 * UserRepository
 *
 * @author Otstar Lin
 * @date 2021/5/19 下午 3:07
 */
public interface UserRepository extends PagingAndSortingRepository<User, String>, JpaSpecificationExecutor<User> {
    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 通过用户名查找有效用户
     *
     * @param username 用户名
     * @return 用户
     */
    Optional<User> findByUsernameAndStatusTrue(String username);

    /**
     * 更新用户
     *
     * @param user 用户
     * @return 用户
     */
    default User update(final User user) {
        final String id = user.getId();
        Assert.notNull(id, "更新时 ID 必须设置");
        final Optional<User> optional = this.findById(id);
        if (optional.isEmpty()) {
            return this.save(user);
        }
        final User original = optional.get();
        return this.save(Jpa.updateNull(user, original));
    }
}
