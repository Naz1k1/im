package com.naz1k1.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.naz1k1.dao.mapper.FriendRelationshipMapper;
import com.naz1k1.model.entity.FriendRelationship;
import com.naz1k1.model.enums.FriendStatus;

public class FriendValidator {
    /**
     * 判断是否为好友
     * @param mapper FriendRelationshipMapper
     * @param userId 用户ID
     * @param targetId 好友ID
     * @return 返回boolean
     */
    public static boolean isFriend(FriendRelationshipMapper mapper, Long userId, Long targetId) {
        LambdaQueryWrapper<FriendRelationship> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(FriendRelationship::getUserId, userId)
                .eq(FriendRelationship::getFriendId, targetId)
                .eq(FriendRelationship::getStatus, FriendStatus.ACCEPTED.getCode());
        return mapper.exists(queryWrapper);
    }

    /**
     * 判断是是否拉黑状态
     * @param mapper FriendRelationshipMapper
     * @param userId 用户ID
     * @param targetId 好友ID
     * @return 返回boolean
     */
    public static boolean isBlocked(FriendRelationshipMapper mapper, Long userId, Long targetId) {
        LambdaQueryWrapper<FriendRelationship> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FriendRelationship::getUserId, userId)
                .eq(FriendRelationship::getFriendId, targetId)
                .eq(FriendRelationship::getStatus, FriendStatus.BLOCKED.getCode());
        return mapper.exists(queryWrapper);
    }

}
