package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

/**
 * @author August
 * @create 2022-09-08 13:36
 */
@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool MATCHING_POOL = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId, String game) {
        System.out.println("add player: " + userId + " " + rating + " " + game);
        MATCHING_POOL.addPlayer(userId, rating, botId, game);
        System.out.println(MATCHING_POOL);
        return null;
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player: " + userId);
        MATCHING_POOL.removePlayer(userId);
        return null;
    }
}
