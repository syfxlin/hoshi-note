/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.annotation.JsonModel;
import me.ixk.hoshi.common.result.ApiPage;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.common.result.PageView;
import me.ixk.hoshi.common.util.Jpa;
import me.ixk.hoshi.ums.entity.Role;
import me.ixk.hoshi.ums.entity.User;
import me.ixk.hoshi.ums.repository.RoleRepository;
import me.ixk.hoshi.ums.repository.UserRepository;
import me.ixk.hoshi.ums.view.AddUserView;
import me.ixk.hoshi.ums.view.EditUserRoleView;
import me.ixk.hoshi.ums.view.FilterUserView;
import me.ixk.hoshi.ums.view.UpdateUserView;
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
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Api(value = "用户管理控制器")
public class UserManagerController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisIndexedSessionRepository sessionRepository;

    @ApiOperation("列出用户（查询用户）")
    @GetMapping("")
    public ApiResult<ApiPage<User>> list(final PageView page, final FilterUserView user) {
        final String username = user.getUsername();
        final String nickname = user.getNickname();
        final String email = user.getEmail();
        final Integer status = user.getStatus();
        final Specification<User> specification = (root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (username != null) {
                predicates.add(criteriaBuilder.like(root.get("username"), Jpa.like(username)));
            }
            if (nickname != null) {
                predicates.add(criteriaBuilder.like(root.get("nickname"), Jpa.like(nickname)));
            }
            if (email != null) {
                predicates.add(criteriaBuilder.like(root.get("email"), Jpa.like(email)));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
        final ApiResult<ApiPage<User>> users;
        if (page.getPage() != null) {
            users = ApiResult.page(this.userRepository.findAll(specification, page.toPage()));
        } else {
            users = ApiResult.page(this.userRepository.findAll(specification));
        }
        return users;
    }

    @ApiOperation("添加用户")
    @PostMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<User> add(@Valid @JsonModel final AddUserView vo) {
        final User user = vo.toEntity();
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(this.roleRepository.findById("USER").get()));
        return ApiResult.ok(this.userRepository.save(user));
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{userId}")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> remove(@PathVariable("userId") final String userId) {
        final Optional<User> user = this.userRepository.findById(userId);
        if (user.isEmpty()) {
            return ApiResult.bindException(new String[] { "用户 ID 不存在" });
        }
        this.userRepository.deleteById(userId);
        this.invalidSession(user.get().getUsername());
        return ApiResult.ok().build();
    }

    @ApiOperation("更新用户")
    @PutMapping("/{userId}")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<User> update(@Valid @JsonModel final UpdateUserView vo) {
        final User user = vo.toEntity();
        final String password = user.getPassword();
        if (password != null) {
            user.setPassword(this.passwordEncoder.encode(password));
        }
        final User newUser = this.userRepository.update(user);
        if (!user.getStatus()) {
            this.invalidSession(newUser.getUsername());
        }
        return ApiResult.ok(newUser);
    }

    @ApiOperation("添加用户权限")
    @PostMapping("/{userId}/role")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<User> addRoles(@Valid @JsonModel final EditUserRoleView vo) {
        final User user = this.userRepository.findById(vo.getUserId()).get();
        final int size = this.addRoleToUser(user, vo.getRoles());
        final User newUser = this.userRepository.save(user);
        this.invalidSession(newUser.getUsername());
        if (size == vo.getRoles().size()) {
            return ApiResult.ok(newUser, "所有权限均添加成功");
        } else {
            return ApiResult.ok(newUser, "部分权限添加成功（可能添加了不存在的权限）");
        }
    }

    @ApiOperation("更改用户权限")
    @PutMapping("/{userId}/role")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<User> updateRoles(@Valid @JsonModel final EditUserRoleView vo) {
        final User user = this.userRepository.findById(vo.getUserId()).get();
        final List<Role> roles = vo
            .getRoles()
            .stream()
            .map(this.roleRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        user.setRoles(roles);
        final User newUser = this.userRepository.save(user);
        this.invalidSession(newUser.getUsername());
        if (roles.size() != vo.getRoles().size()) {
            return ApiResult.ok(newUser, "所有权限均修改成功");
        } else {
            return ApiResult.ok(newUser, "部分权限修改成功（可能添加了或删除了不存在的权限）");
        }
    }

    @ApiOperation("删除用户权限")
    @DeleteMapping("/{userId}/role")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> removeRoles(
        @PathVariable("userId") @NotNull final String id,
        @RequestParam("roles") @NotNull final List<String> roles
    ) {
        final Optional<User> optional = this.userRepository.findById(id);
        if (optional.isEmpty()) {
            return ApiResult.bindException(new String[] { "用户 ID 不存在" });
        }
        final User user = optional.get();
        final int size = this.removeRoleToUser(user, roles);
        final User newUser = this.userRepository.save(user);
        this.invalidSession(newUser.getUsername());
        if (size == roles.size()) {
            return ApiResult.ok(newUser, "所有权限均删除成功");
        } else {
            return ApiResult.ok(newUser, "部分权限删除成功（可能删除了不存在的权限）");
        }
    }

    private int addRoleToUser(final User user, final List<String> addRoleNames) {
        final List<Role> addRoles = addRoleNames
            .stream()
            .map(this.roleRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        user.setRoles(
            Stream.concat(user.getRoles().stream(), addRoles.stream()).distinct().collect(Collectors.toList())
        );
        return addRoles.size();
    }

    private int removeRoleToUser(final User user, final List<String> removeRoleNames) {
        final List<Role> roles = user.getRoles();
        final List<Role> removeRoles = roles
            .stream()
            .filter(r -> !removeRoleNames.contains(r.getName()))
            .collect(Collectors.toList());
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
