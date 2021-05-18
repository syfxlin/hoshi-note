package me.ixk.hoshi.ums.controller;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiPage;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.common.result.PageView;
import me.ixk.hoshi.security.entity.Users;
import me.ixk.hoshi.security.security.Roles;
import me.ixk.hoshi.security.service.UserRoleRelationService;
import me.ixk.hoshi.security.service.UsersService;
import me.ixk.hoshi.ums.entity.AddUserView;
import me.ixk.hoshi.ums.entity.FilterUserView;
import me.ixk.hoshi.ums.entity.UpdateUserView;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 *
 * @author Otstar Lin
 * @date 2021/5/16 下午 5:13
 */
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Api(value = "用户管理控制器", authorizations = { @Authorization("admin") })
public class UserManagerController {

    private final UsersService usersService;
    private final UserRoleRelationService userRoleRelationService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation("列出用户（查询用户）")
    @GetMapping("")
    public ApiResult<ApiPage<Users>> list(final PageView<Users> page, final FilterUserView user) {
        final QueryChainWrapper<Users> query = usersService.query();
        final String username = user.getUsername();
        final String nickname = user.getNickname();
        final String email = user.getEmail();
        final Integer status = user.getStatus();
        if (username != null) {
            query.eq(Users.USERNAME, username);
        }
        if (nickname != null) {
            query.eq(Users.NICKNAME, nickname);
        }
        if (email != null) {
            query.eq(Users.EMAIL, email);
        }
        if (status != null) {
            query.eq(Users.STATUS, status);
        }
        final ApiResult<ApiPage<Users>> users;
        if (page.getPage() != null) {
            users = ApiResult.page(query.page(page.toPage()));
        } else {
            users = ApiResult.page(query.list());
        }
        return users;
    }

    @ApiOperation("添加用户")
    @PostMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Users> add(@Valid @RequestBody final AddUserView vo) {
        final Users user = vo.toUsers();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        final Users data = usersService.insert(user);
        userRoleRelationService.save(data.getId(), Roles.USER.name());
        return ApiResult.ok(data);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Boolean> remove(@RequestParam("id") final int id) {
        return ApiResult.ok(usersService.removeById(id));
    }

    @ApiOperation("更新用户")
    @PutMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Users> update(@Valid @RequestBody final UpdateUserView vo) {
        final Users user = vo.toUsers();
        final String password = user.getPassword();
        if (password != null) {
            user.setPassword(passwordEncoder.encode(password));
        }
        return ApiResult.ok(usersService.update(user));
    }
}
