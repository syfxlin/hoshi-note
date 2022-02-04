/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import me.ixk.hoshi.ums.request.UpdateUserInfoView;
import me.ixk.hoshi.ums.response.UserInfoView;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * 用户信息表
 *
 * @author Otstar Lin
 * @date 2021/5/22 12:07
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(name = "用户信息表")
@Accessors(chain = true)
@Entity
@Table(name = "user_info")
public class UserInfo implements Serializable {

    /**
     * ID
     */
    @Id
    @Schema(name = "用户信息 ID")
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户头像的地址
     */
    @Column(name = "avatar")
    @Schema(name = "用户头像的地址")
    private String avatar;

    @Column(name = "bio")
    @Schema(name = "用户简介")
    private String bio;

    @Column(name = "address")
    @Schema(name = "用户地址")
    private String address;

    @Column(name = "url")
    @Schema(name = "用户链接")
    private String url;

    @Column(name = "company")
    @Schema(name = "公司名称")
    private String company;

    @Column(name = "status")
    @Schema(name = "用户状态")
    private String status;

    public static UserInfo ofUpdate(final UpdateUserInfoView vo, final Long id) {
        return UserInfo
            .builder()
            .id(id)
            .avatar(vo.getAvatar())
            .bio(vo.getBio())
            .address(vo.getAddress())
            .url(vo.getUrl())
            .company(vo.getCompany())
            .status(vo.getStatus())
            .build();
    }

    public UserInfoView toView() {
        return UserInfoView
            .builder()
            .avatar(this.avatar)
            .bio(this.bio)
            .address(this.address)
            .url(this.url)
            .company(this.company)
            .status(this.status)
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
        UserInfo userInfo = (UserInfo) o;
        return id != null && Objects.equals(id, userInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
