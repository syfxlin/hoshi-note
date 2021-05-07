package me.ixk.hoshi.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.ixk.hoshi.ums.entity.UserTest;
import me.ixk.hoshi.ums.mapper.UserTestMapper;
import me.ixk.hoshi.ums.service.UserTestService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author syfxlin
 * @since 2021-05-05
 */
@Service
public class UserTestServiceImpl extends ServiceImpl<UserTestMapper, UserTest> implements UserTestService {}
