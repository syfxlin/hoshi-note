package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.db.entity.Role;
import me.ixk.hoshi.db.repository.RoleRepository;
import me.ixk.hoshi.ums.view.AddRoleView;
import me.ixk.hoshi.ums.view.UpdateRoleView;
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
        return ApiResult.ok(this.roleRepository.findAll(null));
    }

    @ApiOperation("添加权限")
    @PostMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Role> add(@Valid @RequestBody final AddRoleView vo) {
        return ApiResult.ok(this.roleRepository.save(vo.toRole()));
    }

    @ApiOperation("更新权限")
    @PutMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Role> update(@Valid @RequestBody final UpdateRoleView vo) {
        return ApiResult.ok(this.roleRepository.update(vo.toRole()));
    }

    @ApiOperation("删除权限")
    @DeleteMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Void> remove(@RequestParam("id") final String id) {
        this.roleRepository.deleteById(id);
        return ApiResult.ok().build();
    }
}
