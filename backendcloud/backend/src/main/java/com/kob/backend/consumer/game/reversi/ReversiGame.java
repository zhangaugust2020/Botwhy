package com.kob.backend.consumer.game.reversi;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.common.api.RunningClient;
import com.kob.backend.common.constants.KobConstants;
import com.kob.backend.consumer.game.Cell;
import com.kob.backend.consumer.websocket.ReversiWebSocketServer;
import com.kob.backend.consumer.websocket.SnakeWebSocketServer;
import com.kob.backend.entity.Bot;
import com.kob.backend.entity.Record;
import com.kob.backend.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author August
 * @create 2022-09-29 15:09
 */
@Component
@NoArgsConstructor
public class ReversiGame extends Thread {
    private final static int[] dx = {0, 1, 0, -1, 1, 1, -1, -1};
    private final static int[] dy = {1, 0, -1, 0, -1, 1, 1, -1};
    private final static Integer rows = 8, cols = 8;
    private static RunningClient client;
    private final ReentrantLock lock = new ReentrantLock();
    private int[][] g;
    private ReversiPlayer playerA, playerB;
    private String status = "playing"; // playing -> finished
    private String loser = ""; // all 平局, A : A输, B : B输
    private Cell nextStepA = null;
    private Cell nextStepB = null;

    public ReversiGame(Integer idA, Bot botA, Integer idB, Bot botB) {
        this.g = new int[rows][cols];
        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
        String languageA = "", languageB = "";
        if (botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
            languageA = botA.getLanguage();
        }
        if (botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
            languageB = botB.getLanguage();
        }
        ArrayList<Cell> cellsA = new ArrayList<>();
        ArrayList<Cell> cellsB = new ArrayList<>();
        cellsA.add(new Cell(3, 3));
        cellsA.add(new Cell(4, 4));
        cellsB.add(new Cell(3, 4));
        cellsB.add(new Cell(4, 3));
        this.playerA = new ReversiPlayer(idA, botIdA, languageA, botCodeA, "black", true, cellsA, new ArrayList<>(), null, 0);
        this.playerB = new ReversiPlayer(idB, botIdB, languageB, botCodeB, "white", false, cellsB, new ArrayList<>(), null, 0);
    }

    @Autowired
    private void setRunningClient(RunningClient client) {
        ReversiGame.client = client;
    }

    public int[][] getG() {
        return g;
    }

    public ReversiPlayer getPlayerA() {
        return this.playerA;
    }

    public ReversiPlayer getPlayerB() {
        return this.playerB;
    }

    public void setNextStepA(int x, int y) {
        lock.lock();
        try {
            if (check_valid_step(x, y)) {
                status = "finished";
                loser = "A";
                sendResult();
            }
            this.nextStepA = new Cell(x, y);
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(int x, int y) {
        lock.lock();
        try {
            if (check_valid_step(x, y)) {
                status = "finished";
                loser = "B";
                sendResult();
            }
            this.nextStepB = new Cell(x, y);
        } finally {
            lock.unlock();
        }
    }

    private boolean check_valid_step(int x, int y) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            return false;
        }
        return true;
    }

