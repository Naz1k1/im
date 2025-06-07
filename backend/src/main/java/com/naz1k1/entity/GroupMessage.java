package com.naz1k1.entity;

import com.naz1k1.model.response.MessageContent;
import com.naz1k1.model.response.MessageReader;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Data
public class GroupMessage {
    private ObjectId id;
    private Long groupId;
    private Long senderId;
    private MessageContent content;
    private Integer status;  // 0-正常，1-撤回
    private Date sendTime;
    private Integer readCount;
    private List<MessageReader> readers;
}
