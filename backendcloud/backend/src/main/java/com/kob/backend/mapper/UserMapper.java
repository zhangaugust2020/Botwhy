package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author August
 * @create 2022-08-30 9:21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
