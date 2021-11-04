/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.entity;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import me.ixk.hoshi.ums.view.request.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
@Entity
@ApiModel("用户表")
@Accessors(chain = true)
@Table(name = "user")
public class User implements Serializable {

    /**
     * 用户 ID
     */
    @Id
    @ApiModelProperty("用户 ID")
    @Column(name = "id", nullable = false, unique = true, length = 20)
    private String id;

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
    private List<Role> roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info", referencedColumnName = "id")
    @ApiModelProperty("用户信息")
    private UserInfo info;

    @JsonBackReference
    @OneToMany(mappedBy = "follower", targetEntity = Follow.class)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @ApiModelProperty("用户关注了")
    @ToString.Exclude
    private List<Follow> following = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "following", targetEntity = Follow.class)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @ApiModelProperty("用户关注者")
    @ToString.Exclude
    private List<Follow> followers = new ArrayList<>();

    @JsonProperty("followingCount")
    @ApiModelProperty("用户关注了数量")
    private int getFollowingCont() {
        final List<Follow> following = this.getFollowing();
        return following == null ? 0 : following.size();
    }

    @JsonProperty("followersCount")
    @ApiModelProperty("用户关注者数量")
    public int getFollowersCount() {
        final List<Follow> followers = this.getFollowers();
        return followers == null ? 0 : followers.size();
    }

    public static String generateId() {
        return RandomUtil.randomString(10);
    }

    public static User ofRegister(final RegisterUserView vo) {
        return User
            .builder()
            .id(User.generateId())
            .username(vo.getUsername())
            .password(vo.getPassword())
            .nickname(vo.getNickname())
            .email(vo.getEmail())
            .status(true)
            .createdTime(OffsetDateTime.now())
            .info(new UserInfo())
            .build();
    }

    public static User ofAdd(final AddUserView vo) {
        return User
            .builder()
            .id(User.generateId())
            .username(vo.getUsername())
            .password(vo.getPassword())
            .nickname(vo.getNickname())
            .email(vo.getEmail())
            .status(true)
            .createdTime(OffsetDateTime.now())
            .info(new UserInfo())
            .status(vo.getStatus())
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

    public static User ofUpdateName(final UpdateNameView vo, final String userId) {
        return User.builder().id(userId).username(vo.getUsername()).nickname(vo.getNickname()).build();
    }

    public static User ofUpdateEmail(final UpdateEmailView vo, final String userId) {
        return User.builder().id(userId).email(vo.getEmail()).build();
    }

    public static User ofUpdatePassword(final UpdatePasswordView vo, final String userId) {
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
        return Objects.hashCode(id);
    }
}
