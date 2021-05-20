package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.ums.entity.AddRoleView;
import me.ixk.hoshi.ums.entity.UpdateRoleView;
import me.ixk.hoshi.user.entity.Roles;
import me.ixk.hoshi.user.repository.RolesRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author Otstar Lin
 * @date 2021/5/18 下午 9:21
 */
@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Api(value = "权限管理控制器", authorizations = { @Authorization("admin") })
public class RoleManagerController {

    private final RolesRepository rolesRepository;

    @ApiOperation("列出所有权限")
    @GetMapping("")
    public ApiResult<List<Roles>> list() {
        return ApiResult.ok(this.rolesRepository.findAll(null));
    }

    @ApiOperation("添加权限")
    @PostMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Roles> add(@Valid @RequestBody final AddRoleView vo) {
        return ApiResult.ok(this.rolesRepository.save(vo.toRole()));
    }

    @ApiOperation("更新权限")
    @PutMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Roles> update(@Valid @RequestBody final UpdateRoleView vo) {
        return ApiResult.ok(this.rolesRepository.update(vo.toRole()));
    }

    @ApiOperation("删除权限")
    @DeleteMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Void> remove(@RequestParam("id") final String id) {
        this.rolesRepository.deleteById(id);
        return ApiResult.ok().build();
    }
}
