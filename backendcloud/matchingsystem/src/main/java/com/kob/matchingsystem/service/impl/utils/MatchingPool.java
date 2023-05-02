package com.kob.matchingsystem.service.impl.utils;

import com.kob.matchingsystem.common.constants.MatchConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author August
 * @create 2022-09-08 14:46
 */
@Component
public class MatchingPool extends Thread {

    private static List<Player> players = new ArrayList<>();
    private static RestTemplate restTemplate;
    private ReentrantLock lock = new ReentrantLock();

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer userId, Integer rating, Integer botId, String game) {
        lock.lock();
        try {
            players.add(new Player(userId, rating, botId, game, 0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId) {
        lock.lock();
        try {
            List<Player> newPlayers = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getUserId() != userId) {
                    newPlayers.add(players.get(i));
                }
            }
            players = newPlayers;
        } finally {
            lock.unlock();
        }
    }

    private void increaseWaitingTime() {
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    /**
     * 判断是否匹配
     * 匹配策略: 分差 <= 等待时间*10, 等待时间 : 两人间等待时间较少的
     */
    private boolean checkMatched(Player a, Player b) {
        if (a.getUserId().equals(b.getUserId())) {
            return false;
        }
        if (!a.getGame().equals(b.getGame())) {
            return false;
        }
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }

    private void matchPlayers() { // 匹配所有玩家
        boolean[] used = new boolean[players.size()];
        int idx = -1;
        for (int i = 0; i < players.size(); i++) {
            if (used[i]) {
                continue;
            }
            for (int j = i + 1; j < players.size(); j++) {
                if (used[j]) {
                    continue;
                }
                Player a = players.get(i), b = players.get(j);
                if (checkMatched(a, b)) {
                    used[i] = used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }

        idx = dispatchBot("snake");
        if (idx != -1 && players.get(idx).getWaitingTime() >= MatchConstant.MAX_WAITING_TIME) {
            used[idx] = true;
            sendResult(players.get(idx), new Player(MatchConstant.AUTO_USERID, MatchConstant.AUTO_SNAKE_BOTID));
        }

        idx = dispatchBot("reversi");
        if (idx != -1 && players.get(idx).getWaitingTime() >= MatchConstant.MAX_WAITING_TIME) {
            used[idx] = true;
            sendResult(players.get(idx), new Player(MatchConstant.AUTO_USERID, MatchConstant.AUTO_REVERSI_BOTID));
        }

        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (!used[i]) {
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }

    private int dispatchBot(String game) {
        int res = -1, cnt = 0;
        for (int i = 0; i < players.size(); i++) {
            if (game.equals(players.get(i).getGame())) {
                cnt++;
                res = i;
            }
        }
        if (cnt == 1) {
            return res;
        } else {
            return -1;
        }
    }

    private void sendResult(Player a, Player b) { // 返回匹配结果
        System.out.println("send result: " + a + " " + b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.set("aId", a.getUserId().toString());
        data.set("bId", b.getUserId().toString());
        data.set("aBotId", a.getBotId().toString());
        data.set("bBotId", b.getBotId().toString());
        switch (a.getGame()) {
            case "snake":
                restTemplate.postForObject(MatchConstant.START_SNAKEGAME_URL, data, String.class);
                break;
            case "reversi":
                restTemplate.postForObject(MatchConstant.START_REVERSIGAME_URL, data, String.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
