package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.annotation.JsonModel;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.db.entity.Role;
import me.ixk.hoshi.db.repository.RoleRepository;
import me.ixk.hoshi.ums.view.AddRoleView;
import me.ixk.hoshi.ums.view.UpdateRoleView;
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
        return ApiResult.ok(this.roleRepository.findAll((Specification<Role>) null));
    }

    @ApiOperation("添加权限")
    @PostMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Role> add(@Valid @JsonModel final AddRoleView vo) {
        return ApiResult.ok(this.roleRepository.save(vo.toRole()));
    }

    @ApiOperation("更新权限")
    @PutMapping("/{roleName}")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Role> update(@Valid @JsonModel final UpdateRoleView vo) {
        return ApiResult.ok(this.roleRepository.update(vo.toRole()));
    }

    @ApiOperation("删除权限")
    @DeleteMapping("/{roleName}")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Void> remove(@PathVariable("roleName") final String roleName) {
        this.roleRepository.deleteById(roleName);
        return ApiResult.ok().build();
    }
}
