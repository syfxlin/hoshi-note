package me.ixk.hoshi.mail.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

/**
 * 验证码
 *
 * @author Otstar Lin
 * @date 2021/7/20 20:49
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("验证码")
@RedisHash("hoshi:mail:verify-code")
public class VerifyCode {

    @ApiModelProperty("主题区分")
    @Indexed
    private String hash;

    @ApiModelProperty("验证码")
    @Id
    private String code;

    @ApiModelProperty("邮箱")
    @Indexed
    private String email;

    @ApiModelProperty("过期时间")
    @TimeToLive
    private Long timeout;
}
