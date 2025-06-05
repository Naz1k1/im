package com.naz1k1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("im_group")
public class Group {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String avatar;
    private Long creatorId;
    private String announcement;
    private int maxMembers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
