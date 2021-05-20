package me.ixk.hoshi.ums.entity;

import java.time.LocalDateTime;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.common.util.App;
import me.ixk.hoshi.user.entity.Users;
import me.ixk.hoshi.user.repository.UsersRepository;
import org.hibernate.validator.constraints.URL;

/**
 * @author Otstar Lin
 * @date 2021/5/16 下午 3:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserView {

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
    @Email
    private String email;

    @Size(max = 255, message = "头像链接长度不能超过 255")
    @URL
    private String avatar;

    @AssertTrue(message = "用户名已存在")
    protected boolean isUnique() {
        return App.getBean(UsersRepository.class).findByUsername(this.username).isEmpty();
    }

    public Users toUsers() {
        final Users user = new Users();
        user.setUsername(this.getUsername());
        user.setPassword(this.getPassword());
        user.setNickname(this.getNickname());
        user.setEmail(this.getEmail());
        user.setAvatar(this.getAvatar());
        user.setStatus(true);
        user.setCreatedTime(LocalDateTime.now());
        return user;
    }
}
