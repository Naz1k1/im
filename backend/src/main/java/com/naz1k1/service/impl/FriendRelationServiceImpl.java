package com.naz1k1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naz1k1.entity.FriendRelation;
import com.naz1k1.entity.User;
import com.naz1k1.exception.BusinessException;
import com.naz1k1.mapper.FriendRelationMapper;
import com.naz1k1.mapper.UserMapper;
import com.naz1k1.model.enums.FriendStatus;
import com.naz1k1.model.response.FriendVO;
import com.naz1k1.model.response.UserSearchVO;
import com.naz1k1.service.FriendRelationService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FriendRelationServiceImpl extends ServiceImpl<FriendRelationMapper, FriendRelation> implements FriendRelationService {

    private final UserMapper userMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ONLINE_USER_KEY = "online_users";

    public FriendRelationServiceImpl(UserMapper userMapper, RedisTemplate<String, Object> redisTemplate) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 通过username 查找用户
     * @param userId
     * @param username
     * @return
     */
    @Override
    public UserSearchVO searchUser(Long userId, String username) {
        User user = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
        );

        if (user == null) {
            return null;
        }

        // 检查是否已经是好友
        FriendRelation relation = lambdaQuery()
                .eq(FriendRelation::getUserId, userId)
                .eq(FriendRelation::getFriendId, user.getId())
                .eq(FriendRelation::getStatus, FriendStatus.NORMAL.getValue())
                .one();

        UserSearchVO vo = new UserSearchVO();
        BeanUtils.copyProperties(user, vo);

        if (relation == null) {
            vo.setStatus(FriendStatus.NOT_FRIEND);
        } else {
            vo.setStatus(FriendStatus.fromValue(relation.getStatus()));
            vo.setRemark(relation.getRemark());
        }
        return vo;
    }

    /**
     * 发送好友请求
     * @param userId
     * @param friendId
     * @param remark
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFriend(Long userId, Long friendId, String remark) {
        // 检查是否已经是好友
        FriendRelation existingRelation = lambdaQuery()
                .eq(FriendRelation::getUserId, userId)
                .eq(FriendRelation::getFriendId, friendId)
                .eq(FriendRelation::getStatus, FriendStatus.NORMAL.getValue())
                .one();

        if (existingRelation != null) {
            throw new BusinessException("该用户已经是您的好友");
        }

        // 添加好友关系（双向）
        FriendRelation relation1 = new FriendRelation();
        relation1.setUserId(userId);
        relation1.setFriendId(friendId);
        relation1.setRemark(remark);
        relation1.setStatus(FriendStatus.NORMAL.getValue());

        FriendRelation relation2 = new FriendRelation();
        relation2.setUserId(friendId);
        relation2.setFriendId(userId);
        relation2.setStatus(FriendStatus.NORMAL.getValue());

        return save(relation1) && save(relation2);
    }

    /**
     * 删除好友
     * @param userId
     * @param friendId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFriend(Long userId, Long friendId) {
        // 删除好友关系（双向）
        boolean result1 = this.remove(
            new LambdaQueryWrapper<FriendRelation>()
                .eq(FriendRelation::getUserId, userId)
                .eq(FriendRelation::getFriendId, friendId)
                .eq(FriendRelation::getStatus, FriendStatus.NORMAL.getValue())
        );

        boolean result2 = this.remove(
            new LambdaQueryWrapper<FriendRelation>()
                .eq(FriendRelation::getUserId, friendId)
                .eq(FriendRelation::getFriendId, userId)
                .eq(FriendRelation::getStatus, FriendStatus.NORMAL.getValue())
        );

        return result1 && result2;
    }

    @Override
    public boolean updateRemark(Long userId, Long friendId, String remark) {
        FriendRelation relation = lambdaQuery()
                .eq(FriendRelation::getUserId, userId)
                .eq(FriendRelation::getFriendId, friendId)
                .eq(FriendRelation::getStatus, FriendStatus.NORMAL.getValue())
                .one();
        if (relation == null) {
            throw new BusinessException("好友关系不存在");
        }
        relation.setRemark(remark);
        return updateById(relation);
    }

    @Override
    public List<FriendVO> getFriendList(Long userId) {
        // 获取好友关系列表
        List<FriendRelation> relations = lambdaQuery()
                .eq(FriendRelation::getUserId, userId)
                .eq(FriendRelation::getStatus, FriendStatus.NORMAL.getValue())
                .list();

        if (relations.isEmpty()) {
            return Collections.emptyList();
        }

        // 获取好友ID列表
        List<Long> friendIds = relations.stream()
                .map(FriendRelation::getFriendId)
                .collect(Collectors.toList());

        // 获取好友用户信息
        List<User> friends = userMapper.selectBatchIds(friendIds);

        Map<Long, User> friendMap = friends.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        // 转换为VO，并检查在线状态
        return relations.stream()
                .map(rel -> {
                    FriendVO vo = new FriendVO();
                    User friend = friendMap.get(rel.getFriendId());
                    if (friend != null) {
                        vo.setId(friend.getId());
                        vo.setUsername(friend.getUsername());
                        vo.setNickname(friend.getNickname());
                        vo.setAvatar(friend.getAvatar());
                        vo.setRemark(rel.getRemark());
                        vo.setOnline(redisTemplate.opsForSet().isMember(ONLINE_USER_KEY, friend.getId()));
                    }
                    return vo;
                })
                .filter(vo -> vo.getId() != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendVO> getOnlineFriends(Long userId) {
        // 获取好友列表
        List<FriendVO> allFriends = getFriendList(userId);
        
        // 过滤出在线的好友
        return allFriends.stream()
                .filter(FriendVO::getOnline)
                .collect(Collectors.toList());
    }
}
