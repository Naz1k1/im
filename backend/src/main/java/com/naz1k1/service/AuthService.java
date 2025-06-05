package com.naz1k1.service;

import com.naz1k1.entity.User;
import com.naz1k1.mapper.UserMapper;
import com.naz1k1.model.request.RegisterDTO;
import com.naz1k1.utils.PasswordEncoder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper usermapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserMapper usermapper, PasswordEncoder passwordEncoder) {
        this.usermapper = usermapper;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean register(RegisterDTO dto) {
        String salt = passwordEncoder.generateSalt();
        String encodedPassword = passwordEncoder.encode(dto.getPassword(), salt);

        User user = new User();
        BeanUtils.copyProperties(dto, user,"password");
        user.setPassword(encodedPassword);
        user.setSalt(salt);
        return usermapper.insert(user) > 0;
    }
}
