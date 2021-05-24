package me.ixk.hoshi.ums.view;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import me.ixk.hoshi.db.entity.Role;
import me.ixk.hoshi.db.entity.User;
import me.ixk.hoshi.db.entity.UserInfo;

/**
 * 开放的用户信息
 *
 * @author Otstar Lin
 * @date 2021/5/22 17:00
 */
@Data
@Builder
public class PublicUserView {

    private final Long id;
    private final String username;
    private final String nickname;
    private final String email;
    private final LocalDateTime createdTime;
    private final UserInfo info;
    private final List<String> roles;

    public static PublicUserView of(final User user) {
        return PublicUserView
            .builder()
            .id(user.getId())
            .username(user.getUsername())
            .nickname(user.getNickname())
            .email(user.getEmail())
            .createdTime(user.getCreatedTime())
            .info(user.getInfo())
            .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
            .build();
    }
}
