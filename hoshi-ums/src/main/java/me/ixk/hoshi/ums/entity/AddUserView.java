package me.ixk.hoshi.ums.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
        final Integer status
    ) {
        super(username, password, nickname, email, avatar);
        this.status = status;
    }

    @Override
    public Users toUsers() {
        final Users user = super.toUsers();
        user.setStatus(this.getStatus());
        return user;
    }
}
