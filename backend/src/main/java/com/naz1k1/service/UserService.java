package com.naz1k1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.naz1k1.entity.User;
import com.naz1k1.model.request.UpdateUserInfoDTO;
import com.naz1k1.model.response.UserInfoVO;

public interface UserService extends IService<User> {
    void updateUserInfo(UpdateUserInfoDTO dto,Long userId);
    void updatePassword(String password,Long userId);
    UserInfoVO getUserInfo(Long userId);
    void updateAvatar(String avatar, Long userId);

}
