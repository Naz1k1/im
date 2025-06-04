package com.naz1k1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naz1k1.entity.GroupMember;
import com.naz1k1.mapper.GroupMemberMapper;
import com.naz1k1.service.GroupMemberService;
import org.springframework.stereotype.Service;

@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements GroupMemberService {
}
