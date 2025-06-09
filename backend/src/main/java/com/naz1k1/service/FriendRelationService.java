package com.naz1k1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.naz1k1.entity.FriendRelation;
import com.naz1k1.model.response.FriendVO;
import com.naz1k1.model.response.UserSearchVO;

import java.util.List;

public interface FriendRelationService extends IService<FriendRelation> {

    UserSearchVO searchUser(Long userId, String username);

    boolean addFriend(Long userId, Long friendId, String remark);

    boolean deleteFriend(Long userId, Long friendId);

    boolean updateRemark(Long userId, Long friendId, String remark);

    List<FriendVO> getFriendList(Long userId);

    List<FriendVO> getOnlineFriends(Long userId);

    List<?> getPendingRelation(Long userId);
}
