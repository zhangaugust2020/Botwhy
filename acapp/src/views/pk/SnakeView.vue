<template>
  <div>
    <PlayGround
      class="body-base"
      v-if="$store.state.pk.snake.status === 'playing'"
    />
    <ChatGround v-if="$store.state.pk.snake.status === 'chating'" />
    <MatchGround v-if="$store.state.pk.snake.status === 'matching'" />
    <ResultBoard v-if="$store.state.pk.snake.loser !== 'none'" />
  </div>
</template>

<script>
import { onMounted, onUnmounted } from 'vue'
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchGround.vue'
import ResultBoard from '../../components/ResultBoard.vue'
import ChatGround from '../../components/ChatGround.vue'
import { useStore } from 'vuex'
export default {
  name: "SnakeView",
  components: {
    PlayGround,
    MatchGround,
    ChatGround,
    ResultBoard
  },
  setup () {
    const store = useStore();
    const socketUrl = `ws://127.0.0.1:10300/websocket/${store.state.user.token}`;
    store.commit("updateLoser", "none");
    store.commit("updateIsRecord", false);
    let socket = null;
    onMounted(() => {
      store.commit("updateOpponent", {
        username: "我的对手",
        photo: "https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/501/16gl-questionMark.png",
        rating: null,
      })
      socket = new WebSocket(socketUrl);

      socket.onopen = () => {
        console.log("connected!");
        store.commit("updateSocket", socket)
      }
      socket.onmessage = msg => {
        const data = JSON.parse(msg.data);
        if (data.event === "playing") {
          on_playing(data);
        } else if (data.event === "move") {
          on_move(data);
        } else if (data.event === "result") {
          on_result(data);
        } else if (data.event === "chating") {
          on_chating(data);
        } else if (data.event === "send-msg") {
          on_sendmsg(data);
        } else if (data.event === "chat-disconnect") {
          on_chat_disconnect(data);
        } else if (data.event === "send-ready") {
          on_ready(data);
        } else if (data.event === "chat-to-game") {
          on_chat_to_game();
        }
      }

      socket.onclose = () => {
        console.log("disconnected");
        store.commit("updateStatus", "matching");
        store.commit("updateHistory", []);
      }
    })

    const on_playing = (data) => {
      store.commit("updateOpponent", {
        username: data.opponent_username,
        photo: data.opponent_photo,
        rating: null,
      })
      setTimeout(() => {
        store.commit("updateStatus", "playing")
      }, 500);
      store.commit("updateGame", data.game);
    }

    const on_move = (data) => {
      const game = store.state.pk.snake.gameObject;
      const [snake0, snake1] = game.snakes;
      snake0.set_direction(data.a_direction);
      snake1.set_direction(data.b_direction);
      if (store.state.user.id === store.state.pk.snake.a_id) {
        store.commit("updateDirection", data.a_direction);
      } else {
        store.commit("updateDirection", data.b_direction);
      }
    }

    const on_result = (data) => {
      const game = store.state.pk.snake.gameObject;
      const [snake0, snake1] = game.snakes;
      if (data.loser === "all" || data.loser === "A") {
        snake0.status = "die";
      }
      if (data.loser === "all" || data.loser === "B") {
        snake1.status = "die";
      }
      store.commit("updateLoser", data.loser);
    }

    const on_chating = (data) => {
      setTimeout(() => {
        store.commit("updateStatus", "chating")
      }, 200);
      store.commit("updateOpponent", {
        username: data.opponent_username,
        photo: data.opponent_photo,
        rating: data.opponent_rating,
      })
      store.commit("updateSelfRating", data.self_rating);
    }

    const on_sendmsg = (data) => {
      let k = store.state.pk.snake.history;
      k.push({
        id: k.length + 1,
        is_me: false,
        content: data.content,
      })
      store.commit("updateHistory", k);
    }

    const on_chat_disconnect = () => {
      store.commit("updateOpponent", {
        username: "我的对手",
        photo: "https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/501/16gl-questionMark (2).png",
        rating: null,
      });
      store.commit("updateReadyStatus", "未准备");
      store.commit("updateHistory", []);
      store.commit("updateStatus", "matching");
    }

    const on_ready = (data) => {
      store.commit("updateReadyStatus", data.ready);
    }

    const on_chat_to_game = () => {
      setTimeout(() => {
        store.commit("updateReadyStatus", "未准备");
        store.commit("updateHistory", []);
      }, 1000);
    }

    onUnmounted(() => {
      store.commit("updateLoser", "none");
      socket.close();
    })
  }
}
</script>

<style scoped>
</style>