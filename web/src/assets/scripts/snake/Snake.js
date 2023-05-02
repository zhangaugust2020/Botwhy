import { AcGameObject } from "../AcGameObject";
import { Cell } from "./Cell";

export class Snake extends AcGameObject {
  constructor(info, gameMap) {
    super();
    this.id = info.id;
    this.color = info.color;
    this.gameMap = gameMap;

    this.cells = [new Cell(info.r, info.c)]; // 蛇的身体, cells[0]存放蛇头
    this.next_cell = null; // 下一步的目标位置

    this.speed = 5; // 蛇每秒钟走五个格子
    this.direction = -1; // -1表示没有指令, 0 1 2 3 表示上右下左
    this.status = "idle"; // idle : 静止、 move : 正在移动、die : 死亡

    // 偏移量
    this.dr = [-1, 0, 1, 0];
    this.dc = [0, 1, 0, -1];

    this.step = 0;
    this.eps = 1e-2;
    this.eye_direction = 0;
    if (this.id === 1) this.eye_direction = 2; // 右上角初始朝下, 左上角初始朝上

    this.eye_dx = [ // 蛇眼睛不同方向的x偏移量
      [-1, 1],
      [1, 1],
      [1, -1],
      [-1, -1],
    ];
    this.eye_dy = [ // 蛇眼睛不同方向的y偏移量
      [-1, -1],
      [-1, 1],
      [1, 1],
      [1, -1],
    ];
  }

  start () {

  }

  update () {
    if (this.status === "move") {
      this.update_move();
    }
    this.render();
  }

  set_direction (d) {
    this.direction = d;
  }

  check_tail_increasing () {
    if (this.step <= 10) return true;
    if (this.step % 3 === 1) return true;
    return false;
  }

  // 将蛇的状态变为走下一步
  next_step () {
    const d = this.direction;
    this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
    this.eye_direction = d;
    this.direction = -1; // 清空操作;
    this.status = "move";
    this.step++;

    let k = this.cells.length;
    // 将蛇往后移一位, 多出一个蛇头
    for (let i = k; i > 0; i--) {
      this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
    }

    // if (!this.gameMap.check_valid(this.next_cell)) { // 下一步非法, 蛇去世
    //   this.status = "die";
    // }
  }

  update_move () {
    const dx = this.next_cell.x - this.cells[0].x;
    const dy = this.next_cell.y - this.cells[0].y;

    const distance = Math.sqrt(dx * dx + dy * dy);
    if (distance < this.eps) {
      this.cells[0] = this.next_cell;
      this.next_cell = null;
      this.status = "idle";
      if (!this.check_tail_increasing()) { // 蛇不变长砍掉蛇尾
        this.cells.pop();
      }
    } else {
      const move_distance = this.speed * this.timedelta / 1000; // 每两帧之间走的距离
      this.cells[0].x += move_distance * dx / distance;
      this.cells[0].y += move_distance * dy / distance;

      // 蛇不变长
      if (!this.check_tail_increasing()) {
        const k = this.cells.length;
        const tail = this.cells[k - 1], tail_target = this.cells[k - 2];
        const tail_dx = tail_target.x - tail.x;
        const tail_dy = tail_target.y - tail.y;
        tail.x += move_distance * tail_dx / distance;
        tail.y += move_distance * tail_dy / distance;
      }
    }
  }

  render () {
    const L = this.gameMap.L;
    const ctx = this.gameMap.ctx;

    ctx.fillStyle = this.color;
    if (this.status === "die") {
      ctx.fillStyle = "white";
    }
    for (const cell of this.cells) {
      ctx.beginPath();
      ctx.arc(cell.x * L, cell.y * L, L * 0.4, 0, Math.PI * 2);
      ctx.fill();
    }

    for (let i = 1; i < this.cells.length; i++) {
      const a = this.cells[i], b = this.cells[i - 1];
      if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) continue;
      if (Math.abs(a.x - b.x) < this.eps) {
        ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
      }
      if (Math.abs(a.y - b.y) < this.eps) {
        ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
      }
    }

    ctx.fillStyle = "black";
    for (let i = 0; i < 2; i++) {
      const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
      const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;

      ctx.beginPath();
      ctx.arc(eye_x, eye_y, L * 0.04, 0, Math.PI * 2);
      ctx.fill();
    }
  }
}