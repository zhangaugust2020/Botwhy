package com.kob.backend.service.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.common.api.R;
import com.kob.backend.entity.User;
import com.kob.backend.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author August
 * @create 2022-09-16 14:06
 */
@Service
@AllArgsConstructor
public class RankListService {
    private final UserMapper userMapper;

    public R page(Integer page) {
        IPage<User> userPage = new Page<>(page, 10);
        QueryWrapper<User> query = new QueryWrapper<>();
        query.ne("id", 4);
        query.orderByDesc("rating");
        List<User> users = userMapper.selectPage(userPage, query).getRecords();
        JSONObject resp = new JSONObject();
        for (User user : users) {
            user.setPassword("");
        }
        resp.put("users", users);
        resp.put("usersCount", userMapper.selectCount(null));
        return R.data(resp);
    }
}
