package com.naz1k1.controller;

import com.naz1k1.model.R;
import com.naz1k1.model.dto.user.UpdateInfoDTO;
import com.naz1k1.model.dto.user.UpdatePasswordDTO;
import com.naz1k1.model.vo.user.SelfInfoVO;
import com.naz1k1.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取个人信息
     */
    @GetMapping("/info")
    public R<SelfInfoVO> getInfo(@RequestAttribute Long userId) {
        return R.success(userService.getInfo(userId));
    }

    /**
     * 更新头像（文件上传方式）
     */
    @PostMapping("/avatar/upload")
    public R<?> updateAvatarByFile(@RequestParam("file") MultipartFile file,
                                   @RequestAttribute Long userId) {
        userService.updateAvatar(userId, file);
        return R.success();
    }

    /**
     * 更新密码
     */
    @PostMapping("/password")
    public R<?> updatePassword(@RequestBody @Valid UpdatePasswordDTO dto,
                               @RequestAttribute Long userId) {
        userService.updatePassword(userId, dto);
        return R.success();
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/info")
    public R<?> updateInfo(@RequestBody @Valid UpdateInfoDTO dto,
                          @RequestAttribute Long userId) {
        userService.updateInfo(userId, dto);
        return R.success();
    }
}
