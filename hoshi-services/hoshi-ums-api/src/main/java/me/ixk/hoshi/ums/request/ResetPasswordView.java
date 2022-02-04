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
 * @date 2021/7/22 11:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "找回密码")
public class ResetPasswordView {

    @NotNull(message = "验证码不能为空")
    @Size(min = 36, max = 36, message = "验证码的长度为 36 位")
    @Schema(name = "验证码")
    private String code;

    @NotNull(message = "密码不能为空")
    @Size(min = 8, max = 50, message = "密码长度应在（8-50）之间")
    @Schema(name = "密码")
    private String password;
}
