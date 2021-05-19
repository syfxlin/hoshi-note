package me.ixk.hoshi.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.result.ApiPage;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.common.result.PageView;
import me.ixk.hoshi.common.util.Jpa;
import me.ixk.hoshi.security.entity.Users;
import me.ixk.hoshi.security.repository.UsersRepository;
import me.ixk.hoshi.ums.entity.AddUserView;
import me.ixk.hoshi.ums.entity.FilterUserView;
import me.ixk.hoshi.ums.entity.UpdateUserView;
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
            users = ApiResult.page(usersRepository.findAll(specification, page.toPage()));
        } else {
            users = ApiResult.page(usersRepository.findAll(specification));
        }
        return users;
    }

    @ApiOperation("添加用户")
    @PostMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Users> add(@Valid @RequestBody final AddUserView vo) {
        final Users user = vo.toUsers();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ApiResult.ok(usersRepository.save(user));
    }

    @ApiOperation("删除用户")
    @DeleteMapping("")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<Void> remove(@RequestParam("id") final Long id) {
        usersRepository.deleteById(id);
        return ApiResult.ok().build();
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
        return ApiResult.ok(usersRepository.update(user));
    }
}
