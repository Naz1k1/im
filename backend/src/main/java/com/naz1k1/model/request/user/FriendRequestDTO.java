package com.naz1k1.model.request.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FriendRequestDTO {
    @NotNull(message = "好友ID不能为空")
    private Long friendId;
    private String remark;
}
