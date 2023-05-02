package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.GameService;
import lombok.AllArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

/**
 * @author August
 * @create 2022-08-28 21:06
 */

@RestController
@RequestMapping("/pk/game")
@AllArgsConstructor
public class GameController {
    private final GameService service;

    @PostMapping("/snake/start")
    public String snakeStart(@RequestParam MultiValueMap<String, String> data) {
        System.out.println("startGame : " + data);
        Integer aId = Integer.parseInt(Objects.requireNonNull(data.getFirst("aId")));
        Integer aBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("aBotId")));
        Integer bId = Integer.parseInt(Objects.requireNonNull(data.getFirst("bId")));
        Integer bBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("bBotId")));
        return service.snakeStart(aId, aBotId, bId, bBotId);
    }

    @PostMapping("/reversi/start")
    public String reversiStart(@RequestParam MultiValueMap<String, String> data) {
        System.out.println("startGame : " + data);
        Integer aId = Integer.parseInt(Objects.requireNonNull(data.getFirst("aId")));
        Integer aBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("aBotId")));
        Integer bId = Integer.parseInt(Objects.requireNonNull(data.getFirst("bId")));
        Integer bBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("bBotId")));
        return service.reversiStart(aId, aBotId, bId, bBotId);
    }

    @PostMapping("/snake/receiveBot")
    public String receiveBotMove(@RequestParam Map<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.get("userId")));
        Integer direction = Integer.parseInt(Objects.requireNonNull(data.get("direction")));
        String container = Objects.requireNonNull(data.get("container"));
        return service.receiveBotMove(userId, direction, container);
    }

    @PostMapping("/reversi/receiveBot")
    public String receiveBotPut(@RequestParam Map<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.get("userId")));
        Integer x = Integer.parseInt(Objects.requireNonNull(data.get("x")));
        Integer y = Integer.parseInt(Objects.requireNonNull(data.get("y")));
        String container = Objects.requireNonNull(data.get("container"));
        return service.receiveBotPut(userId, x, y, container);
    }
}
