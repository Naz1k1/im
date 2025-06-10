package com.naz1k1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naz1k1.dao.mapper.UserMapper;
import com.naz1k1.model.entity.User;
import com.naz1k1.service.UserService;
import com.naz1k1.utils.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


}
