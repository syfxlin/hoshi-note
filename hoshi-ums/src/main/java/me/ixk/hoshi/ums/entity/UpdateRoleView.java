package me.ixk.hoshi.ums.entity;

import java.time.LocalDateTime;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.security.entity.Roles;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 9:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleView {

    @NotNull(message = "更新时必须设置权限的 ID")
    private Integer id;

    @Size(min = 1, max = 50, message = "权限名称的长度应在 1-50 之内")
    @Pattern(regexp = "[A-Z]+", message = "权限名称必须是全大写的英文字符")
    private String name;

    private String description;

    @Min(value = 0, message = "状态值最小不能小于 0")
    @Max(value = 127, message = "状态值最大不能超过 127")
    private Integer status;

    public Roles toRole() {
        final Roles role = new Roles();
        role.setId(this.getId());
        role.setName(this.getName());
        role.setDescription(this.getDescription());
        role.setStatus(this.getStatus());
        role.setCreatedTime(LocalDateTime.now());
        return role;
    }
}
