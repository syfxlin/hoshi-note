package me.ixk.hoshi.ums.factory;

import lombok.extern.slf4j.Slf4j;
import me.ixk.hoshi.common.result.ApiEntity;
import me.ixk.hoshi.ums.client.TokenFeignService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * Token 降级
 *
 * @author Otstar Lin
 * @date 2021/6/27 22:14
 */
@Slf4j
@Component
public class TokenFallbackFactory implements FallbackFactory<TokenFeignService> {

    @Override
    public TokenFeignService create(final Throwable cause) {
        if (log.isErrorEnabled()) {
            log.error("Token 服务调用失败：{}", cause.getMessage(), cause);
        }
        return token -> new ApiEntity<>(500, "服务调用失败：" + cause.getMessage(), null);
    }
}
