package me.ixk.hoshi.ums.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Otstar Lin
 * @date 2021/7/19 21:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "更新密码")
public class UpdatePasswordView {

    @NotNull(message = "旧密码不能为空")
    @Size(min = 8, max = 50, message = "密码长度应在（8-50）之间")
    @Schema(name = "旧密码")
    private String oldPassword;

    @NotNull(message = "新密码不能为空")
    @Size(min = 8, max = 50, message = "密码长度应在（8-50）之间")
    @Schema(name = "新密码")
    private String newPassword;
}
