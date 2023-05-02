package com.kob.backend.service.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.common.api.R;
import com.kob.backend.entity.Record;
import com.kob.backend.entity.User;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author August
 * @create 2022-09-15 16:13
 */
@Service
@AllArgsConstructor
public class RecordService {
    private final RecordMapper mapper;
    private final UserMapper userMapper;

    public R page(Integer page) {
        IPage<Record> recordIPage = new Page<>(page, 10);
        QueryWrapper<Record> query = new QueryWrapper();
        query.orderByDesc("id");
        List<Record> records = mapper.selectPage(recordIPage, query).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for (Record record : records) {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            JSONObject item = new JSONObject();
            item.put("a_photo", userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userB.getPhoto());
            item.put("b_username", userB.getUsername());
            item.put("record", record);

            String result = "平局";
            if ("A".equals(record.getLoser())) {
                result = userB.getUsername() + " 胜";
            } else if ("B".equals(record.getLoser())) {
                result = userA.getUsername() + " 胜";
            }
            item.put("result", result);
            items.add(item);
        }
        resp.put("records", items);
        resp.put("recordsCount", mapper.selectCount(null));
        return R.data(resp);
    }

    public R searchPage(String username, String game, Integer page) {
        IPage<Record> recordIPage = new Page<>(page, 10);
        QueryWrapper<Record> query = new QueryWrapper();
        if ("".equals(game)) {
            game = "%";
        }

        query.like("game", game);
        query.orderByDesc("id");
        List<Record> records = mapper.selectPage(recordIPage, query).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for (Record record : records) {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            if (!userA.getUsername().equals(username) && !userB.getUsername().equals(username) && !"".equals(username)) {
                continue;
            }
            JSONObject item = new JSONObject();
            item.put("a_photo", userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userB.getPhoto());
            item.put("b_username", userB.getUsername());
            item.put("record", record);

            String result = "平局";
            if ("A".equals(record.getLoser())) {
                result = userB.getUsername() + " 胜";
            } else if ("B".equals(record.getLoser())) {
                result = userA.getUsername() + " 胜";
            }
            item.put("result", result);
            items.add(item);
        }
        resp.put("records", items);
        resp.put("recordsCount", mapper.selectCount(null));
        return R.data(resp);
    }
}
