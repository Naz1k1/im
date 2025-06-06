package com.naz1k1.model.request.user;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UpdateUserInfoRequestDTO {
    
    @Size(max = 64, message = "昵称最大长度为64")
    private String nickname;
    
    @Size(max = 255, message = "头像URL最大长度为255")
    private String avatar;
} 