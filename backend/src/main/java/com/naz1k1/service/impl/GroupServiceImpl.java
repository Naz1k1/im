package com.naz1k1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naz1k1.entity.Group;
import com.naz1k1.entity.GroupMember;
import com.naz1k1.entity.User;
import com.naz1k1.mapper.GroupMapper;
import com.naz1k1.mapper.GroupMemberMapper;
import com.naz1k1.mapper.UserMapper;
import com.naz1k1.model.request.CreateGroupRequest;
import com.naz1k1.model.request.UpdateGroupRequest;
import com.naz1k1.model.response.GroupVO;
import com.naz1k1.service.GroupService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    private final GroupMemberMapper groupMemberMapper;
    private final UserMapper userMapper;

    public GroupServiceImpl(GroupMemberMapper groupMemberMapper, UserMapper userMapper) {
        this.groupMemberMapper = groupMemberMapper;
        this.userMapper = userMapper;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Group createGroup(CreateGroupRequest request, Long creatorId) {
        // 创建群组
        Group group = new Group();
        BeanUtils.copyProperties(request, group);
        group.setCreatorId(creatorId);
        this.save(group);
        
        // 添加群成员
        List<GroupMember> members = new ArrayList<>();
        // 添加群主
        GroupMember owner = new GroupMember();
        owner.setGroupId(group.getId());
        owner.setUserId(creatorId);
        owner.setRole(2); // 群主
        members.add(owner);
                
        // 添加其他成员
        for (Long memberId : request.getMemberIds()) {
            if (!memberId.equals(creatorId)) {
                GroupMember member = new GroupMember();
                member.setGroupId(group.getId());
                member.setUserId(memberId);
                member.setRole(0); // 普通成员
                members.add(member);
            }
        }
        
        for (GroupMember member : members) {
            groupMemberMapper.insert(member);
        }
        return group;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGroup(UpdateGroupRequest request, Long operatorId) {
        // 验证操作者权限
        GroupMember operator = groupMemberMapper.selectOne(
            new LambdaQueryWrapper<GroupMember>()
                .eq(GroupMember::getGroupId, request.getGroupId())
                .eq(GroupMember::getUserId, operatorId)
        );
                
        if (operator == null || operator.getRole() < 1) { // 需要管理员或群主权限
            throw new RuntimeException("没有权限修改群组信息");
        }
        
        // 更新群组信息
        Group group = new Group();
        BeanUtils.copyProperties(request, group);
        return this.updateById(group);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean dissolveGroup(Long groupId, Long operatorId) {
        // 验证是否为群主
        GroupMember operator = groupMemberMapper.selectOne(
            new LambdaQueryWrapper<GroupMember>()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getUserId, operatorId)
        );
                
        if (operator == null || operator.getRole() != 2) { // 只有群主能解散群组
            throw new RuntimeException("只有群主能解散群组");
        }
        
        // 删除群组成员
        groupMemberMapper.delete(
            new LambdaQueryWrapper<GroupMember>()
                .eq(GroupMember::getGroupId, groupId)
        );
                
        // 删除群组
        return this.removeById(groupId);
    }

    @Override
    public List<GroupVO> getUserGroups(Long userId) {
        // 获取用户加入的群组ID列表
        List<GroupMember> memberships = groupMemberMapper.selectList(
            new LambdaQueryWrapper<GroupMember>()
                .eq(GroupMember::getUserId, userId)
        );

        if (memberships.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取群组信息
        List<Long> groupIds = memberships.stream()
                .map(GroupMember::getGroupId)
                .collect(Collectors.toList());

        List<Group> groups = this.listByIds(groupIds);

        // 获取群主信息
        List<Long> creatorIds = groups.stream()
                .map(Group::getCreatorId)
                .collect(Collectors.toList());

        Map<Long, User> creatorMap = userMapper.selectList(
            new LambdaQueryWrapper<User>()
                .in(User::getId, creatorIds)
        ).stream().collect(Collectors.toMap(User::getId, user -> user));

        // 转换为VO
        return groups.stream().map(group -> {
            GroupVO vo = new GroupVO();
            BeanUtils.copyProperties(group, vo);
            User creator = creatorMap.get(group.getCreatorId());
            if (creator != null) {
                vo.setCreatorName(creator.getNickname());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<GroupMember> getGroupMembers(Long groupId) {
        return groupMemberMapper.selectList(
            new LambdaQueryWrapper<GroupMember>()
                .eq(GroupMember::getGroupId, groupId)
        );
    }
}
