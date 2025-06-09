package com.naz1k1.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MessageReader {
    private Long userId;
    private Date readTime;
}