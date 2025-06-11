package com.naz1k1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naz1k1.dao.mapper.UserMapper;
import com.naz1k1.exception.BusinessException;
import com.naz1k1.model.dto.user.UpdatePasswordDTO;
import com.naz1k1.model.dto.user.UpdateInfoDTO;
import com.naz1k1.model.entity.User;
import com.naz1k1.model.enums.RCode;
import com.naz1k1.model.vo.user.SelfInfoVO;
import com.naz1k1.service.UserService;
import com.naz1k1.utils.FileUtils;
import com.naz1k1.utils.PasswordEncoder;
import com.naz1k1.utils.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final FileUtils fileUtils;

    public UserServiceImpl(PasswordEncoder passwordEncoder, FileUtils fileUtils) {
        this.passwordEncoder = passwordEncoder;
        this.fileUtils = fileUtils;
    }

    @PostConstruct
    public void init() {
        fileUtils.ensureAvatarDirectoryExists();
    }

    /**
     * 获得个人信息
     * @param id 用户ID
     * @return 个人信息VO
     */
    @Override
    public SelfInfoVO getInfo(Long id) {
        SelfInfoVO selfInfoVO = new SelfInfoVO();
        User user = baseMapper.selectById(id);
        BeanUtils.copyProperties(user, selfInfoVO);
        return selfInfoVO;
    }

    /**
     * 更新用户头像
     * @param id 用户ID
     * @param file 头像文件
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAvatar(Long id, MultipartFile file) {
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        String newAvatarPath = fileUtils.uploadAvatar(file);

        fileUtils.deleteOldAvatar(user.getAvatar());

        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setAvatar(newAvatarPath);

        if (!this.updateById(updateUser)) {
            throw new BusinessException("更新头像失败");
        }
        log.info("用户 {} 更新头像成功", id);
    }

    /**
     * 更新用户密码
     * @param id 用户ID
     * @param dto 密码更新DTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long id, UpdatePasswordDTO dto) {
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证原密码
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getSalt(), user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 生成新的盐值和密码
        String newSalt = passwordEncoder.generateSalt();
        String newEncodedPassword = passwordEncoder.encode(dto.getNewPassword(), newSalt);

        // 更新密码和盐值
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setPassword(newEncodedPassword);
        updateUser.setSalt(newSalt);

        if (!this.updateById(updateUser)) {
            throw new BusinessException("更新密码失败");
        }
        log.info("用户 {} 更新密码成功", id);
    }

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param dto 用户信息更新DTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInfo(Long id, UpdateInfoDTO dto) {
        boolean usernameExists = UserValidator.isUsernameExists(baseMapper, dto.getUsername());

        boolean emailExists = UserValidator.isEmailExists(baseMapper, dto.getEmail());

        if (usernameExists) {
            throw new BusinessException("用户名已存在");
        }
        if (emailExists) {
            throw new BusinessException("邮箱已被注册");
        }
        User user = baseMapper.selectById(id);
        if (user == null) {
            //TODO
            throw new BusinessException(RCode.BAD_REQUEST);
        }
        BeanUtils.copyProperties(dto, user);
        baseMapper.updateById(user);
        log.info("用户 {} 更新信息成功", id);
    }
}
