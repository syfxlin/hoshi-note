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
 * @date 2021/7/19 21:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("更新用户名")
public class UpdateNameView {

    @Size(min = 3, max = 50, message = "用户名长度应在（3-50）之间")
    @ApiModelProperty("用户名")
    private String username;

    @Size(min = 3, max = 50, message = "昵称长度应在（3-50）之间")
    @ApiModelProperty("昵称")
    private String nickname;
}
