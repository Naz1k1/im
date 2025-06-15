package com.naz1k1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naz1k1.dao.mapper.FriendNotificationMapper;
import com.naz1k1.dao.mapper.FriendRelationshipMapper;
import com.naz1k1.dao.mapper.UserMapper;
import com.naz1k1.exception.BusinessException;
import com.naz1k1.model.dto.friend.FriendRequestDTO;
import com.naz1k1.model.entity.FriendNotification;
import com.naz1k1.model.entity.FriendRelationship;
import com.naz1k1.model.entity.User;
import com.naz1k1.model.enums.FriendRequestType;
import com.naz1k1.model.enums.FriendStatus;
import com.naz1k1.model.vo.friend.FriendVO;
import com.naz1k1.service.FriendRelationshipService;
import com.naz1k1.utils.FriendValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendRelationshipServiceImpl extends ServiceImpl<FriendRelationshipMapper, FriendRelationship> implements FriendRelationshipService {

    private final UserMapper userMapper;
    private final FriendNotificationMapper friendNotificationMapper;

    public FriendRelationshipServiceImpl(UserMapper userMapper, FriendNotificationMapper friendNotificationMapper) {
        this.userMapper = userMapper;
        this.friendNotificationMapper = friendNotificationMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendRequest(Long userId, FriendRequestDTO dto) {
        // 检查目标用户是否存在
        User targetUser = userMapper.selectById(dto.getFriendId());
        if (targetUser == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查是否已经是好友
        if (FriendValidator.isFriend(baseMapper, userId, dto.getFriendId())) {
            throw new BusinessException("已经是好友关系");
        }

        // 检查是否被拉黑
        if (FriendValidator.isBlocked(baseMapper, userId, dto.getFriendId())) {
            throw new BusinessException("对方已将你拉黑");
        }

        // 创建好友请求通知
        FriendNotification notification = new FriendNotification();
        notification.setSenderId(userId);
        notification.setReceiverId(dto.getFriendId());
        notification.setType(FriendRequestType.REQUEST.getCode());
        notification.setContent(dto.getContent());
        friendNotificationMapper.insert(notification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleFriendRequest(Long userId, Long notificationId, boolean accept) {
        // 获取通知信息
        FriendNotification notification = friendNotificationMapper.selectById(notificationId);
        if (notification == null || !notification.getReceiverId().equals(userId)) {
            throw new BusinessException("无效的好友请求");
        }

        if (accept) {
            // 创建双向好友关系
            FriendRelationship relationship1 = new FriendRelationship();
            relationship1.setUserId(userId);
            relationship1.setFriendId(notification.getSenderId());
            relationship1.setStatus(FriendStatus.ACCEPTED.getCode());
            baseMapper.insert(relationship1);

            FriendRelationship relationship2 = new FriendRelationship();
            relationship2.setUserId(notification.getSenderId());
            relationship2.setFriendId(userId);
            relationship2.setStatus(FriendStatus.ACCEPTED.getCode());
            baseMapper.insert(relationship2);

            // 创建接受通知
            FriendNotification acceptNotification = new FriendNotification();
            acceptNotification.setSenderId(userId);
            acceptNotification.setReceiverId(notification.getSenderId());
            acceptNotification.setType(FriendRequestType.ACCEPT.getCode());
            friendNotificationMapper.insert(acceptNotification);
        } else {
            // 创建拒绝通知
            FriendNotification rejectNotification = new FriendNotification();
            rejectNotification.setSenderId(userId);
            rejectNotification.setReceiverId(notification.getSenderId());
            rejectNotification.setType(FriendRequestType.REJECT.getCode());
            friendNotificationMapper.insert(rejectNotification);
        }

        // 删除原始请求通知
        friendNotificationMapper.deleteById(notificationId);
    }

    @Override
    public List<FriendVO> getFriendList(Long userId) {
        // 查询所有好友关系
        LambdaQueryWrapper<FriendRelationship> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FriendRelationship::getUserId, userId)
                .eq(FriendRelationship::getStatus, FriendStatus.ACCEPTED.getCode());

        List<FriendRelationship> relationships = baseMapper.selectList(queryWrapper);

        // 转换为VO对象
        return relationships.stream().map(relationship -> {
            FriendVO vo = new FriendVO();
            User friend = userMapper.selectById(relationship.getFriendId());
            if (friend != null) {
                BeanUtils.copyProperties(friend, vo);
                vo.setRemark(relationship.getRemark());
                vo.setStatus(relationship.getStatus());
            }
            return vo;
        }).collect(Collectors.toList());
    }
}
