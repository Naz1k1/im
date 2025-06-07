package com.naz1k1.model.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageReader {
    private Long userId;
    private LocalDateTime readTime;
}