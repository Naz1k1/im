package com.naz1k1.model.dto.friend;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateRemarkDTO {
    @NotNull(message = "好友ID不能为空")
    private Long friendId;

    @NotBlank(message = "备注不能为空")
    @Size(max = 32, message = "备注不能超过32个字符")
    private String remark;
} 