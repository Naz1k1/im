package com.naz1k1.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.naz1k1.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
