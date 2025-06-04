package com.naz1k1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naz1k1.entity.User;
import com.naz1k1.mapper.UserMapper;
import com.naz1k1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final  UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
