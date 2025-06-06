package com.naz1k1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naz1k1.entity.Group;
import com.naz1k1.entity.GroupMember;
import com.naz1k1.mapper.GroupMapper;
import com.naz1k1.mapper.GroupMemberMapper;
import com.naz1k1.service.GroupMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements GroupMemberService {

    private final GroupMapper groupMapper;

    public GroupMemberServiceImpl(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addMembers(Long groupId, List<Long> memberIds, Long operatorId) {
        // 验证操作者权限
        GroupMember operator = this.baseMapper.selectOne(
            new LambdaQueryWrapper<GroupMember>()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getUserId, operatorId)
        );
                
        if (operator == null || operator.getRole() < 1) { // 需要管理员或群主权限
            throw new RuntimeException("没有权限添加群成员");
        }
        
        // 检查群成员数量限制
        Group group = groupMapper.selectById(groupId);
        Long currentMembers = this.baseMapper.selectCount(
            new LambdaQueryWrapper<GroupMember>()
                .eq(GroupMember::getGroupId, groupId)
        );
                
        if (currentMembers + memberIds.size() > group.getMaxMembers()) {
            throw new RuntimeException("超出群成员数量限制");
        }
        
        // 添加新成员
        for (Long memberId : memberIds) {
            GroupMember member = new GroupMember();
            member.setGroupId(groupId);
            member.setUserId(memberId);
            member.setRole(0); // 普通成员
            this.baseMapper.insert(member);
        }
        
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeMember(Long groupId, Long memberId, Long operatorId) {
        // 验证操作者权限
        GroupMember operator = this.baseMapper.selectOne(
            new LambdaQueryWrapper<GroupMember>()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getUserId, operatorId)
        );
                
        GroupMember target = this.baseMapper.selectOne(
            new LambdaQueryWrapper<GroupMember>()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getUserId, memberId)
        );
                
        if (operator == null || target == null) {
            throw new RuntimeException("操作失败");
        }
        
        // 只有群主能踢管理员
        if (target.getRole() == 1 && operator.getRole() != 2) {
            throw new RuntimeException("没有权限移除管理员");
        }
        
        // 不能踢群主
        if (target.getRole() == 2) {
            throw new RuntimeException("不能移除群主");
        }
        
        // 管理员可以踢普通成员
        if (operator.getRole() >= 1 || operator.getUserId().equals(memberId)) {
            return this.baseMapper.delete(
                new LambdaQueryWrapper<GroupMember>()
                    .eq(GroupMember::getGroupId, groupId)
                    .eq(GroupMember::getUserId, memberId)
            ) > 0;
        }
        
        throw new RuntimeException("没有权限移除群成员");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMemberRole(Long groupId, Long memberId, Integer newRole, Long operatorId) {
        // 验证操作者是否为群主
        GroupMember operator = this.baseMapper.selectOne(
            new LambdaQueryWrapper<GroupMember>()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getUserId, operatorId)
        );
                
        if (operator == null || operator.getRole() != 2) {
            throw new RuntimeException("只有群主能修改成员角色");
        }
        
        // 不能修改群主角色
        if (memberId.equals(operator.getUserId())) {
            throw new RuntimeException("不能修改群主角色");
        }
        
        // 更新成员角色
        GroupMember updateMember = new GroupMember();
        updateMember.setRole(newRole);
        
        return this.baseMapper.update(updateMember,
            new LambdaQueryWrapper<GroupMember>()
                .eq(GroupMember::getGroupId, groupId)
                .eq(GroupMember::getUserId, memberId)
        ) > 0;
    }
}
