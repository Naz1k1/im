package com.naz1k1.model.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupVO {
    private Long id;
    private String name;
    private String avatar;
    private Long creatorId;
    private String creatorName;
    private String announcement;
    private Integer maxMembers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 