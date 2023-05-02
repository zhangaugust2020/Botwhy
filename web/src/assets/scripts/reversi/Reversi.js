import { AcGameObject } from "../AcGameObject";
import { Cell } from "../snake/Cell";
export class Reversi extends AcGameObject {
  constructor(info, gameMap) {
    super();
    this.id = info.id;
    this.color = info.color;
    this.gameMap = gameMap;
    this.cells = [];
  }

  start () {
    if (this.id === 1) {
      this.cells.push(...[new Cell(3, 3), new Cell(4, 4)]);
    } else {
      this.cells.push(...[new Cell(3, 4), new Cell(4, 3)]);
    }
  }

  set_cells (cells) {
    let t = [];
    let new_cells = [];
    t = cells.split(' ');
    for (let i = 0; i < t.length - 1; i++) {
      let [x, y] = t[i].split(',');
      new_cells.push(new Cell(parseInt(x), parseInt(y)));
    }
    this.cells = new_cells;
  }

  remove (x, y) {
    let new_cells = [];
    for (let c of this.cells) {
      if (c.r !== x || c.c !== y) {
        new_cells.push(c);
      }
    }
    this.cells = new_cells;
  }

  set_put (x, y) {
    this.cells.push(new Cell(x, y));
  }

  update () {
    this.render();
  }

  render () {
    const L = this.gameMap.L;
    const ctx = this.gameMap.ctx;

    ctx.fillStyle = this.color;

    for (const cell of this.cells) {
      ctx.beginPath();
      ctx.arc(cell.x * L, cell.y * L, L * 0.4, 0, Math.PI * 2);
      ctx.fill();
    }
  }
}