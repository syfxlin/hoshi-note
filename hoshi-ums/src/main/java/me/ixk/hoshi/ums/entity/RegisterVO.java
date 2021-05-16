package me.ixk.hoshi.ums.entity;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.ixk.hoshi.common.util.App;
import me.ixk.hoshi.security.service.impl.UsersServiceImpl;

/**
 * @author Otstar Lin
 * @date 2021/5/16 下午 3:28
 */
@Data
@AllArgsConstructor
public class RegisterVO {

    @Size(min = 3, max = 50, message = "用户名长度应在（3-50）之间")
    @NotNull(message = "用户名不能为空")
    private String username;

    @Size(min = 8, max = 50, message = "密码长度应在（3-50）之间")
    @NotNull(message = "密码不能为空")
    private String password;

    @Size(min = 3, max = 50, message = "昵称长度应在（3-50）之间")
    @NotNull(message = "昵称不能为空")
    private String nickname;

    @Size(max = 75, message = "邮箱的长度不能超过 75")
    @NotNull(message = "邮箱不能为空")
    private String email;

    @Size(max = 255, message = "头像链接长度不能超过 255")
    private String avatar;

    @AssertTrue(message = "用户名已存在")
    private boolean isUnique() {
        if (this.username == null) {
            return false;
        }
        return App.getBean(UsersServiceImpl.class).queryUserByName(username) == null;
    }
}
