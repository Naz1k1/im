package com.naz1k1.controller;

import com.naz1k1.model.request.auth.LoginDTO;
import com.naz1k1.model.request.auth.RegisterDTO;
import com.naz1k1.model.response.Result;
import com.naz1k1.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterDTO dto) {
        authService.register(dto);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody LoginDTO dto) {
        String token = authService.login(dto);
        return Result.success(token);
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestAttribute Long userId) {
        authService.logout(userId);
        return Result.success();
    }
}
