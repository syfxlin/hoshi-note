/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;
import me.ixk.hoshi.api.view.request.user.AddRoleView;
import me.ixk.hoshi.api.view.request.user.UpdateRoleView;

/**
 * 权限表
 *
 * @author Otstar Lin
 * @date 2021/5/19 下午 3:37
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@ApiModel("权限表")
@Accessors(chain = true)
@Table(name = "role")
@EqualsAndHashCode(of = "name")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限名称，必须是大写英文
     */
    @Id
    @ApiModelProperty("权限名称，必须是大写英文")
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column(name = "created_time", nullable = false)
    private OffsetDateTime createdTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    @Column(name = "status", nullable = false)
    private Boolean status;

    /**
     * 权限的描述
     */
    @ApiModelProperty("权限的描述")
    @Column(name = "description", columnDefinition = "text")
    private String description;

    public static Role ofAdd(final AddRoleView vo) {
        return Role
            .builder()
            .name(vo.getName())
            .description(vo.getDescription())
            .status(vo.getStatus())
            .createdTime(OffsetDateTime.now())
            .build();
    }

    public static Role ofUpdate(final UpdateRoleView vo) {
        return Role.builder().name(vo.getRoleName()).description(vo.getDescription()).status(vo.getStatus()).build();
    }
}
