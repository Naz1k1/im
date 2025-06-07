package com.naz1k1.service;

import com.naz1k1.entity.GroupMessage;
import com.naz1k1.entity.PrivateMessage;
import com.naz1k1.model.response.MessageReader;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    private final MongoTemplate mongoTemplate;

    public MessageService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    // =============== 私聊消息操作 ===============

    /**
     * 发送私聊消息
     * @param message 私聊实体
     * @return todo
     */
    public PrivateMessage sendPrivateMessage(PrivateMessage message) {
        message.setSendTime(new Date());
        message.setStatus(0);
        return mongoTemplate.save(message,"private_messages");
    }

    /**
     *
     * @param userId
     * @param friendId
     * @param page
     * @param size
     * @return
     */
    public List<PrivateMessage> getPrivateMessages(Long userId, Long friendId, int page, int size) {
        Query query = new Query(new Criteria().orOperator(
                Criteria.where("sendId").is(userId).and("receiverId").is(friendId),
                Criteria.where("sendId").is(friendId).and("receiverId").is(userId)
        )).with(Sort.by(Sort.Direction.DESC,"sendTime"))
                .skip((long) (page - 1) * size )
                .limit(size);
        return mongoTemplate.find(query, PrivateMessage.class, "private_messages");
    }

    public void markPrivateMessageAsRead(ObjectId messageId, Long receiverId) {
        Query query = new Query(Criteria.where("_id").is(messageId)
                .and("receiverId").is(receiverId));
        Update update = Update.update("status",1).set("readTime",new Date());
        mongoTemplate.updateFirst(query, update, "private_messages");
    }

    // 撤回私聊消息
    public void withdrawPrivateMessage(ObjectId messageId, Long senderId) {
        Query query = Query.query(Criteria.where("_id").is(messageId)
                .and("senderId").is(senderId));
        Update update = Update.update("status", 2);
        mongoTemplate.updateFirst(query, update, "private_messages");
    }


    // =============== 群聊消息操作 ===============

    // 发送群聊消息
    public GroupMessage sendGroupMessage(GroupMessage message) {
        message.setSendTime(new Date());
        message.setStatus(0);
        message.setReadCount(0);
        message.setReaders(new ArrayList<>());
        return mongoTemplate.save(message, "group_messages");
    }

    // 获取群聊消息
    public List<GroupMessage> getGroupMessages(Long groupId, int page, int size) {
        Query query = Query.query(Criteria.where("groupId").is(groupId))
                .with(Sort.by(Sort.Direction.DESC, "sendTime"))
                .skip((long) (page - 1) * size)
                .limit(size);
        return mongoTemplate.find(query, GroupMessage.class, "group_messages");
    }

    // 标记群消息已读
    public void markGroupMessageAsRead(ObjectId messageId, Long userId) {
        Query query = Query.query(Criteria.where("_id").is(messageId));
        Update update = new Update()
                .inc("readCount", 1)
                .addToSet("readers", new MessageReader(userId, new Date()));
        mongoTemplate.updateFirst(query, update, "group_messages");
    }

    // 撤回群消息
    public void withdrawGroupMessage(ObjectId messageId, Long senderId) {
        Query query = Query.query(Criteria.where("_id").is(messageId)
                .and("senderId").is(senderId));
        Update update = Update.update("status", 1);
        mongoTemplate.updateFirst(query, update, "group_messages");
    }


    // =============== 通用查询操作 ===============

    // 获取用户的未读消息数
    public long getUnreadPrivateMessageCount(Long userId) {
        Query query = Query.query(Criteria.where("receiverId").is(userId)
                .and("status").is(0));
        return mongoTemplate.count(query, "private_messages");
    }

    // 获取用户在群组中的未读消息数
    public long getUnreadGroupMessageCount(Long groupId, Long userId) {
        Query query = Query.query(Criteria.where("groupId").is(groupId)
                .and("readers.userId").ne(userId));
        return mongoTemplate.count(query, "group_messages");
    }
}
