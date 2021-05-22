package me.ixk.hoshi.user.repository;

import java.util.Optional;
import me.ixk.hoshi.common.util.Jpa;
import me.ixk.hoshi.user.entity.Role;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.Assert;

/**
 * RoleRepository
 *
 * @author Otstar Lin
 * @date 2021/5/19 下午 3:07
 */
public interface RoleRepository extends CrudRepository<Role, String>, JpaSpecificationExecutor<Role> {
    /**
     * 更新权限
     *
     * @param role 权限
     * @return 权限
     */
    default Role update(final Role role) {
        final String name = role.getName();
        Assert.notNull(name, "更新时权限名称必须设置");
        final Optional<Role> optional = this.findById(name);
        if (optional.isEmpty()) {
            return this.save(role);
        }
        final Role original = optional.get();
        return this.save(Jpa.updateNull(role, original));
    }
}
