package me.ixk.hoshi.mail.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * 访问限制
 *
 * @author Otstar Lin
 * @date 2021/7/20 21:06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("验证码")
@RedisHash("hoshi:mail:rate-limit")
public class RateLimit {

    @Id
    private String ip;

    private String count;

    @TimeToLive
    private Long timeout;
}
