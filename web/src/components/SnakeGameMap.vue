<template>
  <div ref="parent" class="gameMap">
    <div class="pk-info-A">
      <img :src="pk_player.a_photo" />
      <div class="pk-username">{{ pk_player.a_name }}</div>
    </div>

    <canvas ref="canvas" tabindex="0"></canvas>

    <div class="pk-info-B">
      <img :src="pk_player.b_photo" />
      <div class="pk-username">{{ pk_player.b_name }}</div>
    </div>

    <div
      class="direction-msg"
      v-if="$store.state.pk.snake.status === 'playing'"
    >
      <span>direction</span>
      <img :src="direction_photo.value" alt="" />
    </div>
  </div>
</template>

<script>
import { GameMap } from '@/assets/scripts/snake/GameMap'
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
export default {
  setup () {
    const store = useStore();
    let parent = ref(null);
    let canvas = ref(null);
    let pk_player = ref({});
    let direction_photo = ref("https://cdn.acwing.com/media/article/image/2023/03/11/36510_1e3d8651bf-none.png");
    direction_photo.value = computed(() => {
      switch (store.state.pk.snake.direction) {
        case 0:
          return "https://cdn.acwing.com/media/article/image/2023/03/11/36510_b35bee63bf-up.png";
        case 1:
          return "https://cdn.acwing.com/media/article/image/2023/03/11/36510_aa5034e0bf-right.png";
        case 2:
          return "https://cdn.acwing.com/media/article/image/2023/03/11/36510_b629dc76bf-down.png";
        case 3:
          return "https://cdn.acwing.com/media/article/image/2023/03/11/36510_b8a1c635bf-left.png";
        default:
          return "https://cdn.acwing.com/media/article/image/2023/03/11/36510_1e3d8651bf-none.png";
      }
    })
    const confirmInfo = () => {
      if (store.state.record.is_snake_record) {
        pk_player.value = {
          a_name: store.state.record.a_username,
          a_photo: store.state.record.a_photo,
          b_name: store.state.record.b_username,
          b_photo: store.state.record.b_photo,
        }
        return;
      }
      if (store.state.user.id === store.state.pk.snake.a_id) {
        pk_player.value = {
          a_name: store.state.user.username,
          a_photo: store.state.user.photo,
          b_name: store.state.pk.snake.opponent_username,
          b_photo: store.state.pk.snake.opponent_photo,
        }
      } else {
        pk_player.value = {
          b_name: store.state.user.username,
          b_photo: store.state.user.photo,
          a_name: store.state.pk.snake.opponent_username,
          a_photo: store.state.pk.snake.opponent_photo,
        }
      }
    }
    confirmInfo();
    onMounted(() => {
      store.commit(
        "updateSnakeGameObject",
        new GameMap(canvas.value.getContext('2d'), parent.value, store)
      );
    })

    return {
      parent,
      canvas,
      pk_player,
      direction_photo,
    }
  }
}
</script>

<style scoped>
div.gameMap {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

canvas:focus {
  outline: none;
}
.pk-info-A {
  margin-right: 10vh;
}
.pk-info-B {
  margin-left: 10vh;
}
.pk-info-A > img {
  height: 20vh;
  border-radius: 100%;
}
.pk-info-B > img {
  height: 20vh;
  border-radius: 100%;
}
.direction-msg {
  position: absolute;
  top: 86vh;
  width: 12vw;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 5vh;
  border-radius: 1vh;
  background-color: rgb(226, 237, 242, 0.3);
  -webkit-animation: heartbeat 1.5s ease-in-out infinite both;
  animation: heartbeat 1.5s ease-in-out infinite both;
}
.direction-msg > span {
  line-height: 5vh;
  font-weight: 600;
  font-size: 20px;
}
.direction-msg > img {
  height: 4vh;
  margin-left: 1vw;
}
.pk-username {
  color: rgb(242, 231, 237);
  text-align: center;
  margin-top: 2vh;
  font-size: 24px;
  font-weight: bold;
}

@-webkit-keyframes heartbeat {
  from {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-transform-origin: center center;
    transform-origin: center center;
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  10% {
    -webkit-transform: scale(0.91);
    transform: scale(0.91);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  17% {
    -webkit-transform: scale(0.98);
    transform: scale(0.98);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  33% {
    -webkit-transform: scale(0.87);
    transform: scale(0.87);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  45% {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
}
@keyframes heartbeat {
  from {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-transform-origin: center center;
    transform-origin: center center;
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  10% {
    -webkit-transform: scale(0.91);
    transform: scale(0.91);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  17% {
    -webkit-transform: scale(0.98);
    transform: scale(0.98);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  33% {
    -webkit-transform: scale(0.87);
    transform: scale(0.87);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  45% {
    -webkit-transform: scale(1);
    transform: scale(1);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
}
</style>