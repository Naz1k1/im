package com.naz1k1.controller;

import com.naz1k1.model.request.LoginDTO;
import com.naz1k1.model.request.RegisterDTO;
import com.naz1k1.model.response.Result;
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

    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterDTO dto) {
        if (authService.register(dto)){
            return Result.success();
        }
        return Result.failed();
    }


    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto));
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return Result.success();
    }
}
