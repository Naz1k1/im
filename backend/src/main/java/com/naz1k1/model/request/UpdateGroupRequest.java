package com.naz1k1.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateGroupRequest {
    @NotNull(message = "群组ID不能为空")
    private Long groupId;
    
    @Size(max = 128, message = "群名称最大长度为128")
    private String name;
    
    private String avatar;
    
    @Size(max = 512, message = "群公告最大长度为512")
    private String announcement;
    
    private Integer maxMembers;
} 