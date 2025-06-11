package com.naz1k1.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.naz1k1.dao.mapper.UserMapper;
import com.naz1k1.model.entity.User;

public class UserValidator {

    // 或者保持单独验证但优化方法名
    public static boolean isEmailExists(UserMapper userMapper, String email) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("email", email)) > 0;
    }

    public static boolean isUsernameExists(UserMapper userMapper, String username) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("username", username)) > 0;
    }
}
