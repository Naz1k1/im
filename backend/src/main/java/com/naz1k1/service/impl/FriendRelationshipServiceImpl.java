package com.naz1k1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naz1k1.dao.mapper.FriendRelationshipMapper;
import com.naz1k1.exception.BusinessException;
import com.naz1k1.model.dto.friend.FriendRequestDTO;
import com.naz1k1.model.entity.FriendRelationship;
import com.naz1k1.service.FriendRelationshipService;
import org.springframework.stereotype.Service;

@Service
public class FriendRelationshipServiceImpl extends ServiceImpl<FriendRelationshipMapper, FriendRelationship> implements FriendRelationshipService {

    public void sendRequest(Long id, FriendRequestDTO dto) {
        User user = baseMapper.selectMapsPage(dto.getFriendId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if ()
    }
}
