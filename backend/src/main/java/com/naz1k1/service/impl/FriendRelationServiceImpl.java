package com.naz1k1.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FriendRelationServiceImpl extends ServiceImpl<FriendRelationMapper, FriendRelation> implements FriendRelationService {

    private final UserMapper userMapper;

    public FriendRelationServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 通过username 查找用户
     * @param userId
     * @param username
     * @return
     */
    @Override
    public UserSearchVO searchUser(Long userId, String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            return null;
        }

        FriendRelation friendRelation = lambdaQuery()
                .eq(FriendRelation::getUserId,userId)
                .eq(FriendRelation::getFriendId,user.getId())
                .one();

        UserSearchVO vo = new UserSearchVO();
        BeanUtils.copyProperties(user,vo);

        if (friendRelation == null) {
            vo.setStatus(FriendStatus.NOT_FRIEND);
        }else {
            vo.setStatus(FriendStatus.fromValue(friendRelation.getStatus()));
            vo.setRemark(friendRelation.getRemark());
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
    public boolean addFriend(Long userId, Long friendId, String remark) {
        FriendRelation relation = lambdaQuery()
                .eq(FriendRelation::getUserId,userId)
                .eq(FriendRelation::getFriendId,friendId)
                .one();

        if ( relation != null ) {
            if (relation.getStatus() == FriendStatus.NORMAL.getValue()) {
                throw new BusinessException("该用户已经是您的好友");
            }
            if (relation.getStatus() == FriendStatus.PENDING.getValue()) {
                throw new BusinessException("好友请求已发送，请等待对方确认");
            }
        }

        FriendRelation newRelation = new FriendRelation();
        newRelation.setUserId(userId);
        newRelation.setFriendId(friendId);
        newRelation.setRemark(remark);
        newRelation.setStatus(FriendStatus.PENDING.getValue());

        return save(newRelation);
    }

    /**
     * 删除好友
     * @param userId
     * @param friendId
     * @return
     */
    @Override
    public boolean deleteFriend(Long userId, Long friendId) {
        FriendRelation relation = isRelationNormal(userId, friendId);

        if ( relation == null ) {
            throw new BusinessException("好友关系不存在");
        }

        relation.setStatus(FriendStatus.DELETED.getValue());

        return updateById(relation);
    }

    @Override
    public boolean updateRemark(Long userId, Long friendId, String remark) {
        FriendRelation relation = isRelationNormal(userId, friendId);
        if ( relation == null ) {
            throw new BusinessException("好友关系不存在");
        }
        relation.setRemark(remark);
        return updateById(relation);
    }

    @Override
    public List<FriendVO> getFriendList(Long userId) {
        List<FriendRelation> relations = lambdaQuery()
                .eq(FriendRelation::getUserId, userId)
                .eq(FriendRelation::getStatus, FriendStatus.NORMAL.getValue())
                .list();

        if (relations.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> friendIds = relations.stream()
                .map(FriendRelation::getFriendId)
                .collect(Collectors.toList());

        List<User> friends = userMapper.selectBatchIds(friendIds);

        Map<Long, User> friendIdToUserMap = friends.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return relations.stream()
                .map(FriendRelation::getFriendId)
                .map(friendIdToUserMap::get)
                .filter(Objects::nonNull)
                .map(friend -> {
                    FriendVO vo = new FriendVO();
                    BeanUtils.copyProperties(friend, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    //TODO 可以再次优化该方法
    private FriendRelation isRelationNormal(Long userId, Long friendId) {

        return lambdaQuery()
                .eq(FriendRelation::getUserId,userId)
                .eq(FriendRelation::getFriendId,friendId)
                .eq(FriendRelation::getStatus,FriendStatus.NORMAL.getValue())
                .one();
    }
}
