package me.ixk.hoshi.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Collections;
import java.util.List;
import me.ixk.hoshi.security.entity.Roles;
import me.ixk.hoshi.security.mapper.RolesMapper;
import me.ixk.hoshi.security.service.RolesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author syfxlin
 * @since 2021-05-18
 */
@Service
public class RolesServiceImpl extends ServiceImpl<RolesMapper, Roles> implements RolesService {

    @Override
    public List<Roles> queriesByUser(final Long userId) {
        final List<Roles> roles = getBaseMapper().listByUserId(userId);
        return roles == null ? Collections.emptyList() : roles;
    }
}