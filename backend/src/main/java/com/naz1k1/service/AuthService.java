package com.naz1k1.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.naz1k1.entity.User;
import com.naz1k1.common.exception.BusinessException;
import com.naz1k1.mapper.UserMapper;
import com.naz1k1.model.request.auth.LoginDTO;
import com.naz1k1.model.request.auth.RegisterDTO;
import com.naz1k1.common.utils.JwtTokenProvider;
import com.naz1k1.common.utils.PasswordEncoder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper usermapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    public AuthService(UserMapper usermapper,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider, RedisTemplate<String, Object> redisTemplate) {
        this.usermapper = usermapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 注册方法
     * @param dto 用户注册数据传输对象，包含用户名、密码、昵称。
     */
    public void register(RegisterDTO dto) {
        if (usermapper.findByUsername(dto.getUsername()) > 0) {
            throw new BusinessException("用户名已存在");
        }
        String salt = passwordEncoder.generateSalt();
        String encodedPassword = passwordEncoder.encode(dto.getPassword(), salt);

        User user = new User();
        BeanUtils.copyProperties(dto, user,"password");
        user.setPassword(encodedPassword);
        user.setSalt(salt);
        user.setAvatar("default-avatar.svg");
        usermapper.insert(user);
    }

    public String login(LoginDTO dto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",dto.getUsername());
        User user = usermapper.selectOne(queryWrapper);
        passwordEncoder.matches(dto.getPassword(), user.getSalt(),user.getPassword());
        return jwtTokenProvider.generateToken(user.getId());
    }

    public void logout(Long userId) {
        redisTemplate.delete("token:" +userId);
    }

}
