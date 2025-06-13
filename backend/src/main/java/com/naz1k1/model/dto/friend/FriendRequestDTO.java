package com.naz1k1.model.dto.friend;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class FriendRequestDTO {
    @NotNull(message = "用户ID不能为空")
    private Long friendId;

    @Size(max = 200, message = "验证消息不能超过200字符")
    private String content;
} 