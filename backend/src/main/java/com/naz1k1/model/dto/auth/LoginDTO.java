package com.naz1k1.model.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {
    @NotBlank(message = "帐号不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}
