package com.naz1k1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.naz1k1.model.dto.user.UpdatePasswordDTO;
import com.naz1k1.model.dto.user.UpdateInfoDTO;
import com.naz1k1.model.entity.User;
import com.naz1k1.model.vo.user.SelfInfoVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends IService<User> {
    SelfInfoVO getInfo(Long id);

    void updateAvatar(Long id, MultipartFile file);

    void updatePassword(Long id, UpdatePasswordDTO dto);

    void updateInfo(Long id, UpdateInfoDTO dto);
}
