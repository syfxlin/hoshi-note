package me.ixk.hoshi.ums.handler;

import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiBindException;
import me.ixk.hoshi.security.util.Security;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.UserRepository;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Otstar Lin
 * @date 2021/5/22 16:06
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class SecurityUserAdvice {

    public static final String USER = "me.ixk.hoshi.ums.handler.SecurityUserAdvice.USER";
    private final UserRepository userRepository;

    @ModelAttribute(USER)
    public User user() {
        final String userId = Security.id();
        if (userId == null) {
            return null;
        }
        final Optional<User> optional = this.userRepository.findById(userId);
        if (optional.isEmpty()) {
            throw new ApiBindException(Collections.singletonList("用户登录状态无效（ID 失效，可能用户已被删除）"));
        }
        return optional.get();
    }
}
