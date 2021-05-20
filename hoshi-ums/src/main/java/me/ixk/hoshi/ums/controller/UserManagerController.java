package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
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
import me.ixk.hoshi.common.result.ApiPage;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.common.result.PageView;
import me.ixk.hoshi.common.util.Jpa;
import me.ixk.hoshi.ums.entity.AddUserView;
import me.ixk.hoshi.ums.entity.EditUserRoleView;
import me.ixk.hoshi.ums.entity.FilterUserView;
import me.ixk.hoshi.ums.entity.UpdateUserView;
import me.ixk.hoshi.user.entity.RoleNames;
import me.ixk.hoshi.user.entity.Roles;
import me.ixk.hoshi.user.entity.Users;
import me.ixk.hoshi.user.repository.RolesRepository;
import me.ixk.hoshi.user.repository.UsersRepository;
import org.springframework.data.jpa.domain.Specification;
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

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation("列出用户（查询用户）")
    @GetMapping("")
    public ApiResult<ApiPage<Users>> list(final PageView<Users> page, final FilterUserView user) {
        final String username = user.getUsername();
        final String nickname = user.getNickname();
        final String email = user.getEmail();
        final Integer status = user.getStatus();
        final Specification<Users> specification = (root, query, criteriaBuilder) -> {
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
        final ApiResult<ApiPage<Users>> users;
        if (page.getPage() != null) {
            users = ApiResult.page(this.usersRepository.findAll(specification, page.toPage()));
        } else {
            users = ApiResult.page(this.usersRepository.findAll(specification));
        }
        return users;
    }

    @ApiOperation("添加用户")
    @PostMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Users> add(@Valid @RequestBody final AddUserView vo) {
        final Users user = vo.toUsers();
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(this.rolesRepository.findById(RoleNames.USER.name()).get()));
        return ApiResult.ok(this.usersRepository.save(user));
    }

    @ApiOperation("删除用户")
    @DeleteMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> remove(@RequestParam("id") final Long id) {
        if (this.usersRepository.findById(id).isEmpty()) {
            return ApiResult.bindException(new String[] { "用户 ID 不存在" });
        }
        this.usersRepository.deleteById(id);
        return ApiResult.ok().build();
    }

    @ApiOperation("更新用户")
    @PutMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Users> update(@Valid @RequestBody final UpdateUserView vo) {
        final Users user = vo.toUsers();
        final String password = user.getPassword();
        if (password != null) {
            user.setPassword(this.passwordEncoder.encode(password));
        }
        return ApiResult.ok(this.usersRepository.update(user));
    }

    @ApiOperation("添加用户权限")
    @PostMapping("/roles")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Users> addRoles(@Valid @RequestBody final EditUserRoleView vo) {
        final Users user = this.usersRepository.findById(vo.getId()).get();
        final int size = this.addRoleToUser(user, vo.getRoles());
        final Users newUser = this.usersRepository.save(user);
        if (size == vo.getRoles().size()) {
            return ApiResult.ok(newUser, "所有权限均添加成功");
        } else {
            return ApiResult.ok(newUser, "部分权限添加成功（可能添加了不存在的权限）");
        }
    }

    @ApiOperation("更改用户权限")
    @PutMapping("/roles")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Users> updateRoles(@Valid @RequestBody final EditUserRoleView vo) {
        final Users user = this.usersRepository.findById(vo.getId()).get();
        final List<Roles> roles = vo
            .getRoles()
            .stream()
            .map(this.rolesRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        user.setRoles(roles);
        final Users newUser = this.usersRepository.save(user);
        if (roles.size() != vo.getRoles().size()) {
            return ApiResult.ok(newUser, "所有权限均修改成功");
        } else {
            return ApiResult.ok(newUser, "部分权限修改成功（可能添加了或删除了不存在的权限）");
        }
    }

    @ApiOperation("删除用户权限")
    @DeleteMapping("/roles")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> removeRoles(
        @RequestParam("id") @NotNull final Long id,
        @RequestParam("roles") @NotNull final List<String> roles
    ) {
        final Optional<Users> optional = this.usersRepository.findById(id);
        if (optional.isEmpty()) {
            return ApiResult.bindException(new String[] { "用户 ID 不存在" });
        }
        final Users user = optional.get();
        final int size = this.removeRoleToUser(user, roles);
        final Users newUser = this.usersRepository.save(user);
        if (size == roles.size()) {
            return ApiResult.ok(newUser, "所有权限均删除成功");
        } else {
            return ApiResult.ok(newUser, "部分权限删除成功（可能删除了不存在的权限）");
        }
    }

    private int addRoleToUser(final Users user, final List<String> addRoleNames) {
        final List<Roles> addRoles = addRoleNames
            .stream()
            .map(this.rolesRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        user.setRoles(
            Stream.concat(user.getRoles().stream(), addRoles.stream()).distinct().collect(Collectors.toList())
        );
        return addRoles.size();
    }

    private int removeRoleToUser(final Users user, final List<String> removeRoleNames) {
        final List<Roles> roles = user.getRoles();
        final List<Roles> removeRoles = roles
            .stream()
            .filter(r -> !removeRoleNames.contains(r.getName()))
            .collect(Collectors.toList());
        user.setRoles(removeRoles);
        return roles.size() - removeRoles.size();
    }
}
