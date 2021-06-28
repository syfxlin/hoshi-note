package me.ixk.hoshi.ums.view.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author Otstar Lin
 * @date 2021/6/27 23:12
 */
@Data
@Builder
@ApiModel("Token 登录响应")
public class TokenLoginView {

    @ApiModelProperty("是否登录成功")
    private final boolean success;

    @JsonInclude(Include.NON_NULL)
    @ApiModelProperty("用户描述信息")
    private final Details details;

    @Data
    @Builder
    public static class Details {

        private final String userId;
        private final String username;
        private final String password;
        private final List<String> roles;
        private final boolean status;
    }
}
