package com.naz1k1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.naz1k1.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    int findByUsername(@Param("username") String username);
    User findById(@Param("id") Long userId);
}
