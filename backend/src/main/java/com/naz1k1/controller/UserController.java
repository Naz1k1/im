package com.naz1k1.controller;

import com.naz1k1.model.request.user.UpdatePasswordRequestDTO;
import com.naz1k1.model.request.user.UpdateUserInfoRequestDTO;
import com.naz1k1.model.response.Result;
import com.naz1k1.model.response.UserInfoVO;
import com.naz1k1.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo(@RequestAttribute Long userId) {
        UserInfoVO vo = userService.getUserInfo(userId);
        return Result.success(vo);
    }
    
    @PutMapping("/info")
    public Result<Void> updateUserInfo(
            @Validated @RequestBody UpdateUserInfoRequestDTO request,
            @RequestAttribute Long userId) {
        userService.updateUserInfo(request,userId);
        return Result.success();
    }
    
    @PutMapping("/password")
    public Result<Void> updatePassword(
            @Validated @RequestBody UpdatePasswordRequestDTO request,
            @RequestAttribute Long userId) {
        userService.updatePassword(request,userId);
        return Result.success();
    }
}
