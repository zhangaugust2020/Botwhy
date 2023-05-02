package com.kob.backend.consumer.websocket;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.common.constants.KobConstants;
import com.kob.backend.consumer.chat.Chat;
import com.kob.backend.consumer.game.reversi.ReversiGame;
import com.kob.backend.consumer.utils.JwtAuthorization;
import com.kob.backend.entity.Bot;
import com.kob.backend.entity.User;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author August
 * @create 2022-09-29 15:06
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/reversi/{token}")
public class ReversiWebSocketServer {
    public static final ConcurrentHashMap<Integer, ReversiWebSocketServer> users = new ConcurrentHashMap<>();
    public static RecordMapper recordMapper;
    public static RestTemplate restTemplate;
    public static UserMapper userMapper;
    private static BotMapper botMapper;

    static {
        users.put(KobConstants.BOT_USER_ID, new ReversiWebSocketServer());
    }

    public ReversiGame game = null;
    private Chat chat = null;
    private User user;
    private Session session = null;

    public static void startChat(Integer aId, Integer bId) {
        System.out.println("start chat");
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);
        Chat chat = new Chat(aId, bId, "reversi");
        if (users.get(a.getId()) != null) {
            users.get(a.getId()).chat = chat;
        }
        if (users.get(b.getId()) != null) {
            users.get(b.getId()).chat = chat;
        }

        chat.start();
        JSONObject respA = new JSONObject();
        respA.put("event", "chating");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("opponent_rating", b.getRating());
        respA.put("self_rating", a.getRating());
        if (users.get(a.getId()) != null) {
            users.get(a.getId()).sendMessage(StrUtil.toString(respA));
        }

        JSONObject respB = new JSONObject();
        respB.put("event", "chating");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("opponent_rating", a.getRating());
        respB.put("self_rating", b.getRating());
        if (users.get(b.getId()) != null) {
            users.get(b.getId()).sendMessage(StrUtil.toString(respB));
        }
    }

    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId);
        Bot botB = botMapper.selectById(bBotId);
        ReversiGame game = new ReversiGame(a.getId(), botA, b.getId(), botB);
        game.createMap();
        if (users.get(a.getId()) != null) {
            users.get(a.getId()).game = game;
        }
        if (users.get(b.getId()) != null) {
            users.get(b.getId()).game = game;
        }

        game.start();

        ReversiWebSocketServer socketA = users.get(a.getId());
        ReversiWebSocketServer socketB = users.get(b.getId());
        JSONObject respGame = new JSONObject();
        respGame.put("map", game.getG());
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("a_round", game.getPlayerA().getIsRound());
        respGame.put("b_round", game.getPlayerB().getIsRound());

        JSONObject respA = new JSONObject();
        respA.put("event", "playing");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);
        if (socketA != null) {
            socketA.sendMessage(StrUtil.toString(respA));
        }

        JSONObject respB = new JSONObject();
        respB.put("event", "playing");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
        if (b.getId() != KobConstants.BOT_USER_ID && socketB != null) {
            socketB.sendMessage(StrUtil.toString(respB));
        }
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        ReversiWebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        ReversiWebSocketServer.botMapper = botMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        ReversiWebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        ReversiWebSocketServer.restTemplate = restTemplate;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        log.info("connected!");
        System.out.println("connected!");
        Integer userId = JwtAuthorization.getUserId(token);
        this.user = userMapper.selectById(userId);

        if (this.user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        log.info("disconnected!");
        System.out.println("disconnected!");
        if (this.user != null) {
            users.remove(this.user.getId());
            removeFromMatchPool(this.user.getId());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        JSONObject data = JSONObject.parseObject(message);
        log.info("成功接收信息 : {}", data);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatching(data.getInteger("bot_id"));
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        } else if ("put".equals(event)) {
            put(data.getInteger("x"), data.getInteger("y"));
        } else if ("chat".equals(event)) {
            System.out.println("receive chat");
            chat(data.getString("content"));
        } else if ("ready".equals(event)) {
            System.out.println("receive ready");
            ready(data.getInteger("status"));
        }
    }

    private void chat(String content) {
        if (chat.getPersonA().getId().equals(user.getId())) {
            chat.setAcontent(content);
        } else if (chat.getPersonB().getId().equals(user.getId())) {
            chat.setBcontent(content);
        }
    }

    private void ready(Integer ready) {
        if (chat.getPersonA().getId().equals(user.getId())) {
            chat.setAready(ready);
        } else if (chat.getPersonB().getId().equals(user.getId())) {
            chat.setBready(ready);
        }
    }

    private void startMatching(Integer botId) {
        log.info("start-matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.set("userId", StrUtil.toString(this.user.getId()));
        data.set("rating", StrUtil.toString(this.user.getRating()));
        data.set("botId", StrUtil.toString(botId));
        data.set("game", "reversi");
        restTemplate.postForObject(KobConstants.ADD_PLAYER_URL, data, String.class);
    }

    private void stopMatching() {
        log.info("stop-matching");
        removeFromMatchPool(this.user.getId());
    }

    public void removeFromMatchPool(Integer id) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.set("userId", StrUtil.toString(id));
        restTemplate.postForObject(KobConstants.REMOVE_PLAYER_URL, data, String.class);
    }

    private void put(int x, int y) {
        if (game.getPlayerA().getId().equals(user.getId())) {
            if (game.getPlayerA().getBotId().equals(-1)) { // 如果选择机器人, 屏蔽人的操作
                game.setNextStepA(x, y);
            }
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            if (game.getPlayerB().getBotId().equals(-1)) {
                game.setNextStepB(x, y);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
                System.out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
