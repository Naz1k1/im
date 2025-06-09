package com.naz1k1.netty;

import com.naz1k1.entity.GroupMessage;
import com.naz1k1.entity.PrivateMessage;
import com.naz1k1.model.dto.MessageDTO;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class MessageConverter {
    
    public static PrivateMessage toPrivateMessage(MessageDTO dto) {
        PrivateMessage message = new PrivateMessage();
        if (dto.getMessageId() != null) {
            message.setId(new ObjectId(dto.getMessageId()));
        }
        message.setSenderId(dto.getSenderId());
        message.setReceiverId(dto.getReceiverId());
        message.setContent(dto.getContent());
        message.setStatus(0); // 未读状态
        message.setSendTime(new Date(dto.getTimestamp()));
        return message;
    }
    
    public static GroupMessage toGroupMessage(MessageDTO dto) {
        GroupMessage message = new GroupMessage();
        if (dto.getMessageId() != null) {
            message.setId(new ObjectId(dto.getMessageId()));
        }
        message.setGroupId(dto.getReceiverId()); // 群聊时receiverId为groupId
        message.setSenderId(dto.getSenderId());
        message.setContent(dto.getContent());
        message.setStatus(0); // 正常状态
        message.setSendTime(new Date(dto.getTimestamp()));
        message.setReadCount(0);
        message.setReaders(new ArrayList<>());
        return message;
    }
    
    public static MessageDTO toMessageDTO(PrivateMessage message) {
        MessageDTO dto = new MessageDTO();
        dto.setMessageId(message.getId().toString());
        dto.setMessageType(1); // 私聊消息
        dto.setSenderId(message.getSenderId());
        dto.setReceiverId(message.getReceiverId());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getSendTime().getTime());
        return dto;
    }
    
    public static MessageDTO toMessageDTO(GroupMessage message) {
        MessageDTO dto = new MessageDTO();
        dto.setMessageId(message.getId().toString());
        dto.setMessageType(2); // 群聊消息
        dto.setSenderId(message.getSenderId());
        dto.setReceiverId(message.getGroupId()); // 群聊时receiverId为groupId
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getSendTime().getTime());
        return dto;
    }
} 