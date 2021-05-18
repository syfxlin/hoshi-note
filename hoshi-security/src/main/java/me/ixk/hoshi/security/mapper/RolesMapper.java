package me.ixk.hoshi.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import me.ixk.hoshi.security.entity.Roles;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author syfxlin
 * @since 2021-05-18
 */
public interface RolesMapper extends BaseMapper<Roles> {
    /**
     * 查询指定用户 ID 的所有权限
     *
     * @param userId 用户 ID
     * @return 所有权限
     */
    @Select(
        "SELECT roles.* FROM roles, user_role_relation WHERE roles.id = user_role_relation.role_id AND user_role_relation.user_id = #{userId}"
    )
    List<Roles> listByUserId(@Param("userId") Long userId);
}
