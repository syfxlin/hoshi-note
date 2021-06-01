/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.view;

import java.time.OffsetDateTime;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.ums.entity.Role;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 9:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleView {

    @Size(min = 1, max = 50, message = "权限名称的长度应在 1-50 之内")
    @Pattern(regexp = "[A-Z]+", message = "权限名称必须是全大写的英文字符")
    private String roleName;

    private String description;

    private Boolean status;

    public Role toRole() {
        final Role role = new Role();
        role.setName(this.getRoleName());
        role.setDescription(this.getDescription());
        role.setStatus(this.getStatus());
        role.setCreatedTime(OffsetDateTime.now());
        return role;
    }
}