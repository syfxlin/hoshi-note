package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Collections;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.db.entity.RoleNames;
import me.ixk.hoshi.db.entity.User;
import me.ixk.hoshi.db.repository.RoleRepository;
import me.ixk.hoshi.db.repository.UserRepository;
import me.ixk.hoshi.ums.view.RegisterUserView;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限控制器
 *
 * @author Otstar Lin
 * @date 2021/5/15 下午 4:53
 */
@RestController
@RequiredArgsConstructor
@Api("权限控制器")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<User> register(@Valid @RequestBody final RegisterUserView vo) {
        final User user = vo.toEntity();
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(this.roleRepository.findById(RoleNames.USER.name()).get()));
        return ApiResult.ok(this.userRepository.save(user));
    }
}
