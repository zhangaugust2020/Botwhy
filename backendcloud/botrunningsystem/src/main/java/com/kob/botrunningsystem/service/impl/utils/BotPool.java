package com.kob.botrunningsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author August
 * @create 2022-09-14 9:05
 */

public class BotPool extends Thread {
    private final static String receiveBotMoveUrl = "http://127.0.0.1:10300/pk/game/receiveBotMove";
    private static RestTemplate restTemplate;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    /**
     * 该线程处理bots队列, nextStep插入Bot, 需要用锁
     */
    private final Queue<Bot> bots = new LinkedList<>();


    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        BotPool.restTemplate = restTemplate;
    }

    public void addBot(Bot bot) {
        lock.lock();

        try {
            bots.add(bot);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private void consume(Bot bot) {
        Consumer consumer = new Consumer();
        consumer.startTimeout(4000, bot);
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            if (bots.isEmpty()) {
                try {
                    condition.await(); // 自动释放锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            } else {
                Bot bot = bots.remove();
                lock.unlock();
                consume(bot); // 比较耗时, 可能执行几秒钟 -- 编译执行代码
            }
        }
    }
}
