package com.kob.backend.consumer.game.snake;

import com.kob.backend.consumer.game.Cell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author August
 * @create 2022-09-07 9:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnakePlayer {
    private Integer id;
    private Integer botId; // -1为人工
    private String language;
    private String botCode;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;
    private String container;

    private boolean check_tail_increasing(int steps) { // 检测当前回合蛇是否变长
        if (steps <= 10) {
            return true;
        }
        return steps % 3 == 1;
    }

    public List<Cell> getCells() {
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for (int d : steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!check_tail_increasing(++step)) {
                res.remove(0);
            }
        }
        return res;
    }

    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for (int d : steps) {
            res.append(d);
        }
        return res.toString();
    }
}
