<template>
  <div class="result-board">
    <div class="result-board-text" v-if="$store.state.pk.snake.loser === 'all'">
      Draw
    </div>
    <div
      class="result-board-text"
      v-else-if="
        $store.state.pk.snake.loser === 'A' &&
        $store.state.pk.snake.a_id === parseInt($store.state.user.id)
      "
    >
      Lose ~
    </div>
    <div
      class="result-board-text"
      v-else-if="
        $store.state.pk.snake.loser === 'B' &&
        $store.state.pk.snake.b_id === parseInt($store.state.user.id)
      "
    >
      Lose ~
    </div>
    <div class="result-board-text" v-else style="color: #aad751">Win !</div>
    <div class="result-board-btn">
      <button @click="restart" type="button" class="btn btn-restart">
        再来!
      </button>
    </div>
  </div>
</template>

<script>
import { useStore } from 'vuex';

export default {
  setup () {
    const store = useStore();

    const restart = () => {
      store.commit("updateStatus", "matching");
      store.commit("updateLoser", "none");
      store.commit("updateDirection", -1);
      store.commit("updateOpponent", {
        username: "我的对手",
        photo: "https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/501/16gl-questionMark.png",
        rating: null,
      })
    }

    return {
      restart
    };
  }
}
</script>

<style scoped>
div.result-board {
  height: 30vh;
  width: 30vw;
  background-color: rgba(50, 50, 50, 0.5);
  position: absolute;
  top: 30vh;
  left: 35vw;
  border-radius: 3vh;
}
div.result-board-text {
  text-align: center;
  color: white;
  font-size: 50px;
  font-weight: 600;
  font-style: italic;
  padding-top: 9vh;
}

div.result-board-btn {
  padding-top: 3vh;
  text-align: center;
}

.btn-restart {
  background-color: rgb(199, 130, 92);
  margin-top: 14vh;
  color: white;
  font-size: 20px;
  width: 15vh;
  border: none;
}
.btn-restart:hover {
  scale: 1.1;
}
</style>
