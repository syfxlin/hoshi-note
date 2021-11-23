/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.ums.entity.Role;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.RoleRepository;
import me.ixk.hoshi.ums.repository.UserRepository;
import me.ixk.hoshi.ums.request.AddUserView;
import me.ixk.hoshi.ums.request.EditUserRoleView;
import me.ixk.hoshi.ums.request.UpdateUserView;
import me.ixk.hoshi.web.annotation.JsonModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
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
@Api(value = "用户管理控制器")
public class UserManagerController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisIndexedSessionRepository sessionRepository;

    @ApiOperation("列出用户（查询用户）")
    @GetMapping("")
    @PreAuthorize("hasAuthority('USER_MANAGER')")
    public ApiResult<?> list(
        final Pageable page,
        @RequestParam(value = "search", required = false) final String search
    ) {
        final Specification<User> specification = search == null
            ? null
            : (root, query, cb) ->
                cb.or(
                    cb.like(root.get("username"), String.format("%%%s%%", search)),
                    cb.like(root.get("nickname"), String.format("%%%s%%", search)),
                    cb.like(root.get("email"), String.format("%%%s%%", search))
                );
        return ApiResult.page(this.userRepository.findAll(specification, page).map(User::toView), "获取用户成功");
    }

    @ApiOperation("获取用户")
    @GetMapping("/{userId:\\d+}")
    @PreAuthorize("hasAuthority('USER_MANAGER')")
    public ApiResult<?> get(@PathVariable("userId") final Long userId) {
        final Optional<User> user = this.userRepository.findById(userId);
        if (user.isEmpty()) {
            return ApiResult.bindException("用户未找到");
        }
        return ApiResult.ok(user.get().toView(), "获取用户成功");
    }

    @ApiOperation("添加用户")
    @PostMapping("")
    @PreAuthorize("hasAuthority('USER_MANAGER')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> add(@Valid @JsonModel final AddUserView vo) {
        if (this.userRepository.findByUsernameOrEmail(vo.getUsername(), vo.getEmail()).isPresent()) {
            return ApiResult.bindException("用户名或邮箱已存在");
        }
        final User user = User.ofAdd(vo);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(this.roleRepository.findById("USER").get()));
        return ApiResult.ok(this.userRepository.save(user).toView(), "添加用户成功");
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{userId:\\d+}")
    @PreAuthorize("hasAuthority('USER_MANAGER')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> remove(@PathVariable("userId") final Long userId) {
        final Optional<User> user = this.userRepository.findById(userId);
        if (user.isEmpty()) {
            return ApiResult.bindException("用户 ID 不存在");
        }
        this.userRepository.deleteById(userId);
        this.invalidSession(user.get().getUsername());
        return ApiResult.ok("删除用户成功").build();
    }

    @ApiOperation("更新用户")
    @PutMapping("/{userId:\\d+}")
    @PreAuthorize("hasAuthority('USER_MANAGER')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> update(@Valid @JsonModel final UpdateUserView vo) {
        if (this.userRepository.findById(vo.getUserId()).isEmpty()) {
            return ApiResult.bindException("用户 ID 不存在");
        }
        if (vo.getUsername() != null) {
            final Optional<User> byUsername =
                this.userRepository.findByUsernameOrEmail(vo.getUsername(), vo.getEmail());
            if (byUsername.isPresent() && !byUsername.get().getId().equals(vo.getUserId())) {
                return ApiResult.bindException("用户名或邮箱已存在");
            }
        }
        final User user = User.ofUpdate(vo);
        final String password = user.getPassword();
        if (password != null) {
            user.setPassword(this.passwordEncoder.encode(password));
        }
        final User newUser = this.userRepository.update(user);
        if (!newUser.getStatus()) {
            this.invalidSession(newUser.getUsername());
        }
        return ApiResult.ok(newUser.toView(), "更新用户成功");
    }

    @ApiOperation("添加用户角色")
    @PostMapping("/{userId:\\d+}/role")
    @PreAuthorize("hasAuthority('USER_MANAGER')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> addRoles(@Valid @JsonModel final EditUserRoleView vo) {
        final Optional<User> optional = this.userRepository.findById(vo.getUserId());
        if (optional.isEmpty()) {
            return ApiResult.bindException("用户 ID 不存在");
        }
        final User user = optional.get();
        final int size = this.addRoleToUser(user, vo.getRoles());
        final User newUser = this.userRepository.save(user);
        this.invalidSession(newUser.getUsername());
        if (size == vo.getRoles().size()) {
            return ApiResult.ok(newUser.toView(), "所有角色均添加成功");
        } else {
            return ApiResult.ok(newUser.toView(), "部分角色添加成功（可能添加了不存在的角色）");
        }
    }

    @ApiOperation("更改用户角色")
    @PutMapping("/{userId:\\d+}/role")
    @PreAuthorize("hasAuthority('USER_MANAGER')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> updateRoles(@Valid @JsonModel final EditUserRoleView vo) {
        final Optional<User> optional = this.userRepository.findById(vo.getUserId());
        if (optional.isEmpty()) {
            return ApiResult.bindException("用户 ID 不存在");
        }
        final User user = optional.get();
        final Set<Role> roles = vo
            .getRoles()
            .stream()
            .map(this.roleRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
        user.setRoles(roles);
        final User newUser = this.userRepository.save(user);
        this.invalidSession(newUser.getUsername());
        if (roles.size() != vo.getRoles().size()) {
            return ApiResult.ok(newUser.toView(), "所有角色均修改成功");
        } else {
            return ApiResult.ok(newUser.toView(), "部分角色修改成功（可能添加了或删除了不存在的角色）");
        }
    }

    @ApiOperation("删除用户角色")
    @DeleteMapping("/{userId:\\d+}/role")
    @PreAuthorize("hasAuthority('USER_MANAGER')")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<?> removeRoles(
        @PathVariable("userId") @NotNull final Long userId,
        @RequestParam("roles") @NotNull final List<String> roles
    ) {
        final Optional<User> optional = this.userRepository.findById(userId);
        if (optional.isEmpty()) {
            return ApiResult.bindException("用户 ID 不存在");
        }
        final User user = optional.get();
        final int size = this.removeRoleToUser(user, roles);
        final User newUser = this.userRepository.save(user);
        this.invalidSession(newUser.getUsername());
        if (size == roles.size()) {
            return ApiResult.ok(newUser.toView(), "所有角色均删除成功");
        } else {
            return ApiResult.ok(newUser.toView(), "部分角色删除成功（可能删除了不存在的角色）");
        }
    }

    private int addRoleToUser(final User user, final Set<String> addRoleNames) {
        final List<Role> addRoles = addRoleNames
            .stream()
            .map(this.roleRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        user.setRoles(Stream.concat(user.getRoles().stream(), addRoles.stream()).collect(Collectors.toSet()));
        return addRoles.size();
    }

    private int removeRoleToUser(final User user, final List<String> removeRoleNames) {
        final Set<Role> roles = user.getRoles();
        final Set<Role> removeRoles = roles
            .stream()
            .filter(r -> !removeRoleNames.contains(r.getName()))
            .collect(Collectors.toSet());
        user.setRoles(removeRoles);
        return roles.size() - removeRoles.size();
    }

    private void invalidSession(final String username) {
        this.sessionRepository.findByIndexNameAndIndexValue(
                FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,
                username
            )
            .forEach((String key, Session value) -> this.sessionRepository.deleteById(key));
    }
}
