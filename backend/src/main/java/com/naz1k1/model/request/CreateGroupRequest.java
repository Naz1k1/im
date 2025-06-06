package com.naz1k1.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CreateGroupRequest {
    @NotBlank(message = "群名称不能为空")
    @Size(max = 128, message = "群名称最大长度为128")
    private String name;
    
    private String avatar;
    
    @Size(max = 512, message = "群公告最大长度为512")
    private String announcement;
    
    private Integer maxMembers = 500;
    
    @NotEmpty(message = "初始成员不能为空")
    private List<Long> memberIds;
} 