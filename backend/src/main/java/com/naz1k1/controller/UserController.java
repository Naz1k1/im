package com.naz1k1.controller;

import com.naz1k1.model.response.Result;
import com.naz1k1.model.response.UserInfoVO;
import com.naz1k1.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public Result<?> getUserInfo(@RequestAttribute Long userId) {
        UserInfoVO vo = userService.getUserInfo(userId);
        return Result.success(vo);
    }
}
