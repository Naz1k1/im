package com.naz1k1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.naz1k1.model.dto.friend.FriendRequestDTO;
import com.naz1k1.model.entity.FriendRelationship;
import com.naz1k1.model.vo.friend.FriendVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FriendRelationshipService extends IService<FriendRelationship> {
    @Transactional(rollbackFor = Exception.class)
    void sendRequest(Long userId, FriendRequestDTO dto);

    /**
     * 处理好友请求
     * @param userId 当前用户ID
     * @param notificationId 好友请求通知ID
     * @param accept 是否接受请求
     */
    void handleFriendRequest(Long userId, Long notificationId, boolean accept);

    /**
     * 获取好友列表
     * @param userId 当前用户ID
     * @return 好友列表
     */
    List<FriendVO> getFriendList(Long userId);
}
