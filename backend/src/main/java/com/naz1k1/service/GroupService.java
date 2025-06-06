package com.naz1k1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.naz1k1.entity.Group;
import com.naz1k1.entity.GroupMember;
import com.naz1k1.model.request.CreateGroupRequest;
import com.naz1k1.model.request.UpdateGroupRequest;
import com.naz1k1.model.response.GroupVO;

import java.util.List;

public interface GroupService extends IService<Group> {
    /**
     * 创建群组
     */
    Group createGroup(CreateGroupRequest request, Long creatorId);
    
    /**
     * 更新群组信息
     */
    boolean updateGroup(UpdateGroupRequest request, Long operatorId);
    
    /**
     * 解散群组
     */
    boolean dissolveGroup(Long groupId, Long operatorId);

    /**
     * 获取用户加入的群组列表
     */
    List<GroupVO> getUserGroups(Long userId);

    /**
     * 获取群组成员列表
     */
    List<GroupMember> getGroupMembers(Long groupId);
}
