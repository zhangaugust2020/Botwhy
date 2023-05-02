package com.kob.botrunningsystem.controller;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.utils.Bot;
import lombok.AllArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author August
 * @create 2022-09-13 20:25
 */
@RestController
@AllArgsConstructor
public class BotRunningController {
    private final BotRunningService service;

    @PostMapping("/bot/add")
    public String add(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("userId")));
        String botCode = data.getFirst("botCode");
        String input = data.getFirst("input");
        String language = data.getFirst("language");
        String container = data.getFirst("container");
        String status = data.getFirst("status");
        String game = data.getFirst("game");

        Bot bot = Bot.builder()
                .userId(userId)
                .botCode(botCode)
                .input(input)
                .language(language)
                .container(container)
                .status(status)
                .game(game).build();
        return this.service.addBot(bot);
    }
}
