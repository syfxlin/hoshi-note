/*
 * Copyright (c) 2021, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.annotation.JsonModel;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.ums.entity.Role;
import me.ixk.hoshi.ums.repository.RoleRepository;
import me.ixk.hoshi.ums.view.request.AddRoleView;
import me.ixk.hoshi.ums.view.request.UpdateRoleView;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 9:21
 */
@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Api(value = "权限管理控制器")
public class RoleManagerController {

    private final RoleRepository roleRepository;

    @ApiOperation("列出所有权限")
    @GetMapping("")
    public ApiResult<List<Role>> list() {
        return ApiResult.ok(this.roleRepository.findAll((Specification<Role>) null), "获取所有权限成功");
    }

    @ApiOperation("获取权限")
    @GetMapping("/{roleName}")
    public ApiResult<Object> get(@PathVariable("roleName") final String roleName) {
        final Optional<Role> role = this.roleRepository.findById(roleName);
        if (role.isEmpty()) {
            return ApiResult.notFound("权限未找到").build();
        }
        return ApiResult.ok(role.get(), "获取权限成功");
    }

    @ApiOperation("添加权限")
    @PostMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Role> add(@Valid @JsonModel final AddRoleView vo) {
        return ApiResult.ok(this.roleRepository.save(Role.ofAdd(vo)), "添加权限成功");
    }

    @ApiOperation("更新权限")
    @PutMapping("/{roleName}")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Role> update(@Valid @JsonModel final UpdateRoleView vo) {
        return ApiResult.ok(this.roleRepository.update(Role.ofUpdate(vo)), "更新权限成功");
    }

    @ApiOperation("删除权限")
    @DeleteMapping("/{roleName}")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Object> remove(@PathVariable("roleName") final String roleName) {
        if (List.of("USER", "ADMIN").contains(roleName)) {
            return ApiResult.bindException("不能删除 USER, ADMIN 权限");
        }
        this.roleRepository.deleteById(roleName);
        return ApiResult.ok("删除权限成功").build();
    }
}
