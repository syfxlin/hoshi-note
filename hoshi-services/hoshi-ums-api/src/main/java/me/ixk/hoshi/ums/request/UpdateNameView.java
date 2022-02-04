package me.ixk.hoshi.ums.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

/**
 * @author Otstar Lin
 * @date 2021/7/19 21:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "更新用户名")
public class UpdateNameView {

    @Size(min = 3, max = 50, message = "用户名长度应在（3-50）之间")
    @Schema(name = "用户名")
    private String username;

    @Size(min = 3, max = 50, message = "昵称长度应在（3-50）之间")
    @Schema(name = "昵称")
    private String nickname;
}
