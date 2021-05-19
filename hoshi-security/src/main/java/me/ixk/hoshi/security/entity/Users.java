package me.ixk.hoshi.security.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户表
 *
 * @author Otstar Lin
 * @date 2021/5/19 下午 2:55
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@ApiModel("用户表")
@Accessors(chain = true)
@Table(name = "users")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

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
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @Column(name = "password", nullable = false)
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
    @Column(name = "email", nullable = false, length = 75)
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
    private LocalDateTime createdTime;

    /**
     * 用户头像的地址
     */
    @Column(name = "avatar")
    @ApiModelProperty("用户头像的地址")
    private String avatar;

    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role_relation",
        joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "role_name", referencedColumnName = "name") }
    )
    @ApiModelProperty("用户权限列表")
    private List<Roles> roles;
}
