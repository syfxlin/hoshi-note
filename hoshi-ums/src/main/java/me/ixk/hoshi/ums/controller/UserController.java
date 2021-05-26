package me.ixk.hoshi.ums.controller;

import static me.ixk.hoshi.security.util.Security.USER_ATTR;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ixk.hoshi.common.annotation.JsonModel;
import me.ixk.hoshi.common.result.ApiResult;
import me.ixk.hoshi.db.entity.User;
import me.ixk.hoshi.db.entity.UserInfo;
import me.ixk.hoshi.db.repository.UserRepository;
import me.ixk.hoshi.ums.view.UserInfoView;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author Otstar Lin
 * @date 2021/5/16 下午 5:12
 */
@RestController
@RequestMapping("/api/users")
@Api("用户控制器")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @ApiOperation("获取用户")
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<Object> user(@ModelAttribute(USER_ATTR) final User user) {
        return ApiResult.ok(user);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<UserInfo> getInfo(@ModelAttribute(USER_ATTR) final User user) {
        return ApiResult.ok(user.getInfo());
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/info")
    @PreAuthorize("isAuthenticated()")
    @Transactional(rollbackFor = { Exception.class, Error.class })
    public ApiResult<UserInfo> updateInfo(
        @ModelAttribute(USER_ATTR) final User user,
        @Valid @JsonModel final UserInfoView vo
    ) {
        final UserInfo info = user.getInfo();
        final String address = vo.getAddress();
        if (address != null) {
            info.setAddress(address);
        }
        final String avatar = vo.getAvatar();
        if (avatar != null) {
            info.setAvatar(avatar);
        }
        final String bio = vo.getBio();
        if (bio != null) {
            info.setBio(bio);
        }
        final String company = vo.getCompany();
        if (company != null) {
            info.setCompany(company);
        }
        final String status = vo.getStatus();
        if (status != null) {
            info.setStatus(status);
        }
        final String url = vo.getUrl();
        if (url != null) {
            info.setUrl(url);
        }
        return ApiResult.ok(this.userRepository.save(user).getInfo());
    }
}
