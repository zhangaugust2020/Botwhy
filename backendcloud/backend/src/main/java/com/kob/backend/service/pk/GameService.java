package com.kob.backend.service.pk;

import com.kob.backend.consumer.game.reversi.ReversiGame;
import com.kob.backend.consumer.game.snake.SnakeGame;
import com.kob.backend.consumer.websocket.ReversiWebSocketServer;
import com.kob.backend.consumer.websocket.SnakeWebSocketServer;
import org.springframework.stereotype.Service;

/**
 * @author August
 * @create 2022-09-08 15:37
 */
@Service
public class GameService {

    public String snakeStart(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        if (aBotId != -1 || bBotId != -1) {
            SnakeWebSocketServer.startGame(aId, aBotId, bId, bBotId);
            return "start game success";
        }
        SnakeWebSocketServer.startChat(aId, bId);
        return "start chat success";
    }

    public String reversiStart(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        if (aBotId != -1 || bBotId != -1) {
            ReversiWebSocketServer.startGame(aId, aBotId, bId, bBotId);
            return "start game success";
        }
        ReversiWebSocketServer.startChat(aId, bId);
        return "start chat success";
    }

    public String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        System.out.println("start game: " + aId + " " + bId);
        SnakeWebSocketServer.startGame(aId, aBotId, bId, bBotId);
        return "start game success";
    }


    public String receiveBotMove(Integer userId, Integer direction, String container) {
        System.out.println("receive bot: " + userId + " " + direction + " " + container);

        if (SnakeWebSocketServer.users.get(userId) != null) {
            SnakeGame game = SnakeWebSocketServer.users.get(userId).game;
            if (game != null) {
                if (game.getPlayerA().getId().equals(userId)) {
                    game.getPlayerA().setContainer(container);
                    game.setNextStepA(direction);
                } else if (game.getPlayerB().getId().equals(userId)) {
                    game.getPlayerB().setContainer(container);
                    game.setNextStepB(direction);
                }
            }
        }

        return "receive bot move success";
    }

    public String receiveBotPut(Integer userId, Integer x, Integer y, String container) {
        System.out.println("receive bot: " + userId + " " + x + " " + y + " " + container);

        if (ReversiWebSocketServer.users.get(userId) != null) {
            ReversiGame game = ReversiWebSocketServer.users.get(userId).game;
            if (game != null) {
                if (game.getPlayerA().getId().equals(userId)) {
                    game.getPlayerA().setContainer(container);
                    game.setNextStepA(x, y);
                } else if (game.getPlayerB().getId().equals(userId)) {
                    game.getPlayerB().setContainer(container);
                    game.setNextStepB(x, y);
                }
            }
        }

        return "receive bot put success";
    }
}
