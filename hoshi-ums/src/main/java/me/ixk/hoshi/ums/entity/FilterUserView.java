package me.ixk.hoshi.ums.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 12:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterUserView {

    @Size(min = 3, max = 50, message = "用户名长度应在（3-50）之间")
    private String username;

    @Size(min = 3, max = 50, message = "昵称长度应在（3-50）之间")
    private String nickname;

    @Size(max = 75, message = "邮箱的长度不能超过 75")
    @Email
    private String email;

    @Min(value = 0, message = "状态值最小不能小于 0")
    @Max(value = 127, message = "状态值最大不能超过 127")
    private Integer status;
}
