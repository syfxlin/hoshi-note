package me.ixk.hoshi.ums.entity;

import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.common.util.App;
import me.ixk.hoshi.security.entity.Users;
import me.ixk.hoshi.security.service.UsersService;
import org.hibernate.validator.constraints.URL;

/**
 * @author Otstar Lin
 * @date 2021/5/16 下午 10:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserView {

    @NotNull(message = "更新时必须设置用户 id")
    private Long id;

    @Size(min = 3, max = 50, message = "用户名长度应在（3-50）之间")
    private String username;

    @Size(min = 8, max = 50, message = "密码长度应在（3-50）之间")
    private String password;

    @Size(min = 3, max = 50, message = "昵称长度应在（3-50）之间")
    private String nickname;

    @Size(max = 75, message = "邮箱的长度不能超过 75")
    @Email
    private String email;

    @Size(max = 255, message = "头像链接长度不能超过 255")
    @URL
    private String avatar;

    @Size(max = 100, message = "权限的长度不能超过 100")
    private String roles;

    @Min(value = 0, message = "状态值最小不能小于 0")
    @Max(value = 127, message = "状态值最大不能超过 127")
    private Integer status;

    public Users toUsers() {
        final Users user = new Users();
        user.setId(this.getId());
        user.setUsername(this.getUsername());
        user.setPassword(this.getPassword());
        user.setNickname(this.getNickname());
        user.setEmail(this.getEmail());
        user.setAvatar(this.getAvatar());
        user.setRoles(this.getRoles());
        user.setStatus(this.getStatus());
        return user;
    }

    @AssertTrue(message = "用户 ID 不存在")
    protected boolean isExist() {
        return App.getBean(UsersService.class).getById(this.getId()) != null;
    }

    @AssertTrue(message = "用户名已存在")
    protected boolean isUnique() {
        final Users user = App.getBean(UsersService.class).queryUserByName(this.getUsername());
        return user == null || user.getId().equals(this.getId());
    }
}
