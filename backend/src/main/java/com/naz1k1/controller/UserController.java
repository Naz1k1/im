package com.naz1k1.controller;

import com.naz1k1.model.response.Result;
import com.naz1k1.model.response.UserInfoVO;
import com.naz1k1.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/info")
    public Result<?> getUserInfo(HttpServletRequest request) {
        Long userId = Long.parseLong(request.getParameter("userId"));
        UserInfoVO vo = userService.getUserInfo(userId);
        return Result.success(vo);
    }
}
