package me.ixk.hoshi.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.ixk.hoshi.security.entity.UserRoleRelation;
import me.ixk.hoshi.security.mapper.UserRoleRelationMapper;
import me.ixk.hoshi.security.service.UserRoleRelationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * <p>
 * 用户权限关系表 服务实现类
 * </p>
 *
 * @author syfxlin
 * @since 2021-05-18
 */
@Service
public class UserRoleRelationServiceImpl
    extends ServiceImpl<UserRoleRelationMapper, UserRoleRelation>
    implements UserRoleRelationService {

    @Override
    public boolean save(@NotNull final Long userId, @NotNull final String roleName) {
        Assert.notNull(userId, "用户 ID 不能为空");
        Assert.notNull(roleName, "权限名称不能为空");
        return getBaseMapper().insertByRoleName(userId, roleName) > 0;
    }
}
