package me.ixk.hoshi.ums.entity;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.user.entity.Users;

/**
 * @author Otstar Lin
 * @date 2021/5/16 下午 9:41
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AddUserView extends RegisterUserView {

    @NotNull(message = "状态值不能为空")
    private Boolean status;

    public AddUserView(
        final String username,
        final String password,
        final String nickname,
        final String email,
        final String avatar,
        final Boolean status
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
