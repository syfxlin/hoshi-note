package me.ixk.hoshi.ums.view;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ixk.hoshi.common.util.App;
import me.ixk.hoshi.db.repository.UserRepository;

/**
 * @author Otstar Lin
 * @date 2021/5/20 15:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditUserRoleView {

    @NotNull(message = "修改权限时必须设置用户 id")
    private Long id;

    private List<String> roles = new ArrayList<>();

    @AssertTrue(message = "用户 ID 不存在")
    protected boolean isExist() {
        return App.getBean(UserRepository.class).findById(this.getId()).isPresent();
    }
}
