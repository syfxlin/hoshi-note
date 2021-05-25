package me.ixk.hoshi.security.handler;

import static me.ixk.hoshi.security.util.Security.USER_ATTR;

import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiBindException;
import me.ixk.hoshi.db.entity.User;
import me.ixk.hoshi.db.repository.UserRepository;
import me.ixk.hoshi.security.util.Security;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Otstar Lin
 * @date 2021/5/22 16:06
 */
@RestControllerAdvice(basePackages = "me.ixk.hoshi")
@RequiredArgsConstructor
public class SecurityAdvice {

    private final UserRepository userRepository;

    @ModelAttribute(USER_ATTR)
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
