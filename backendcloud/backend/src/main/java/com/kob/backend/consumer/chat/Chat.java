package com.kob.backend.consumer.chat;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.websocket.ReversiWebSocketServer;
import com.kob.backend.consumer.websocket.SnakeWebSocketServer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author August
 * @create 2022-09-19 12:36
 */
@Slf4j
public class Chat extends Thread {
    private final ReentrantLock lock1 = new ReentrantLock();
    private final ReentrantLock lock2 = new ReentrantLock();
    private final Person A;
    private final Person B;
    private final String game;

    public Chat(Integer aId, Integer bId, String game) {
        A = new Person(aId, null, 0);
        B = new Person(bId, null, 0);
        this.game = game;
    }

    public Person getPersonA() {
        return this.A;
    }

    public Person getPersonB() {
        return this.B;
    }


    public void setAready(Integer ready) {
        lock2.lock();
        try {
            this.A.setReady(ready);
            sendReady(ready, B.getId());
        } finally {
            lock2.unlock();
        }
    }

    public void setBready(Integer ready) {
        lock2.lock();
        try {
            this.B.setReady(ready);
            sendReady(ready, A.getId());
        } finally {
            lock2.unlock();
        }
    }

    public void setAcontent(String content) {
        lock1.lock();
        try {
            this.A.setContent(content);
        } finally {
            lock1.unlock();
        }
    }

    public void setBcontent(String content) {
        lock1.lock();
        try {
            this.B.setContent(content);
        } finally {
            lock1.unlock();
        }
    }

    private boolean checkChat() {
        return !(CharSequenceUtil.isBlank(A.getContent()) && CharSequenceUtil.isBlank(B.getContent()));
    }

    private boolean checkReady() {
        return A.getReady() == 1 && B.getReady() == 1;
    }

    private boolean checkOnline() {
        boolean t = true;
        switch (game) {
            case "snake":
                if (BeanUtil.isEmpty(SnakeWebSocketServer.users.get(A.getId())) &&
                        BeanUtil.isEmpty(SnakeWebSocketServer.users.get(B.getId()))) {
                    t = false;
                } else if (BeanUtil.isEmpty(SnakeWebSocketServer.users.get(A.getId()))) {
                    SnakeWebSocketServer.users.get(B.getId()).removeFromMatchPool(B.getId());
                    t = false;
                } else if (BeanUtil.isEmpty(SnakeWebSocketServer.users.get(B.getId()))) {
                    SnakeWebSocketServer.users.get(A.getId()).removeFromMatchPool(A.getId());
                    t = false;
                }
                break;
            case "reversi":
                if (BeanUtil.isEmpty(ReversiWebSocketServer.users.get(A.getId())) &&
                        BeanUtil.isEmpty(ReversiWebSocketServer.users.get(B.getId()))) {
                    t = false;
                } else if (BeanUtil.isEmpty(ReversiWebSocketServer.users.get(A.getId()))) {
                    ReversiWebSocketServer.users.get(B.getId()).removeFromMatchPool(B.getId());
                    t = false;
                } else if (BeanUtil.isEmpty(ReversiWebSocketServer.users.get(B.getId()))) {
                    ReversiWebSocketServer.users.get(A.getId()).removeFromMatchPool(A.getId());
                    t = false;
                }
                break;
            default:
                break;
        }
        return t;
    }

    private void send(String content, Integer id) {
        log.info(content);
        if (CharSequenceUtil.isBlank(content)) {
            return;
        }
        JSONObject resp = new JSONObject();
        resp.put("event", "send-msg");
        resp.put("content", content);
        switch (game) {
            case "snake":
                if (SnakeWebSocketServer.users.get(id) != null) {
                    SnakeWebSocketServer.users.get(id).sendMessage(resp.toJSONString());
                }
                break;
            case "reversi":
                if (ReversiWebSocketServer.users.get(id) != null) {
                    ReversiWebSocketServer.users.get(id).sendMessage(resp.toJSONString());
                }
                break;
        }

    }

    private void sendReady(Integer ready, Integer id) {
        JSONObject resp = new JSONObject();
        resp.put("event", "send-ready");
        if (ready == 1) {
            resp.put("ready", "已准备");
        } else if (ready == 0) {
            resp.put("ready", "未准备");
        }
        switch (game) {
            case "snake":
                if (SnakeWebSocketServer.users.get(id) != null) {
                    SnakeWebSocketServer.users.get(id).sendMessage(resp.toJSONString());
                }
                break;
            case "reversi":
                if (ReversiWebSocketServer.users.get(id) != null) {
                    ReversiWebSocketServer.users.get(id).sendMessage(resp.toJSONString());
                }
                break;
        }

    }

    private void sendAllMessage(String message) {
        System.out.println(message);
        switch (game) {
            case "snake":
                if (SnakeWebSocketServer.users.get(A.getId()) != null) {
                    SnakeWebSocketServer.users.get(A.getId()).sendMessage(message);
                }
                if (SnakeWebSocketServer.users.get(B.getId()) != null) {
                    SnakeWebSocketServer.users.get(B.getId()).sendMessage(message);
                }
                break;
            case "reversi":
                if (ReversiWebSocketServer.users.get(A.getId()) != null) {
                    ReversiWebSocketServer.users.get(A.getId()).sendMessage(message);
                }
                if (ReversiWebSocketServer.users.get(B.getId()) != null) {
                    ReversiWebSocketServer.users.get(B.getId()).sendMessage(message);
                }
                break;
        }

    }

    private void respond_disconnect() {
        JSONObject resp = new JSONObject();
        resp.put("event", "chat-disconnect");
        sendAllMessage(resp.toJSONString());
    }

    private void start_game() {
        JSONObject resp = new JSONObject();
        resp.put("event", "chat-to-game");
        sendAllMessage(resp.toJSONString());
    }

    @Override
    public void run() {
        while (true) {
            if (!checkOnline()) {
                respond_disconnect();
                break;
            }
            if (checkChat()) {
                log.info("check chat");
                lock1.lock();
                try {
                    send(A.getContent(), B.getId());
                    send(B.getContent(), A.getId());
                    A.setContent(null);
                    B.setContent(null);
                } finally {
                    lock1.unlock();
                }
            }
            if (checkReady()) {
                lock2.lock();
                try {
                    start_game();
                    switch (game) {
                        case "snake":
                            SnakeWebSocketServer.startGame(A.getId(), null, B.getId(), null);
                            break;
                        case "reversi":
                            ReversiWebSocketServer.startGame(A.getId(), null, B.getId(), null);
                            break;
                    }
                } finally {
                    lock2.unlock();
                }
                break;
            }
        }
    }
}
