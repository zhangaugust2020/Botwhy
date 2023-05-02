<template>
  <div>
    <PlayGround
      class="body-base"
      v-if="$store.state.pk.snake.status === 'playing'"
    />
    <ChatGround
      v-if="$store.state.pk.snake.status === 'chating'"
      :game="game"
    />
    <MatchGround
      v-if="$store.state.pk.snake.status === 'matching'"
      :game="game"
    />
    <ResultBoard
      v-if="
        $store.state.pk.snake.loser !== 'none' &&
        $store.state.pk.snake.status === 'playing'
      "
      :game="game"
    />
  </div>
</template>

<script>
import { onMounted, onUnmounted, ref } from 'vue'
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
    const game = ref("snake");
    const socketUrl = `wss://app459.acapp.acwing.com.cn/websocket/${game.value}/${store.state.user.token}`;
    store.commit("updateSnakeLoser", "none");
    store.commit("updateIsSnakeRecord", false);
    let socket = null;
    onMounted(() => {
      store.commit("updateSnakeOpponent", {
        username: "我的对手",
        photo: "https://cdn.acwing.com/media/article/image/2023/03/11/36510_1fd01b93bf-16gl-questionMark.png",
        rating: null,
      })
      socket = new WebSocket(socketUrl);

      socket.onopen = () => {
        console.log("connected!");
        store.commit("updateSnakeSocket", socket)
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
        store.commit("updateSnakeStatus", "matching");
        store.commit("updateSnakeHistory", []);
      }
    })

    const on_playing = (data) => {
      store.commit("updateSnakeOpponent", {
        username: data.opponent_username,
        photo: data.opponent_photo,
        rating: null,
      })
      setTimeout(() => {
        store.commit("updateSnakeStatus", "playing")
      }, 500);
      store.commit("updateSnakeGame", data.game);
    }

    const on_move = (data) => {
      const game = store.state.pk.snake.gameObject;
      const [snake0, snake1] = game.snakes;
      snake0.set_direction(data.a_direction);
      snake1.set_direction(data.b_direction);
      if (store.state.user.id === store.state.pk.snake.a_id) {
        store.commit("updateSnakeDirection", data.a_direction);
      } else {
        store.commit("updateSnakeDirection", data.b_direction);
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
      store.commit("updateSnakeLoser", data.loser);
    }

    const on_chating = (data) => {
      setTimeout(() => {
        store.commit("updateSnakeStatus", "chating")
      }, 200);
      store.commit("updateSnakeOpponent", {
        username: data.opponent_username,
        photo: data.opponent_photo,
        rating: data.opponent_rating,
      })
    }

    const on_sendmsg = (data) => {
      let k = store.state.pk.snake.history;
      k.push({
        id: k.length + 1,
        is_me: false,
        content: data.content,
      })
      store.commit("updateSnakeHistory", k);
    }

    const on_chat_disconnect = () => {
      store.commit("updateSnakeOpponent", {
        username: "我的对手",
        photo: "https://cdn.acwing.com/media/article/image/2023/03/11/36510_1fd01b93bf-16gl-questionMark.png",
        rating: null,
      });
      store.commit("updateSnakeReadyStatus", "未准备");
      store.commit("updateSnakeHistory", []);
      store.commit("updateSnakeStatus", "matching");
    }

    const on_ready = (data) => {
      store.commit("updateSnakeReadyStatus", data.ready);
    }

    const on_chat_to_game = () => {
      setTimeout(() => {
        store.commit("updateSnakeReadyStatus", "未准备");
        store.commit("updateSnakeHistory", []);
      }, 1000);
    }

    onUnmounted(() => {
      store.commit("updateSnakeLoser", "none");
      socket.close();
    })

    return {
      game,
    }
  }
}
</script>

<style scoped>
</style>