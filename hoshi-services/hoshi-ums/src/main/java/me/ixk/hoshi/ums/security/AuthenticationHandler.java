package me.ixk.hoshi.ums.security;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.UserRepository;
import me.ixk.hoshi.web.details.UserDetails;
import me.ixk.hoshi.web.result.ApiResultUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Otstar Lin
 * @date 2021/8/1 18:54
 */
@Component
@RequiredArgsConstructor
public class AuthenticationHandler
    implements AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutSuccessHandler {

    private final UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public void onAuthenticationSuccess(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication
    ) throws IOException {
        final UserDetails details = (UserDetails) authentication.getPrincipal();
        final Optional<User> user = this.userRepository.findById(details.getId());
        if (user.isEmpty()) {
            ApiResultUtil.toResponse(ApiResult.unauthorized("用户 ID 不存在").build(), response);
        } else {
            ApiResultUtil.toResponse(ApiResult.ok(user.get(), "登录成功"), response);
        }
    }

    @Override
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public void onAuthenticationFailure(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AuthenticationException exception
    ) throws IOException {
        ApiResultUtil.toResponse(ApiResult.unauthorized(exception.getMessage()).build(), response);
    }

    @Override
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public void onLogoutSuccess(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication
    ) throws IOException {
        ApiResultUtil.toResponse(ApiResult.ok("注销成功").build(), response);
    }
}
