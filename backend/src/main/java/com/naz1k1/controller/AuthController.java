package com.naz1k1.controller;

import com.naz1k1.model.R;
import com.naz1k1.model.dto.auth.LoginDTO;
import com.naz1k1.model.dto.auth.RegisterDTO;
import com.naz1k1.service.AuthService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public R<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        String token = authService.login(loginDTO);
        return R.success(token);
    }

    @PostMapping("/register")
    public R<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return R.success();
    }

    @PostMapping("logout")
    public R<?> logout(@RequestAttribute Long userId) {
        authService.logout(userId);
        return R.success();
    }
}
