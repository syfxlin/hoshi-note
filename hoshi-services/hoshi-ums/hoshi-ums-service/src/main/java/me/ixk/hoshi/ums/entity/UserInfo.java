/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.ixk.hoshi.ums.view.request.UpdateUserInfoView;

/**
 * 用户信息表
 *
 * @author Otstar Lin
 * @date 2021/5/22 12:07
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@ApiModel("用户信息表")
@Accessors(chain = true)
@Table(name = "user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty("用户信息 ID")
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户头像的地址
     */
    @Column(name = "avatar")
    @ApiModelProperty("用户头像的地址")
    private String avatar;

    @Column(name = "bio")
    @ApiModelProperty("用户简介")
    private String bio;

    @Column(name = "address")
    @ApiModelProperty("用户地址")
    private String address;

    @Column(name = "url")
    @ApiModelProperty("用户链接")
    private String url;

    @Column(name = "company")
    @ApiModelProperty("公司名称")
    private String company;

    @Column(name = "status")
    @ApiModelProperty("用户状态")
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
}
