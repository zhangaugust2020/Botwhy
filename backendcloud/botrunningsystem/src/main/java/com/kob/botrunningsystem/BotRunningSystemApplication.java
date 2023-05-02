package com.kob.botrunningsystem;

import com.kob.botrunningsystem.service.impl.BotRunningServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author August
 * @create 2022-09-13 20:18
 */
@SpringBootApplication
public class BotRunningSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotRunningSystemApplication.class, args);
        BotRunningServiceImpl.BOT_POOL.start();
    }
}
