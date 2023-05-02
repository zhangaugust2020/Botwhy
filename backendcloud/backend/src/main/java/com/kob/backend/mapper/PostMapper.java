package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.entity.Post;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author August
 * @create 2022-09-02 10:32
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
