package me.ixk.hoshi.ums.entity;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.*;
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
@Table(name = "user")
@EqualsAndHashCode(of = { "id" })
@ToString(exclude = { "followers", "following" })
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role_relation",
        joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "role_name", referencedColumnName = "name") }
    )
    @ApiModelProperty("用户权限列表")
    private List<Role> roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info", referencedColumnName = "id")
    @ApiModelProperty("用户信息")
    private UserInfo info;

    @JsonBackReference
    @OneToMany(mappedBy = "follower", targetEntity = Follow.class)
    @ApiModelProperty("用户关注了")
    private List<Follow> following = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "following", targetEntity = Follow.class)
    @ApiModelProperty("用户关注者")
    private List<Follow> followers = new ArrayList<>();

    public static String generateId() {
        return RandomUtil.randomString(10);
    }
}
