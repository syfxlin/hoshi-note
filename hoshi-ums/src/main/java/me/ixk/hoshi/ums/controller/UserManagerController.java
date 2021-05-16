package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.security.entity.Users;
import me.ixk.hoshi.security.service.UsersService;
import me.ixk.hoshi.ums.entity.AddUserView;
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
    private final PasswordEncoder passwordEncoder;

    @GetMapping("")
    public List<Users> list() {
        return usersService.query().list();
    }

    @PostMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public Users add(@Valid @RequestBody final AddUserView vo) {
        final Users user = vo.toUsers();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersService.save(user);
        return usersService.queryUserByName(vo.getUsername());
    }

    @DeleteMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public boolean remove(@RequestParam("id") final int id) {
        return usersService.removeById(id);
    }

    @PutMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public Users update(@Valid @RequestBody final UpdateUserView vo) {
        final Users user = vo.toUsers();
        final String password = user.getPassword();
        if (password != null) {
            user.setPassword(passwordEncoder.encode(password));
        }
        usersService.updateById(user);
        return usersService.getById(vo.getId());
    }
}
