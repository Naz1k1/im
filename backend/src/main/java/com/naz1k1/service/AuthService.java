package com.naz1k1.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.naz1k1.dao.mapper.UserMapper;
import com.naz1k1.exception.BusinessException;
import com.naz1k1.model.dto.auth.LoginDTO;
import com.naz1k1.model.dto.auth.RegisterDTO;
import com.naz1k1.model.entity.User;
import com.naz1k1.utils.JwtProvider;
import com.naz1k1.utils.PasswordEncoder;
import com.naz1k1.utils.UserValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                       JwtProvider jwtProvider) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    /**
     * 用户登录方法
     * @param dto 用户登录DTO
     * @return token
     */
    public String login(LoginDTO dto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", dto.getUsername()).last("limit 1");
        User user = userMapper.selectOne(queryWrapper);
        if (user == null || !passwordEncoder.matches(
                dto.getPassword(),
                user.getSalt(),
                user.getPassword())) {
            throw new BusinessException("帐号或密码有误");
        }
       return jwtProvider.generateToken(user.getId());
    }

    /**
     * 用户注册方法
     * @param dto 注册DTO
     */
    public void register(RegisterDTO dto) {
        boolean usernameExists = UserValidator.isUsernameExists(userMapper, dto.getUsername());

        boolean emailExists = UserValidator.isEmailExists(userMapper, dto.getEmail());

        if (usernameExists) {
            throw new BusinessException("用户名已存在");
        }
        if (emailExists) {
            throw new BusinessException("邮箱已被注册");
        }

        String salt = passwordEncoder.generateSalt();
        String encodedPassword = passwordEncoder.encode(dto.getPassword(), salt);

        User user = new User();
        BeanUtils.copyProperties(dto, user, "password");
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        user.setAvatar("/static/images/default-avatar.png");
        userMapper.insert(user);
    }

    public void logout(Long id) {
        return;
    }
}
