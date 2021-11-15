/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import me.ixk.hoshi.ums.request.*;
import org.hibernate.Hibernate;

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
@ApiModel("用户表")
@Entity
@Table(name = "user")
public class User implements Serializable {

    /**
     * 用户 ID
     */
    @Id
    @ApiModelProperty("用户 ID")
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @Column(name = "password", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private String password;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    @Column(name = "email", nullable = false, length = 75, unique = true)
    private String email;

    /**
     * 用户状态
     */
    @Column(name = "status")
    @ApiModelProperty("用户状态")
    private Boolean status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column(name = "created_time")
    private OffsetDateTime createdTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role_relation",
        joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "role_name", referencedColumnName = "name") }
    )
    @ApiModelProperty("用户角色列表")
    private Set<Role> roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info", referencedColumnName = "id")
    @ApiModelProperty("用户信息")
    private UserInfo info;

    @Column(name = "following_count")
    @ApiModelProperty("用户关注了数量")
    private Integer followingCount;

    @Column(name = "followers_count")
    @ApiModelProperty("用户关注者数量")
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