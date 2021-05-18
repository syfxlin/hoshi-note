package me.ixk.hoshi.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.ixk.hoshi.security.entity.Users;
import me.ixk.hoshi.security.mapper.UsersMapper;
import me.ixk.hoshi.security.service.UsersService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author syfxlin
 * @since 2021-05-18
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {}
