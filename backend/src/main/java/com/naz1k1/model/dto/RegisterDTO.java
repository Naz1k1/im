package com.naz1k1.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterDTO {
    @NotBlank(message = "帐号不能为空")
    @Size(min = 4, max = 50, message = "帐号长度4-50字符")
    private String username;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 64, message = "用户名长度4-64字符")
    private String nickname;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 32, message = "密码长度8-32字符")
    private String password;

    @Email(message = "邮箱格式不正确")
    private String email;
}
