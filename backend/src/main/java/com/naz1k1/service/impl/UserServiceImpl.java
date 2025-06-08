package com.naz1k1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naz1k1.entity.User;
import com.naz1k1.mapper.UserMapper;
import com.naz1k1.model.request.user.UpdatePasswordRequestDTO;
import com.naz1k1.model.request.user.UpdateUserInfoRequestDTO;
import com.naz1k1.model.response.UserInfoVO;
import com.naz1k1.service.UserService;
import com.naz1k1.common.utils.PasswordEncoder;
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
    public void updateUserInfo(UpdateUserInfoRequestDTO dto, Long userId) {
        User user = getById(userId);
        BeanUtils.copyProperties(dto, user);
        updateById(user);
    }


    @Override
    public void updatePassword(UpdatePasswordRequestDTO dto, Long userId) {
        User user = getById(userId);
        if (passwordEncoder.matches(dto.getOldPassword(),user.getSalt(), user.getPassword())){
            if (dto.getNewPassword().equals(dto.getConfirmPassword())){
                String encodedPassword = passwordEncoder.encode(dto.getNewPassword(), user.getSalt());
                user.setPassword(encodedPassword);
                updateById(user);
            }
            throw new RuntimeException("两次密码输入不一致");
        }
        throw new RuntimeException("请确认输入是否有误");
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
