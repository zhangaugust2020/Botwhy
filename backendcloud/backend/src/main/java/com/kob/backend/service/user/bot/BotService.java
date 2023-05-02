package com.kob.backend.service.user.bot;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.common.api.R;
import com.kob.backend.common.api.RunningClient;
import com.kob.backend.entity.Bot;
import com.kob.backend.entity.User;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.utils.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author August
 * @create 2022-08-31 14:45
 */

@Service
@AllArgsConstructor
@Transactional
public class BotService {
    private final BotMapper mapper;
    private final RunningClient client;

    private Map<String, String> compile(Bot bot) {
        Map<String, String> data = new HashMap<>();
        data.put("language", bot.getLanguage());
        data.put("code", bot.getContent());
        data.put("game", bot.getGame());
        return client.compile(data);
    }

    public R add(Bot bot) {
        User user = UserUtil.getUser();
        QueryWrapper<Bot> query = new QueryWrapper<>();
        query.eq("user_id", user.getId());
        Long botCount = mapper.selectCount(query);
        if (botCount >= 10) {
            return R.fail("每个用户最多只能创建10个Bot~");
        }
        Map<String, String> res = compile(bot);
        if (!"ok".equals(res.get("result"))) {
            return R.fail(res.get("msg") + " " + res.get("output"));
        }
        Bot newBot = Bot.builder().userId(user.getId())
                .title(bot.getTitle())
                .description(bot.getDescription().isEmpty() ? "这个用户很懒, 什么都没有留下~" : bot.getDescription())
                .content(bot.getContent())
                .language(bot.getLanguage())
                .game(bot.getGame())
                .createtime(DateUtil.date())
                .updatetime(DateUtil.date())
                .build();
        return R.data(mapper.insert(newBot));
    }

    public R update(Bot bot) {
        User user = UserUtil.getUser();
        QueryWrapper<Bot> query = new QueryWrapper<>();
        query.eq("id", bot.getId()).eq("user_id", user.getId());
        Bot bot1 = mapper.selectOne(query);
        if (bot1 == null) {
            return R.fail("当前Bot不存在或没有权限修改该Bot");
        }
        Map<String, String> res = compile(bot);

        if (!"ok".equals(res.get("result"))) {
            return R.fail(res.get("msg"));
        }
        bot.setUpdatetime(DateUtil.date());
        return R.data(mapper.update(bot, query));
    }

    public R remove(Integer id) {
        User user = UserUtil.getUser();
        QueryWrapper<Bot> query = new QueryWrapper<>();
        query.eq("id", id).eq("user_id", user.getId());
        return R.data(mapper.delete(query));
    }

    public R botList(String game) {
        User user = UserUtil.getUser();
        if ("all".equals(game) || "".equals(game)) {
            game = "%";
        }
        QueryWrapper<Bot> query = new QueryWrapper<>();
        query.eq("user_id", user.getId()).like("game", game).orderByDesc("createtime");
        return R.data(mapper.selectList(query));
    }

    public R count(Integer id) {
        QueryWrapper query = new QueryWrapper();
        query.eq("user_id", id);
        return R.data(mapper.selectCount(query));
    }


}

