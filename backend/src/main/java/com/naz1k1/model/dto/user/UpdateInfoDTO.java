package com.naz1k1.model.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateInfoDTO {
    @NotBlank(message = "帐号不能为空")
    @Size(min = 4, max = 50, message = "帐号长度4-50字符")
    private String username;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 64, message = "昵称长度必须在4-64个字符之间")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    private String email;
} 