package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.security.entity.Users;
import me.ixk.hoshi.security.security.Roles;
import me.ixk.hoshi.security.service.UserRoleRelationService;
import me.ixk.hoshi.security.service.UsersService;
import me.ixk.hoshi.ums.entity.RegisterUserView;
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

    private final UsersService usersService;
    private final UserRoleRelationService userRoleRelationService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Users> register(@Valid @RequestBody final RegisterUserView vo) {
        final Users user = vo.toUsers();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersService.save(user);
        final Users data = usersService.queryByName(vo.getUsername());
        userRoleRelationService.save(data.getId(), Roles.USER.name());
        return ApiResult.ok(data);
    }
}
