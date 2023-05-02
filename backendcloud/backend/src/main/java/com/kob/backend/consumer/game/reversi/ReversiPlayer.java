package com.kob.backend.consumer.game.reversi;

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
public class ReversiPlayer {
    private Integer id;
    private Integer botId; // -1为人工
    private String language;
    private String botCode;
    private String color;
    private Boolean isRound;
    private List<Cell> self;
    private List<Cell> steps;
    private String container;
    // 违法操作次数
    private Integer validCount;


    public void set_put(int x, int y) {
        this.self.add(new Cell(x, y));
    }

    public void remove(int x, int y) {
        ArrayList<Cell> new_cells = new ArrayList<>();
        self.forEach(cell -> {
            if (cell.x != x || cell.y != y) {
                new_cells.add(cell);
            }
        });
        this.self = new_cells;
    }

    public String getSelfString() {
        StringBuilder res = new StringBuilder();
        for (Cell d : self) {
            res.append(d.x + "," + d.y + " ");
        }
        return res.toString();
    }

    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for (Cell d : steps) {
            res.append(d.x + "," + d.y + " ");
        }
        return res.toString();
    }
}
