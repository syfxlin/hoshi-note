package me.ixk.hoshi.ums.view;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.user.entity.Role;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 9:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRoleView {

    @NotNull(message = "权限名称不能为空")
    @Size(min = 1, max = 50, message = "权限名称的长度应在 1-50 之内")
    @Pattern(regexp = "[A-Z]+", message = "权限名称必须是全大写的英文字符")
    private String name;

    private String description;

    @NotNull(message = "状态值不能为空")
    private Boolean status;

    public Role toRole() {
        final Role role = new Role();
        role.setName(this.getName());
        role.setDescription(this.getDescription());
        role.setStatus(this.getStatus());
        role.setCreatedTime(LocalDateTime.now());
        return role;
    }
}
