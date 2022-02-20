/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import me.ixk.hoshi.ums.request.*;
import me.ixk.hoshi.ums.response.UserView;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户表
 *
 * @author Otstar Lin
 * @date 2021/5/19 下午 2:55
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Schema(name = "用户表")
@Entity
@Table(
    name = "\"user\"",
    indexes = {
        @Index(name = "idx_user_username_unq", columnList = "username", unique = true),
        @Index(name = "idx_user_email_unq", columnList = "email", unique = true),
    }
)
public class User implements Serializable {

    /**
     * 用户 ID
     */
    @Id
    @Schema(name = "用户 ID")
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Schema(name = "用户名")
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    /**
     * 密码
     */
    @Schema(name = "密码")
    @Column(name = "password", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private String password;

    /**
     * 昵称
     */
    @Schema(name = "昵称")
    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    /**
     * 邮箱
     */
    @Schema(name = "邮箱")
    @Column(name = "email", nullable = false, length = 75, unique = true)
    private String email;

    /**
     * 用户状态
     */
    @Column(name = "status")
    @Schema(name = "用户状态")
    private Boolean status;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @Column(name = "created_time")
    private OffsetDateTime createdTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role_relation",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)},
        inverseJoinColumns = {@JoinColumn(name = "role_name", referencedColumnName = "name", nullable = false)}
    )
    @Schema(name = "用户角色列表")
    private Set<Role> roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info", referencedColumnName = "id", nullable = false)
    @Schema(name = "用户信息")
    private UserInfo info;

    @Column(name = "following_count")
    @Schema(name = "用户关注了数量")
    private Integer followingCount;

    @Column(name = "followers_count")
    @Schema(name = "用户关注者数量")
    private Integer followersCount;

    public static User ofRegister(final RegisterUserView vo) {
        return User
            .builder()
            .username(vo.getUsername())
            .password(vo.getPassword())
            .nickname(vo.getNickname())
            .email(vo.getEmail())
            .status(true)
            .createdTime(OffsetDateTime.now())
            .info(new UserInfo())
            .followersCount(0)
            .followingCount(0)
            .build();
    }

    public static User ofAdd(final AddUserView vo) {
        return User
            .builder()
            .username(vo.getUsername())
            .password(vo.getPassword())
            .nickname(vo.getNickname())
            .email(vo.getEmail())
            .status(true)
            .createdTime(OffsetDateTime.now())
            .info(new UserInfo())
            .status(vo.getStatus())
            .followersCount(0)
            .followingCount(0)
            .build();
    }

    public static User ofUpdate(final UpdateUserView vo) {
        return User
            .builder()
            .id(vo.getUserId())
            .username(vo.getUsername())
            .password(vo.getPassword())
            .nickname(vo.getNickname())
            .email(vo.getEmail())
            .status(vo.getStatus())
            .build();
    }

    public static User ofUpdateName(final UpdateNameView vo, final Long userId) {
        return User.builder().id(userId).username(vo.getUsername()).nickname(vo.getNickname()).build();
    }

    public static User ofUpdateEmail(final UpdateEmailView vo, final Long userId) {
        return User.builder().id(userId).email(vo.getEmail()).build();
    }

    public static User ofUpdatePassword(final UpdatePasswordView vo, final Long userId) {
        return User.builder().id(userId).password(vo.getNewPassword()).build();
    }

    public UserView toView() {
        return UserView
            .builder()
            .id(this.id)
            .username(this.username)
            .nickname(this.nickname)
            .email(this.email)
            .status(this.status)
            .createdTime(this.createdTime)
            .roles(this.roles.stream().map(Role::toView).collect(Collectors.toSet()))
            .info(this.info.toView())
            .followingCount(this.followingCount)
            .followersCount(this.followersCount)
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
