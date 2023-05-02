package com.kob.backend.consumer.game.snake;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.common.api.RunningClient;
import com.kob.backend.common.constants.KobConstants;
import com.kob.backend.consumer.game.Cell;
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
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author August
 * @create 2022-09-06 20:18
 */
@Component
@NoArgsConstructor
public class SnakeGame extends Thread {
    private final static int[] dx = {-1, 0, 1, 0};
    private final static int[] dy = {0, 1, 0, -1};
    private static RunningClient client;
    private final ReentrantLock lock = new ReentrantLock();
    private Integer rows;
    private Integer cols;
    private Integer innerWallsCount;
    private int[][] g;
    private SnakePlayer playerA, playerB;
    private String status = "playing"; // playing -> finished
    private String loser = ""; // all 平局, A : A输, B : B输
    private Integer nextStepA = null;
    private Integer nextStepB = null;

    public SnakeGame(Integer rows, Integer cols, Integer innerWallsCount, Integer idA, Bot botA, Integer idB, Bot botB) {
        this.rows = rows;
        this.cols = cols;
        this.innerWallsCount = innerWallsCount;
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
        this.playerA = new SnakePlayer(idA, botIdA, languageA, botCodeA, this.rows - 2, 1, new ArrayList<>(), null);
        this.playerB = new SnakePlayer(idB, botIdB, languageB, botCodeB, 1, this.cols - 2, new ArrayList<>(), null);
    }

    @Autowired
    private void setRunningClient(RunningClient client) {
        SnakeGame.client = client;
    }

    public int[][] getG() {
        return g;
    }

    public SnakePlayer getPlayerA() {
        return this.playerA;
    }

    public SnakePlayer getPlayerB() {
        return this.playerB;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        System.out.println("a : " + nextStepA);
        try {
            if (check_valid_step(nextStepA)) {
                status = "finished";
                loser = "A";
                sendResult();
            }
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        System.out.println("b : " + nextStepB);
        try {
            if (check_valid_step(nextStepB)) {
                status = "finished";
                loser = "B";
                sendResult();
            }
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    private boolean check_valid_step(int d) {
        return !(d >= 0 && d <= 3);
    }

    private boolean draw() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g[i][j] = 0;
            }
        }

        // 四周加上障碍物
        for (int r = 0; r < this.rows; r++) {
            g[r][0] = 1;
            g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c++) {
            g[0][c] = 1;
            g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        // 创建随机障碍物
        for (int i = 0; i < this.innerWallsCount / 2; i++) {
            // 找到一个空格
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) {
                    continue;
                }
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) {
                    continue;
                }
                g[r][c] = 1;
                g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }
        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) {
            return true;
        }
        g[sx][sy] = 1;
        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (this.check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }
        g[sx][sy] = 0;
        return false;
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (this.draw()) {
                break;
            }
        }
    }

    // 将当前局面信息编码成字符串
    private String getInput(SnakePlayer player) {
        SnakePlayer me, you;
        if (playerA.getId().equals(player.getId())) {
            me = playerA;
            you = playerB;
        } else {
            me = playerB;
            you = playerA;
        }
        return getMapString() + "#" + me.getSx() + "#" + me.getSy() + "#(" + me.getStepsString()
                + ")#" + you.getSx() + "#" + you.getSy() + "#(" + you.getStepsString() + ")";
    }

    private void sendBotCode(SnakePlayer player) {
        if (player.getBotId().equals(-1)) { // 亲自出马
            return;
        }
        // 机器人: 发送代码到BotRunningSystem服务
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.set("userId", StrUtil.toString(player.getId()));
        data.set("botCode", StrUtil.toString(player.getBotCode()));
        data.set("input", getInput(player));
        data.set("language", StrUtil.toString(player.getLanguage()));
        data.set("game", "snake");
        if (player.getContainer() == null) {
            data.set("status", "0");
        } else {
            data.set("status", "1");
        }
        data.set("container", player.getContainer());
        SnakeWebSocketServer.restTemplate.postForObject(KobConstants.ADD_BOT_URL, data, String.class);
    }

    private void sendBotEnd(SnakePlayer player) {
        if (player.getBotId().equals(-1)) { // 亲自出马
            return;
        }
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.set("status", "2");
        data.set("container", player.getContainer());
        data.set("game", "snake");
        client.end(data);
    }

    private boolean nextStep() { // 等待两名玩家下一步操作

        // 前端画完一个格子最少需要200ms, 所以先暂停300ms等前端画完格子, 再去判断
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        for (int i = 0; i < 50; i++) { // 五秒时间输入信息
            try {
                Thread.sleep(100); // 100ms去判断一次是否输入信息
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
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

    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        // 撞墙
        if (g[cell.x][cell.y] == 1) {
            return false;
        }
        // 撞自己
        for (int i = 0; i < n - 1; i++) {
            if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y) {
                return false;
            }
        }
        // 撞对面
        for (int i = 0; i < n - 1; i++) {
            if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y) {
                return false;
            }
        }
        return true;
    }

    private void judge() { // 判断两名client下一步操作是否合法
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);
        if (!validA || !validB) {
            status = "finished";
            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }

    private void sendAllMessage(String message) { // 向每个人广播信息
        System.out.println(message);
        if (SnakeWebSocketServer.users.get(playerA.getId()) != null) {
            SnakeWebSocketServer.users.get(playerA.getId()).sendMessage(message);
        }
        if (playerB.getId() != KobConstants.BOT_USER_ID && SnakeWebSocketServer.users.get(playerB.getId()) != null) {
            SnakeWebSocketServer.users.get(playerB.getId()).sendMessage(message);
        }
    }

    @Transactional
    public void updateUserRating(SnakePlayer playerA, SnakePlayer playerB) {
        User userA = SnakeWebSocketServer.userMapper.selectById(playerA.getId());
        User userB = SnakeWebSocketServer.userMapper.selectById(playerB.getId());
        if ("A".equals(loser)) {
            userA.setRating(userA.getRating() - 2);
            userB.setRating(userB.getRating() + 5);
        } else if ("B".equals(loser)) {
            userA.setRating(userA.getRating() + 5);
            userB.setRating(userB.getRating() - 2);
        }
        SnakeWebSocketServer.userMapper.updateById(userA);
        SnakeWebSocketServer.userMapper.updateById(userB);
    }

    private void saveToData() {
        if (!"all".equals(loser)) {
            updateUserRating(playerA, playerB);
        }
        Record record = new Record(null, "snake", playerA.getId(),
                playerA.getSx(), playerA.getSy(),
                playerB.getId(), playerB.getSx(), playerB.getSy(),
                playerA.getStepsString(), playerB.getStepsString(),
                getMapString(), loser, DateUtil.date());

        SnakeWebSocketServer.recordMapper.insert(record);
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

    private void sendMove() { // 向两个client传递信息
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
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

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            if (nextStep()) { // 是否获取两条蛇下一操作
                judge();
                if (status.equals("playing")) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
