package me.ixk.hoshi.ums.view;

import java.util.Optional;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.common.util.App;
import me.ixk.hoshi.user.entity.User;
import me.ixk.hoshi.user.repository.UserRepository;

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
    @Email(message = "邮箱格式有误")
    private String email;

    private Boolean status;

    public User toEntity() {
        return User
            .builder()
            .id(this.getId())
            .username(this.getUsername())
            .password(this.getPassword())
            .nickname(this.getNickname())
            .email(this.getEmail())
            .status(this.getStatus())
            .build();
    }

    @AssertTrue(message = "用户 ID 不存在")
    protected boolean isExist() {
        return App.getBean(UserRepository.class).findById(this.getId()).isPresent();
    }

    @AssertTrue(message = "用户名已存在")
    protected boolean isUnique() {
        final Optional<User> user = App.getBean(UserRepository.class).findByUsername(this.getUsername());
        return user.isEmpty() || user.get().getId().equals(this.getId());
    }
}
