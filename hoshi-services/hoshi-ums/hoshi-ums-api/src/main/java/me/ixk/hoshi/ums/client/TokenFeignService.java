package me.ixk.hoshi.ums.client;

import me.ixk.hoshi.common.result.ApiEntity;
import me.ixk.hoshi.ums.factory.TokenFallbackFactory;
import me.ixk.hoshi.ums.view.response.TokenLoginView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Token 远程服务
 *
 * @author Otstar Lin
 * @date 2021/6/27 22:13
 */
@FeignClient(name = "hoshi-ums", fallbackFactory = TokenFallbackFactory.class)
public interface TokenFeignService {
    /**
     * Token 登录
     *
     * @param token Token
     * @return 响应
     */
    @PostMapping("/api/token/login/{token}")
    ApiEntity<TokenLoginView> login(@PathVariable("token") String token);
}
