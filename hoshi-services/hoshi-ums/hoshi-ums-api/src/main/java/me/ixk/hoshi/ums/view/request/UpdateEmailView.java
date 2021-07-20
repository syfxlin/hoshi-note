package me.ixk.hoshi.ums.view.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/7/19 21:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("更新邮箱")
public class UpdateEmailView {

    @Size(min = 10, max = 10, message = "验证码的长度为 10 位")
    @ApiModelProperty("验证码")
    private String code;

    @Size(max = 75, message = "邮箱的长度不能超过 75")
    @Email(message = "邮箱格式有误")
    @ApiModelProperty("邮箱")
    private String email;
}
