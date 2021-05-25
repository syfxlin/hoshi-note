package me.ixk.hoshi.db.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Otstar Lin
 * @date 2021/5/25 16:25
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@ApiModel("空间表")
@Accessors(chain = true)
@Table(name = "workspace")
@EqualsAndHashCode(of = { "id" })
public class Workspace {

    @Id
    @ApiModelProperty("空间 ID")
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @ApiModelProperty("空间名称")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ApiModelProperty("空间描述")
    @Column(name = "description")
    private String description;

    @ApiModelProperty("域名")
    @Column(name = "domain", unique = true, length = 50)
    private String domain;

    @ApiModelProperty("空间图标")
    @Column(name = "icon")
    private String icon;

    @ApiModelProperty("空间计划")
    @Column(name = "plan", nullable = false, length = 50)
    private String plan;

    @ApiModelProperty("创建时间")
    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    @ApiModelProperty("笔记")
    @OneToMany(mappedBy = "workspace")
    private List<Note> notes;

    @ApiModelProperty("用户")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
