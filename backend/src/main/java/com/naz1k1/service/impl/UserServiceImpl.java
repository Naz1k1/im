package com.naz1k1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naz1k1.entity.User;
import com.naz1k1.mapper.UserMapper;
import com.naz1k1.model.request.user.UpdateUserInfoDTO;
import com.naz1k1.model.response.FriendVO;
import com.naz1k1.model.response.UserInfoVO;
import com.naz1k1.service.UserService;
import com.naz1k1.utils.PasswordEncoder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 获得个人信息
     * @param userId
     * @return
     */
    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = getById(userId);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        return userInfoVO;
    }

    /**
     * 更新个人信息
     * @param dto
     * @param userId
     */
    @Override
    public void updateUserInfo(UpdateUserInfoDTO dto, Long userId) {
        User user = getById(userId);
        BeanUtils.copyProperties(dto, user);
        updateById(user);
    }

    /**
     * 更新密码
     * @param password
     * @param userId
     */
    @Override
    public void updatePassword(String password, Long userId) {
        User user = getById(userId);
        String encodedPassword = passwordEncoder.encode(password, user.getSalt());
        user.setPassword(encodedPassword);
        updateById(user);
    }

    /**
     * 更新头像
     * @param avatar
     * @param userId
     */
    public void updateAvatar(String avatar, Long userId) {
        User user = getById(userId);
        user.setAvatar(avatar);
        updateById(user);
    }


}
