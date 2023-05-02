<template>
  <div class="result-board">
    <div class="result-board-text" v-if="loser.value === 'all'">Draw</div>
    <div
      class="result-board-text"
      v-else-if="
        loser.value === 'A' && a_id.value === parseInt($store.state.user.id)
      "
    >
      Lose ~
    </div>
    <div
      class="result-board-text"
      v-else-if="
        loser.value === 'B' && b_id.value === parseInt($store.state.user.id)
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
import { computed, ref } from 'vue';
export default {
  props: {
    game: {
      type: String,
      required: true,
    }
  },
  setup (props) {
    const store = useStore();
    let loser = ref("");
    let a_id = ref("");
    let b_id = ref("");
    if (props.game === "snake") {
      loser.value = computed(() => store.state.pk.snake.loser);
      a_id.value = computed(() => store.state.pk.snake.a_id);
      b_id.value = computed(() => store.state.pk.snake.b_id);
    } else if (props.game === "reversi") {
      loser.value = computed(() => store.state.pk.reversi.loser);
      a_id.value = computed(() => store.state.pk.reversi.a_id);
      b_id.value = computed(() => store.state.pk.reversi.b_id);
    }

    const do_snake_restart = () => {
      store.commit("updateSnakeStatus", "matching");
      store.commit("updateSnakeLoser", "none");
      store.commit("updateSnakeDirection", -1);
      store.commit("updateSnakeOpponent", {
        username: "我的对手",
        photo: "https://cdn.acwing.com/media/article/image/2023/03/11/36510_1fd01b93bf-16gl-questionMark.png",
        rating: null,
      })
    }

    const do_reversi_restart = () => {
      store.commit("updateReversiStatus", "matching");
      store.commit("updateReversiLoser", "none");
      store.commit("updateReversiOpponent", {
        username: "我的对手",
        photo: "https://cdn.acwing.com/media/article/image/2023/03/11/36510_1fd01b93bf-16gl-questionMark.png",
        rating: null,
      })
    }

    const restart = () => {
      if (props.game === "snake") {
        do_snake_restart();
      } else if (props.game === "reversi") {
        do_reversi_restart();
      }
    }

    return {
      restart,
      loser,
      a_id,
      b_id,
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
