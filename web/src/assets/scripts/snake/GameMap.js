import { AcGameObject } from "../AcGameObject";
import { Wall } from "./Wall";
import { Snake } from './Snake.js';
export class GameMap extends AcGameObject {
  constructor(ctx, parent, store) {
    super();
    this.ctx = ctx;
    this.parent = parent;
    this.L = 0;
    this.store = store;
    this.rows = 13;
    this.cols = 14;

    this.inner_walls_count = 20;
    this.walls = [];

    this.color_cnt = 0;
    this.color = {
      wall: [
        "#B47225",
        "#46433f",
        "#1b263b",
        "#333837"
      ],
      map: [
        ["#A2D149", "#AAD751"],
        ["#a7aea7", "#979e97"],
        ["#a5b7f0", "#8da0e1"],
        ["#88aba1", "#689386"]
      ],
      snake: [
        ["#4876EC", "#F94847"],
        ["#877562", "#becd94"],
        ["#90c9e2", "#458d7b"],
        ["#90c9e2", "#d08496"],
      ],
    }
    this.color_cnt = parseInt(Math.random() * 4);

    this.snakes = [
      new Snake({ id: 0, color: this.color.snake[this.color_cnt][0], r: this.rows - 2, c: 1 }, this),
      new Snake({ id: 1, color: this.color.snake[this.color_cnt][1], r: 1, c: this.cols - 2 }, this),
    ]
  }

  add_listening_events () {
    // 录像逻辑: 每300ms指定一次方向
    if (this.store.state.record.is_snake_record) {
      let k = 0;
      const a_steps = this.store.state.record.a_steps;
      const b_steps = this.store.state.record.b_steps;
      const loser = this.store.state.record.record_loser;
      const [snake0, snake1] = this.snakes;
      const interval_id = setInterval(() => {
        this.store.commit("updateIntervalId", interval_id);
        if (k >= a_steps.length - 1) {
          if (loser === "all" || loser === "A") {
            snake0.status = "die";
          }
          if (loser === "all" || loser === "B") {
            snake1.status = "die";
          }
          clearInterval(interval_id);
        } else {
          snake0.set_direction(parseInt(a_steps[k]));
          snake1.set_direction(parseInt(b_steps[k]));
        }
        k++;
      }, 300);
    } else {
      this.ctx.canvas.focus();
      this.ctx.canvas.addEventListener("keydown", e => {
        let d = -1;
        if (e.key === "w" || e.key === "ArrowUp") d = 0;
        else if (e.key === "d" || e.key === "ArrowRight") d = 1;
        else if (e.key === "s" || e.key === "ArrowDown") d = 2;
        else if (e.key === "a" || e.key === "ArrowLeft") d = 3;
        if (this.snakes[0].status !== "die" && this.snakes[1].status !== "die") {
          this.store.commit("updateSnakeDirection", d);
        }

        if (d >= 0) {
          this.store.state.pk.snake.socket.send(
            JSON.stringify({
              event: "move",
              direction: d,
            })
          )
        }
      })
    }

  }

  start () {
    this.create_walls()
    this.add_listening_events();
  }

  create_walls () {
    const g = this.store.state.pk.snake.gamemap;
    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if (g[r][c]) {
          this.walls.push(new Wall(r, c, this));
        }
      }
    }
    return true;
  }

  update_size () {
    this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
    this.ctx.canvas.width = this.L * this.cols;
    this.ctx.canvas.height = this.L * this.rows;
  }

  // 判断两条蛇是否都准备好下一回合
  check_ready () {

    for (const snake of this.snakes) {
      if (snake.status !== "idle") return false;
      if (snake.direction === -1) return false;
    }
    return true;
  }

  // 让两条蛇进入下一回合
  next_step () {
    for (const snake of this.snakes) {
      snake.next_step();
    }
  }

  check_valid (cell) {
    for (const wall of this.walls) {
      if (wall.r === cell.r && wall.c === cell.c)
        return false;
    }
    for (const snake of this.snakes) {
      let k = snake.cells.length;
      if (!snake.check_tail_increasing()) { // 如果蛇尾会前进的时候, 蛇尾不要判断
        k--;
      }
      for (let i = 0; i < k; i++) {
        if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) {
          return false;
        }
      }
    }
    return true;
  }

  update () {
    this.update_size();
    if (this.check_ready()) {
      this.next_step();
    }
    this.render();
  }

  render () {
    // const color_even = "#A2D149", color_odd = "#AAD751";
    const color_even = this.color.map[this.color_cnt][0], color_odd = this.color.map[this.color_cnt][1];
    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if ((r + c) % 2 == 0) {
          this.ctx.fillStyle = color_even;
        } else {
          this.ctx.fillStyle = color_odd;
        }
        this.ctx.fillRect(this.L * c, this.L * r, this.L, this.L);
      }
    }
  }
}