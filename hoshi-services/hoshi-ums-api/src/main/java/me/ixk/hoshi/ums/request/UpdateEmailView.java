package me.ixk.hoshi.ums.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
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
@Schema(name = "更新邮箱")
public class UpdateEmailView {

    @NotNull(message = "验证码不能为空")
    @Size(min = 10, max = 10, message = "验证码的长度为 10 位")
    @Schema(name = "验证码")
    private String code;

    @NotNull(message = "邮箱不能为空")
    @Size(max = 75, message = "邮箱的长度不能超过 75")
    @Email(message = "邮箱格式有误")
    @Schema(name = "邮箱")
    private String email;
}
