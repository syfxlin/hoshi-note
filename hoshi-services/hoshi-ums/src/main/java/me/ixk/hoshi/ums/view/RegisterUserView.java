/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.view;

import java.time.LocalDateTime;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.common.util.App;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.entity.UserInfo;
import me.ixk.hoshi.ums.repository.UserRepository;

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
    @Pattern(regexp = "[^0-9]+", message = "用户名必须包含非数字")
    private String username;

    @Size(min = 8, max = 50, message = "密码长度应在（3-50）之间")
    @NotNull(message = "密码不能为空")
    private String password;

    @Size(min = 3, max = 50, message = "昵称长度应在（3-50）之间")
    @NotNull(message = "昵称不能为空")
    private String nickname;

    @Size(max = 75, message = "邮箱的长度不能超过 75")
    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱格式有误")
    private String email;

    @AssertTrue(message = "用户名已存在")
    protected boolean isUnique() {
        return App.getBean(UserRepository.class).findByUsername(this.username).isEmpty();
    }

    public User toEntity() {
        return User
            .builder()
            .id(User.generateId())
            .username(this.getUsername())
            .password(this.getPassword())
            .nickname(this.getNickname())
            .email(this.getEmail())
            .status(true)
            .createdTime(LocalDateTime.now())
            .info(new UserInfo())
            .build();
    }
}
