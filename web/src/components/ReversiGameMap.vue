<template>
  <div ref="parent" class="gameMap">
    <div class="pk-info-A">
      <div class="notice-msg" style="color: black">
        黑子 :
        <span class="pk-info-cnt">{{ $store.state.pk.reversi.a_cnt }}</span>
      </div>
      <img :src="pk_player.a_photo" />
      <div class="pk-username">{{ pk_player.a_name }}</div>
    </div>

    <canvas ref="canvas" tabindex="0"></canvas>

    <div class="pk-info-B">
      <div class="notice-msg" style="color: white">
        白子 :
        <span class="pk-info-cnt">{{ $store.state.pk.reversi.b_cnt }}</span>
      </div>
      <img :src="pk_player.b_photo" />
      <div class="pk-username">{{ pk_player.b_name }}</div>
    </div>

    <div class="round-msg" v-if="$store.state.pk.reversi.status === 'playing'">
      <span>当前回合: </span>
      <img :src="round_photo.value" alt="" />
    </div>

    <div class="round-msg" v-if="$store.state.record.is_reversi_record">
      <span style="color: #e5dd8a; font-size: 24px">
        {{ $store.state.record.result }}</span
      >
    </div>
  </div>
</template>

<script>
import { GameMap } from '@/assets/scripts/reversi/GameMap'
import { ref, onMounted, computed, onUnmounted } from 'vue'
import { useStore } from 'vuex'
export default {
  setup () {
    const store = useStore();
    let parent = ref(null);
    let canvas = ref(null);
    let pk_player = ref({});
    let round_photo = ref("");

    round_photo.value = computed(() => {
      if (store.state.user.id === store.state.pk.reversi.a_id) {
        if (store.state.pk.reversi.round) {
          return "https://cdn.acwing.com/media/article/image/2023/03/11/36510_5423e0d4bf-黑子.png";
        } else {
          return "https://cdn.acwing.com/media/article/image/2023/03/11/36510_56da3170bf-白子.png";
        }
      } else {
        if (store.state.pk.reversi.round) {
          return "https://cdn.acwing.com/media/article/image/2023/03/11/36510_56da3170bf-白子.png";
        } else {
          return "https://cdn.acwing.com/media/article/image/2023/03/11/36510_5423e0d4bf-黑子.png";
        }
      }
    })
    const confirmInfo = () => {
      if (store.state.record.is_reversi_record) {
        pk_player.value = {
          a_name: store.state.record.a_username,
          a_photo: store.state.record.a_photo,
          b_name: store.state.record.b_username,
          b_photo: store.state.record.b_photo,
        }
        return;
      }

      if (store.state.user.id === store.state.pk.reversi.a_id) {
        pk_player.value = {
          a_name: store.state.user.username,
          a_photo: store.state.user.photo,
          b_name: store.state.pk.reversi.opponent_username,
          b_photo: store.state.pk.reversi.opponent_photo,
        }
      } else {
        pk_player.value = {
          b_name: store.state.user.username,
          b_photo: store.state.user.photo,
          a_name: store.state.pk.reversi.opponent_username,
          a_photo: store.state.pk.reversi.opponent_photo,
        }
      }
    }
    confirmInfo();

    onMounted(() => {
      store.commit(
        "updateReversiGameObject",
        new GameMap(canvas.value.getContext('2d'), parent.value, store)
      );
    })

    onUnmounted(() => {
      store.commit("updateReversiCount", [2, 2]);
    })
    return {
      parent,
      canvas,
      pk_player,
      round_photo
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
canvas:hover {
  cursor: pointer;
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
  border-radius: 50%;
}
.pk-info-B > img {
  height: 20vh;
  border-radius: 50%;
}

.pk-username {
  color: rgb(242, 231, 237);
  text-align: center;
  margin-top: 2vh;
  font-size: 24px;
  font-weight: bold;
}
.notice-msg {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 1vh;
}

.round-msg {
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
.round-msg > span {
  line-height: 5vh;
  font-weight: 600;
  font-size: 20px;
}
.round-msg > img {
  height: 4vh;
  margin-left: 1vw;
}

.pk-info-cnt {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
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