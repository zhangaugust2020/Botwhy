import { AcGameObject } from "./AcGameObject";

export class Wall extends AcGameObject {
  constructor(r, c, gameMap) {
    super();
    this.r = r, this.c = c;
    this.gameMap = gameMap;
    this.color = this.gameMap.color.wall[this.gameMap.color_cnt];
  }

  start () {

  }

  update () {
    this.render();
  }

  render () {
    const L = this.gameMap.L;
    const ctx = this.gameMap.ctx;

    ctx.fillStyle = this.color;
    ctx.fillRect(this.c * L, this.r * L, L, L);
  }
}