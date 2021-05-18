package me.ixk.hoshi.ums.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.security.entity.Users;

/**
 * @author Otstar Lin
 * @date 2021/5/16 下午 9:41
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AddUserView extends RegisterUserView {

    @Size(max = 100, message = "权限的长度不能超过 100")
    @NotNull(message = "权限不能为空")
    private String roles;

    @Min(value = 0, message = "状态值最小不能小于 0")
    @Max(value = 127, message = "状态值最大不能超过 127")
    @NotNull(message = "状态值不能为空")
    private Integer status;

    public AddUserView(
        final String username,
        final String password,
        final String nickname,
        final String email,
        final String avatar,
        final String roles,
        final Integer status
    ) {
        super(username, password, nickname, email, avatar);
        this.roles = roles;
        this.status = status;
    }

    @Override
    public Users toUsers() {
        final Users user = super.toUsers();
        user.setRoles(this.getRoles());
        user.setStatus(this.getStatus());
        return user;
    }
}
