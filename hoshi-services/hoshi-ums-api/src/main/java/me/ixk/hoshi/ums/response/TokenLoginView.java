package me.ixk.hoshi.ums.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Otstar Lin
 * @date 2021/6/27 23:12
 */
@Data
@Builder
@Schema(name = "Token 登录响应")
public class TokenLoginView {

    @Schema(name = "是否登录成功")
    private final boolean success;

    @JsonInclude(Include.NON_NULL)
    @Schema(name = "用户描述信息")
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
