package com.kob.botrunningsystem.service.impl;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.utils.Bot;
import com.kob.botrunningsystem.service.impl.utils.BotPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author August
 * @create 2022-09-13 20:23
 */
@Service
@Slf4j
public class BotRunningServiceImpl implements BotRunningService {
    public final static BotPool BOT_POOL = new BotPool();

    @Override
    public String addBot(Bot bot) {
        log.info("userid : {}, game : {}, language : {}, status : {}, container : {}",
                bot.getUserId(), bot.getGame(), bot.getLanguage(), bot.getStatus(), bot.getContainer());
        BOT_POOL.addBot(bot);
        return "success";
    }
}
