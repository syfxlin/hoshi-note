package me.ixk.hoshi.ums.view.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("更新密码")
public class UpdatePasswordView {

    @Size(min = 8, max = 50, message = "密码长度应在（3-50）之间")
    @ApiModelProperty("密码")
    private String oldPassword;

    @Size(min = 8, max = 50, message = "密码长度应在（3-50）之间")
    @ApiModelProperty("密码")
    private String newPassword;
}
