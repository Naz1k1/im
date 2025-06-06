package com.naz1k1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.naz1k1.entity.GroupMember;

import java.util.List;

public interface GroupMemberService extends IService<GroupMember> {
    /**
     * 添加群成员
     */
    boolean addMembers(Long groupId, List<Long> memberIds, Long operatorId);
    
    /**
     * 移除群成员
     */
    boolean removeMember(Long groupId, Long memberId, Long operatorId);
    
    /**
     * 更新成员角色
     */
    boolean updateMemberRole(Long groupId, Long memberId, Integer newRole, Long operatorId);
}
