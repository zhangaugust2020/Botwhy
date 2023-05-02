package com.kob.matchingsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author August
 * @create 2022-09-08 14:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer userId;
    private Integer rating;
    private Integer botId;
    private String game;
    private Integer waitingTime;

    Player(Integer userId, Integer botId) {
        this.userId = userId;
        this.botId = botId;
    }
}
