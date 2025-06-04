package com.naz1k1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naz1k1.entity.Group;
import com.naz1k1.mapper.GroupMapper;
import com.naz1k1.service.GroupService;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {
}
