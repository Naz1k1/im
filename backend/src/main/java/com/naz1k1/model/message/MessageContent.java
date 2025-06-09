package com.naz1k1.model.message;

import lombok.Data;

@Data
public class MessageContent {
    private String type;  // text/image/video/file
    private String text;
    private String mediaUrl;
    private String fileName;
    private Long fileSize;
}
