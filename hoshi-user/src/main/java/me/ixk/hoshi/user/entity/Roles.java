package me.ixk.hoshi.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

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
@Table(name = "roles")
@EqualsAndHashCode(of = "name")
@ToString(exclude = "users")
public class Roles implements Serializable {

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
    private LocalDateTime createdTime;

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

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    @ApiModelProperty("用户列表")
    private List<Users> users;
}
