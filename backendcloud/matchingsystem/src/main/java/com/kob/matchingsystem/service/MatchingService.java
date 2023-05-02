package com.kob.matchingsystem.service;

/**
 * @author August
 * @create 2022-09-08 13:35
 */
public interface MatchingService {
    String addPlayer(Integer userId, Integer rating, Integer botId, String game);

    String removePlayer(Integer userId);
}
