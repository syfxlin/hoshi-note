package me.ixk.hoshi.ums.controller;

import java.time.LocalDateTime;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.Result;
import me.ixk.hoshi.security.entity.Users;
import me.ixk.hoshi.security.security.Roles;
import me.ixk.hoshi.security.security.Status;
import me.ixk.hoshi.security.service.UsersService;
import me.ixk.hoshi.ums.entity.RegisterVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Otstar Lin
 * @date 2021/5/15 下午 4:53
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UsersService usersService;

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public Result<?> register(@Valid @RequestBody final RegisterVO vo) {
        final Users user = new Users();
        user.setUsername(vo.getUsername());
        user.setPassword(vo.getPassword());
        user.setRoles(Roles.USER.name());
        user.setNickname(vo.getNickname());
        user.setEmail(vo.getEmail());
        user.setAvatar(vo.getAvatar());
        user.setStatus(Status.ENABLE.ordinal());
        user.setCreatedTime(LocalDateTime.now());
        usersService.save(user);
        return Result.data(usersService.queryUserByName(vo.getUsername()));
    }
}
