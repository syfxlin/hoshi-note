package me.ixk.hoshi.mail.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "验证码")
@RedisHash("hoshi:mail:verify-code")
public class VerifyCode {

    @Schema(name = "主题区分")
    @Indexed
    private String hash;

    @Schema(name = "验证码")
    @Id
    @Indexed
    private String code;

    @Schema(name = "邮箱")
    @Indexed
    private String email;

    @Schema(name = "过期时间")
    @TimeToLive
    private Long timeout;
}
