import { AcGameObject } from "../AcGameObject";
import { Reversi } from './Reversi';

export class GameMap extends AcGameObject {
  constructor(ctx, parent, store) {
    super();
    this.ctx = ctx;
    this.parent = parent;
    this.store = store;
    this.L = 0;
    this.rows = 8;
    this.cols = 8;
    this.g = []; // 0空 1黑 2白
    this.reversi = [
      new Reversi({ id: 1, color: "black" }, this),
      new Reversi({ id: 2, color: "white" }, this),
    ];
  }

  start () {
    this.init_map();
    this.add_listening_events();
  }

  add_listening_events () {
    const reversi_record = this.store.state.record.is_reversi_record;
    if (reversi_record) {
      let k = 1; // 奇数下黑子, 偶数下白子
      let i = 0, j = 0; // 黑白子下到了哪一步
      const a_steps = this.store.state.record.a_steps;
      const b_steps = this.store.state.record.b_steps;
      if (a_steps == "" && b_steps == "") {
        return;
      }
      const interval_id = setInterval(() => {
        this.store.commit("updateIntervalId", interval_id);
        if (this.check_change(k)) {
          k++;
        }
        if (k % 2 === 1) {
          this.set_put(a_steps[i][0], a_steps[i][1], 1);
          this.reverse(a_steps[i][0], a_steps[i][1], 1);
          i++;
        } else {
          this.set_put(b_steps[j][0], b_steps[j][1], 2);
          this.reverse(b_steps[j][0], b_steps[j][1], 2);
          j++;
        }
        this.store.commit("updateReversiCount", this.calculate_count());
        k++;
        if (i == a_steps.length && j === b_steps.length) {
          clearInterval(interval_id);
        }
      }, 300)
    } else {
      this.ctx.canvas.focus();
      this.ctx.canvas.addEventListener("click", e => {
        let x = -1, y = -1;
        x = parseInt((e.clientY - this.parent.children[1].offsetTop) / this.L);
        y = parseInt((e.clientX - this.parent.children[1].offsetLeft) / this.L);
        if (this.store.state.pk.reversi.round && x !== -1 && y !== -1) {
          this.store.state.pk.reversi.socket.send(
            JSON.stringify({
              event: "put",
              x,
              y,
            })
          )
        }
      })
    }
  }

  check_change (k) {
    let is_black_end = this.no_select(1);
    let is_white_end = this.no_select(2);
    return (k % 2 === 1 && is_black_end) || (k % 2 === 0 && is_white_end)
  }

  no_select (id) {
    for (let i = 0; i < this.rows; i++) {
      for (let j = 0; j < this.cols; j++) {
        for (let k = 0; k < 8; k++) {
          if (this.g[i][j] == 0 && this.check_reverse(i, j, i, j, 0, 0, k, id, 1)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  init_map () {
    const reversi_record = this.store.state.record.is_reversi_record;
    if (reversi_record) {
      for (let r = 0; r < this.rows; r++) {
        this.g[r] = [];
        for (let c = 0; c < this.cols; c++) {
          this.g[r][c] = 0;
        }
      }
      this.g[3][3] = 1, this.g[3][4] = 2, this.g[4][3] = 2, this.g[4][4] = 1;
    } else {
      this.g = this.store.state.pk.reversi.gamemap;
    }
  }

  refresh_map () {
    const reversi_record = this.store.state.record.is_reversi_record;
    if (!reversi_record) {
      this.g = this.store.state.pk.reversi.gamemap;
    }
  }

  reverse (x, y, id) {
    for (let i = 0; i < 8; i++) {
      this.check_reverse(x, y, x, y, 0, 0, i, id, 0)
    }
  }

  // x, y用来记录初始位置, ex,ey记录偏移位置,
  // w用来标记是否遇到白子, b标记是否遇到黑子, d往哪个方向搜, id代表黑白子
  check_reverse (x, y, ex, ey, w, b, d, id, s) {
    const dx = [0, 1, 0, -1, 1, 1, -1, -1];
    const dy = [1, 0, -1, 0, -1, 1, 1, -1];
    let tx = ex + dx[d], ty = ey + dy[d];
    if (tx < 0 || ty < 0 || tx >= 8 || ty >= 8) return false;
    // 黑子
    if (id === 1) {
      if (this.g[tx][ty] === 1 && w === 1) {
        while ((x !== tx || y !== ty) && !s) {
          x += dx[d], y += dy[d];
          if (x === tx && y === ty) break;
          this.remove(x, y, id + 1);
          this.set_put(x, y, id);
        }
        return true;
      }
      if (this.g[tx][ty] === 2) {
        return this.check_reverse(x, y, tx, ty, 1, 0, d, id, s);
      }
    } else { // 白子
      if (this.g[tx][ty] === 2 && b === 1) {
        while ((x !== tx || y !== ty) && !s) {
          x += dx[d], y += dy[d];
          if (x === tx && y === ty) break;
          this.remove(x, y, id - 1);
          this.set_put(x, y, id);
        }
        return true;
      }
      if (this.g[tx][ty] === 1) {
        return this.check_reverse(x, y, tx, ty, 0, 1, d, id, s);
      }
    }
    return false;
  }

  update_size () {
    this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
    this.ctx.canvas.width = this.L * this.cols;
    this.ctx.canvas.height = this.L * this.rows;
  }

  calculate_count () {
    let white_cnt = 0, black_cnt = 0;
    for (let i = 0; i < this.rows; i++) {
      for (let j = 0; j < this.cols; j++) {
        if (this.g[i][j] === 1) {
          black_cnt++;
        } else if (this.g[i][j] === 2) {
          white_cnt++;
        }
      }
    }
    return [
      black_cnt,
      white_cnt,
    ]
  }

  update () {
    this.update_size();
    this.refresh_map();
    this.render();
  }

  remove (x, y, id) {
    const [reversi1, reversi2] = this.reversi;
    if (id === 1) {
      reversi1.remove(x, y);
    } else {
      reversi2.remove(x, y);
    }
  }

  set_put (x, y, id) {
    const [reversi1, reversi2] = this.reversi;
    this.g[x][y] = id;
    if (id === 1) reversi1.set_put(x, y);
    else {
      reversi2.set_put(x, y);
    }
  }

  render () {
    const color_even = "#a7aea7", color_odd = "#979e97";
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