    public void createMap() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                g[r][c] = 0;
            }
        }
        g[3][3] = 1;
        g[4][4] = 1;
        g[3][4] = 2;
        g[4][3] = 2;
    }

    private boolean check_valid(int x, int y, int id) {
        boolean f = false;
        if (g[x][y] != 0) {
            return false;
        }
        for (int i = 0; i < 8; i++) {
            if (this.check_reverse(x, y, x, y, 0, 0, i, id, 1)) {
                f = true;
            }
        }
        return f;
    }

    private boolean no_select(int id) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                for (int k = 0; k < 8; k++) {
                    if (g[i][j] == 0 && this.check_reverse(i, j, i, j, 0, 0, k, id, 1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * x, y用来记录初始位置, ex,ey记录偏移位置,
     * w用来标记是否遇到白子, b标记是否遇到黑子, d往哪个方向搜, id代表黑白子
     * s代表是否进行翻转
     */
    private boolean check_reverse(int x, int y, int ex, int ey, int w, int b, int d, int id, int s) {
        int tx = ex + dx[d], ty = ey + dy[d];
        if (tx < 0 || ty < 0 || tx >= 8 || ty >= 8) {
            return false;
        }
        // 黑子
        if (id == 1) {
            if (this.g[tx][ty] == 1 && w == 1) {
                while ((x != tx || y != ty) && s == 0) {
                    x += dx[d];
                    y += dy[d];
                    if (x == tx && y == ty) {
                        break;
                    }
                    playerB.remove(x, y);
                    this.g[x][y] = id;
                    playerA.set_put(x, y);
                }
                return true;
            }

            if (this.g[tx][ty] == 2) {
                return this.check_reverse(x, y, tx, ty, 1, 0, d, id, s);
            }
        } else { // 白子
            if (this.g[tx][ty] == 2 && b == 1) {
                while ((x != tx || y != ty) && s == 0) {
                    x += dx[d];
                    y += dy[d];
                    if (x == tx && y == ty) {
                        break;
                    }
                    playerA.remove(x, y);
                    this.g[x][y] = id;
                    playerB.set_put(x, y);
                }
                return true;
            }
            if (this.g[tx][ty] == 1) {
                return this.check_reverse(x, y, tx, ty, 0, 1, d, id, s);
            }
        }
        return false;
    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private void sendBotEnd(ReversiPlayer player) {
        if (player.getBotId().equals(-1)) { // 亲自出马
            return;
        }
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.set("status", "2");
        data.set("container", player.getContainer());
        data.set("game", "reversi");
        client.end(data);
    }

    private void sendBotCode(ReversiPlayer player) {
        if (player.getBotId().equals(-1)) { // 亲自出马
            return;
        }
        // 机器人: 发送代码到BotRunningSystem服务
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.set("userId", StrUtil.toString(player.getId()));
        data.set("botCode", StrUtil.toString(player.getBotCode()));
        data.set("input", getInput(player));
        data.set("language", StrUtil.toString(player.getLanguage()));
        data.set("game", "reversi");
        if (player.getContainer() == null) {
            data.set("status", "0");
        } else {
            data.set("status", "1");
        }
        data.set("container", player.getContainer());
        SnakeWebSocketServer.restTemplate.postForObject(KobConstants.ADD_BOT_URL, data, String.class);
    }

    // 将当前局面信息编码成字符串
    private String getInput(ReversiPlayer player) {
        ReversiPlayer me, you;
        if (playerA.getId().equals(player.getId())) {
            me = playerA;
            you = playerB;
        } else {
            me = playerB;
            you = playerA;
        }
        return getMapString() + " " + me.getColor();
    }

    private boolean nextStep() {
        // 减缓服务器压力~
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        check_change();
        if (playerA.getIsRound()) {
            sendBotCode(playerA);
        }

        if (playerB.getIsRound()) {
            sendBotCode(playerB);
        }

        for (int i = 0; i < 150; i++) { // 15s下棋
            // 是否改变下棋人
            check_change();
            try {
                Thread.sleep(200);
                lock.lock();
                try {
                    if (nextStepA != null && playerA.getIsRound()) {
                        return true;
                    } else if (nextStepB != null && playerB.getIsRound()) {
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void send(String content, String event, Integer id) {
        System.out.println(content);
        if (StrUtil.isBlank(content)) {
            return;
        }
        JSONObject resp = new JSONObject();
        resp.put("event", event);
        resp.put("content", content);
        if (id != KobConstants.BOT_USER_ID && ReversiWebSocketServer.users.get(id) != null) {
            ReversiWebSocketServer.users.get(id).sendMessage(resp.toJSONString());
        }
    }

    private boolean judge_put() {
        if (playerA.getIsRound() && !check_valid(nextStepA.getX(), nextStepA.getY(), 1)) {
            playerA.setValidCount(playerA.getValidCount() + 1);
            send("操作违法", "notice", playerA.getId());
            lock.lock();
            try {
                nextStepA = null;
            } finally {
                lock.unlock();
            }
            return false;
        }
        if (playerB.getIsRound() && !check_valid(nextStepB.getX(), nextStepB.getY(), 2)) {
            playerB.setValidCount(playerB.getValidCount() + 1);
            send("操作违法", "notice", playerB.getId());
            lock.lock();
            try {
                nextStepB = null;
            } finally {
                lock.unlock();
            }
            return false;
        }
        return true;
    }

    private int[] calculate() {
        int[] ans = new int[2];
        int white_cnt = 0, black_cnt = 0;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (this.g[i][j] == 1) {
                    black_cnt++;
                } else if (this.g[i][j] == 2) {
                    white_cnt++;
                }
            }
        }
        ans[0] = black_cnt;
        ans[1] = white_cnt;
        return ans;
    }

    private boolean judge_end() {
        boolean is_black_end = no_select(1);
        boolean is_white_end = no_select(2);
        Integer validCountA = playerA.getValidCount();
        Integer validCountB = playerB.getValidCount();
        Console.log(is_black_end, is_white_end);
        if (is_black_end && is_white_end) {
            status = "finished";
            int ans[] = calculate();
            if (ans[0] == ans[1]) {
                loser = "all";
            } else if (ans[0] < ans[1]) {
                loser = "A";
            } else if (ans[0] > ans[1]) {
                loser = "B";
            }
            return true;
        }

        if (validCountA >= 60) {
            status = "finished";
            loser = "A";
            return true;
        } else if (validCountB >= 60) {
            status = "finished";
            loser = "B";
            return true;
        }

        return false;
    }

    private void check_change() {
        boolean is_black_end = no_select(1);
        boolean is_white_end = no_select(2);
        JSONObject resp = new JSONObject();
        resp.put("event", "change");
        if (playerA.getIsRound() && is_black_end) {
            sendAllMessage(resp.toJSONString());
            playerA.setIsRound(false);
            playerB.setIsRound(true);
            sendBotCode(playerB);
        } else if (playerB.getIsRound() && is_white_end) {
            sendAllMessage(resp.toJSONString());
            playerB.setIsRound(false);
            playerA.setIsRound(true);
            sendBotCode(playerA);
        }
    }

    private void put(int x, int y, int id) {
        // 改变地图
        this.g[x][y] = id;

        // 放置下的'子'
        if (playerA.getIsRound()) {
            playerA.set_put(x, y);
            playerA.getSteps().add(nextStepA);
        } else if (playerB.getIsRound()) {
            playerB.set_put(x, y);
            playerB.getSteps().add(nextStepB);
        }

        // 翻转
        for (int k = 0; k < 8; k++) {
            this.check_reverse(x, y, x, y, 0, 0, k, id, 0);
        }
    }

    private void sendResult() { // 向两名client公布结果
        JSONObject resp = new JSONObject();
        sendBotEnd(playerA);
        sendBotEnd(playerB);
        playerA.setContainer(null);
        playerB.setContainer(null);
        resp.put("event", "result");
        resp.put("loser", loser);
        sendAllMessage(resp.toJSONString());
        saveToData();
    }

    private void saveToData() {
        if (!"all".equals(loser)) {
            updateUserRating(playerA, playerB);
        }
        Record record = new Record(null, "reversi", playerA.getId(),
                -1, -1,
                playerB.getId(), -1, -1,
                playerA.getStepsString(), playerB.getStepsString(),
                "", loser, DateUtil.date());

        ReversiWebSocketServer.recordMapper.insert(record);
    }

    @Transactional
    public void updateUserRating(ReversiPlayer playerA, ReversiPlayer playerB) {
        User userA = ReversiWebSocketServer.userMapper.selectById(playerA.getId());
        User userB = ReversiWebSocketServer.userMapper.selectById(playerB.getId());
        if ("A".equals(loser)) {
            userA.setRating(userA.getRating() - 2);
            userB.setRating(userB.getRating() + 5);
        } else if ("B".equals(loser)) {
            userA.setRating(userA.getRating() + 5);
            userB.setRating(userB.getRating() - 2);
        }
        ReversiWebSocketServer.userMapper.updateById(userA);
        ReversiWebSocketServer.userMapper.updateById(userB);
    }

    private void send_put() {
        lock.lock();
        try {
            int[] cnt = calculate();
            JSONObject resp = new JSONObject();
            resp.put("event", "put");
            playerA.setIsRound(!playerA.getIsRound());
            playerB.setIsRound(!playerB.getIsRound());
            resp.put("cnt", cnt);
            resp.put("cellsA", playerA.getSelfString());
            resp.put("cellsB", playerB.getSelfString());
            resp.put("map", g);
            sendAllMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    private void sendAllMessage(String message) { // 向每个人广播信息
        if (ReversiWebSocketServer.users.get(playerA.getId()) != null) {
            ReversiWebSocketServer.users.get(playerA.getId()).sendMessage(message);
        }
        if (playerB.getId() != KobConstants.BOT_USER_ID && ReversiWebSocketServer.users.get(playerB.getId()) != null) {
            ReversiWebSocketServer.users.get(playerB.getId()).sendMessage(message);
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {

            if (judge_end()) {
                sendResult();
                break;
            }
            // 获取下一步操作
            if (nextStep()) {
                if (!judge_put()) {
                    continue;
                }
                if (playerA.getIsRound()) {
                    playerA.setValidCount(0);
                    put(nextStepA.getX(), nextStepA.getY(), 1);
                } else if (playerB.getIsRound()) {
                    playerB.setValidCount(0);
                    put(nextStepB.getX(), nextStepB.getY(), 2);
                }
                send_put();
            } else { // 如果没有得到下一步操作, 则下棋的一方盘判输
                status = "finished";
                if (playerA.getIsRound()) {
                    loser = "A";
                } else {
                    loser = "B";
                }
                sendResult();
                break;
            }
        }
    }
}
