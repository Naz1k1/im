package com.naz1k1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.naz1k1.model.entity.FriendNotification;
import com.naz1k1.model.vo.friend.FriendRequestVO;

import java.util.List;

public interface FriendNotificationService extends IService<FriendNotification> {
    /**
     * 获取好友请求列表
     * @param userId 当前用户ID
     * @return 好友请求列表
     */
    List<FriendRequestVO> getFriendRequests(Long userId);
} 