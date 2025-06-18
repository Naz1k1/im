package com.naz1k1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naz1k1.dao.mapper.FriendNotificationMapper;
import com.naz1k1.dao.mapper.UserMapper;
import com.naz1k1.model.entity.FriendNotification;
import com.naz1k1.model.entity.FriendRelationship;
import com.naz1k1.model.entity.User;
import com.naz1k1.model.enums.FriendRequestType;
import com.naz1k1.model.vo.friend.FriendRequestVO;
import com.naz1k1.service.FriendNotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendNotificationServiceImpl extends ServiceImpl<FriendNotificationMapper, FriendNotification> implements FriendNotificationService {

    private final UserMapper userMapper;

    public FriendNotificationServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 获得待处理的好友请求
     * @param userId 当前用户ID
     * @return vo
     */
    @Override
    public List<FriendRequestVO> getFriendRequests(Long userId) {
        LambdaQueryWrapper<FriendNotification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper
                .eq(FriendNotification::getReceiverId, userId))
                .eq(FriendNotification::getType, FriendRequestType.REQUEST.getCode())
                .orderByDesc(FriendNotification::getCreatedAt);

        List<FriendNotification> notifications = this.list(queryWrapper);

        // 转换为VO对象
        return notifications.stream().map(notification -> {
            FriendRequestVO vo = new FriendRequestVO();
            BeanUtils.copyProperties(notification, vo);
            
            // 获取发送者信息
            User sender = userMapper.selectById(notification.getSenderId());
            if (sender != null) {
                vo.setSenderName(sender.getNickname());
                vo.setSenderAvatar(sender.getAvatar());
            }

            return vo;
        }).collect(Collectors.toList());
    }
} 