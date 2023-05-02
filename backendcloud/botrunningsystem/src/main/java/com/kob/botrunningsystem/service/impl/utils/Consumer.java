package com.kob.botrunningsystem.service.impl.utils;

import com.kob.botrunningsystem.api.RunningClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author August
 * @create 2022-09-14 9:32
 */
@Component
public class Consumer extends Thread {

    private static RestTemplate restTemplate;
    private static RunningClient client;
    private Bot bot;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    @Autowired
    public void setRunningClient(RunningClient client) {
        Consumer.client = client;
    }

    public void startTimeout(long timeout, Bot bot) {
        this.bot = bot;
        this.start();

        /**
         * 1. 线程执行完毕
         * 2. timeout秒后
         *  继续执行join后面的语句
         */
        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt(); // 中断当前线程
        }
    }

    private StringBuilder get_random() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            res.append((char) (Math.random() * 10 + '0'));
        }
        return res;
    }


    @Override
    public void run() {
        StringBuilder random = get_random();
        File file = new File(bot.getGame() + random + "input.txt");
        File code = new File(bot.getGame() + random + "code.txt");
        try (PrintWriter fout = new PrintWriter(file)) {
            fout.println(bot.getInput());
            fout.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (PrintWriter fout = new PrintWriter(code)) {
            fout.println(bot.getBotCode());
            fout.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.set("language", bot.getLanguage());
        data.set("userId", bot.getUserId().toString());
        data.set("status", bot.getStatus());
        data.set("container", bot.getContainer());
        data.set("game", bot.getGame());
        data.set("random", random.toString());
        Map<String, String> map = client.run(data);
    }

}
