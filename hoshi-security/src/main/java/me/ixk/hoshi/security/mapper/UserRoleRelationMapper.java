package me.ixk.hoshi.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.ixk.hoshi.security.entity.UserRoleRelation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户权限关系表 Mapper 接口
 * </p>
 *
 * @author syfxlin
 * @since 2021-05-18
 */
public interface UserRoleRelationMapper extends BaseMapper<UserRoleRelation> {
    @Insert(
        "INSERT INTO user_role_relation (user_id, role_id) VALUES (#{userId}, (SELECT id FROM roles WHERE name = #{roleName}))"
    )
    int insertByRoleName(@Param("userId") final Long userId, @Param("roleName") final String roleName);
}
