<template>
  <div class="body-base game-show">
    <div class="card">
      <div class="photo">
        <img
          src="https://augu.oss-cn-beijing.aliyuncs.com/2023/04/28/14.png"
          alt=""
        />
      </div>
      <h3>贪吃蛇</h3>
      <h2>游戏类型：双人对战</h2>
      <p>经典双人回合制游戏，通过控制蛇的方向去堵住对方</p>
      <router-link class="entrance" :to="{ name: 'snake_view' }"
        >进入游戏</router-link
      >
    </div>

    <div class="card">
      <div class="photo">
        <img
          src="https://augu.oss-cn-beijing.aliyuncs.com/2023/04/28/6.png"
          alt=""
        />
      </div>
      <h3>黑白棋</h3>
      <h2>游戏类型：双人对战</h2>
      <p>游戏通过相互翻转对方的棋子，最后以棋盘上谁的棋子多来判断胜负。</p>
      <router-link class="entrance" :to="{ name: 'reversi_view' }"
        >进入游戏</router-link
      >
    </div>

    <div class="card">
      <div class="photo">
        <img
          src="https://augu.oss-cn-beijing.aliyuncs.com/2023/04/28/13.png"
          alt=""
        />
      </div>
      <h3>圈小猫</h3>
      <h2>游戏类型：单人娱乐</h2>
      <p>这是一个摸鱼小游戏， 快来围住这只小猫别让它跑掉。</p>
      <router-link class="entrance" :to="{ name: 'chat_noir_view' }"
        >进入游戏</router-link
      >
    </div>
  </div>
</template>

<script>
export default {
  components: {
  }
}
</script>

<style scoped>
.game-show {
  height: 100vh;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  display: flex;
}
.card {
  position: relative;
  width: 18vw;
  height: 55vh;
  margin: 20px;
  background-color: #797675;
  border-radius: 20px;
  /* 溢出隐藏 */
  overflow: hidden;
  /* 弹性布局 */
  display: flex;
  /* 元素纵向排列 */
  flex-direction: column;
  /* 居中 */
  align-items: center;
  color: #fff;
  /* 阴影 */
  box-shadow: 0 0 30px rgba(0, 0, 0, 0.5);
  /* 不让其被挤压 */
  flex-shrink: 0;
}
.card .photo img {
  width: 100%;
  height: 100%;
  /* 保持原有尺寸比例，裁切长边 */
  object-fit: cover;
}
/* 默认大图 */
.card .photo {
  /* 绝对定位 */
  position: absolute;
  top: 0;
  width: 100%;
  height: 100%;
  border-radius: 0%;
  overflow: hidden;
  /* 动画过渡 */
  transition: 0.5s;
}
/* 鼠标移入变小图 */
.card:hover .photo {
  top: 3vh;
  width: 12vh;
  height: 12vh;
  border-radius: 50%;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.8);
}
/* 这里加个黑色到透明的渐变背景，可以更好的看清楚名字 */
.card .photo::before {
  content: "";
  position: absolute;
  width: 100%;
  height: 100%;
  background: linear-gradient(to bottom, transparent 50%, #222);
}
.card h3 {
  position: absolute;
  top: 48vh;
  transition: 0.5s;
}
.card:hover h3 {
  top: 20vh;
}
.card h2 {
  margin-top: 26vh;
  width: 80%;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  font-size: 20px;
  text-align: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
}
.card p {
  width: 90%;
  text-indent: 32px;
  font-size: 14px;
  margin-bottom: 15px;
  line-height: 30px;
}
.entrance {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  border: 1px solid rgba(255, 255, 255, 0.5);
  margin-top: 2vh;
  padding: 8px 32px;
  border-radius: 8px;
}
.entrance:hover {
  color: #fff;
  background-color: rgba(255, 255, 255, 0.2);
}
</style>