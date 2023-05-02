<template>
  <div>
    <PlayGround
      class="body-base"
      v-if="$store.state.pk.reversi.status === 'playing'"
    />
    <ChatGround
      v-if="$store.state.pk.reversi.status === 'chating'"
      :game="game"
    />
    <MatchGround
      v-if="$store.state.pk.reversi.status === 'matching'"
      :game="game"
    />
    <ResultBoard
      v-if="
        $store.state.pk.reversi.loser !== 'none' &&
        $store.state.pk.reversi.status === 'playing'
      "
      :game="game"
    />
  </div>
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchGround.vue'
import ResultBoard from '../../components/ResultBoard.vue'
import ChatGround from '../../components/ChatGround.vue'
import { onMounted, onUnmounted, ref } from 'vue'
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
    const game = ref("reversi");
    const socketUrl = `wss://app459.acapp.acwing.com.cn/websocket/${game.value}/${store.state.user.token}`;

    let socket = null;

    onMounted(() => {
      store.commit("updateReversiOpponent", {
        username: "我的对手",
        photo: "https://augu.oss-cn-beijing.aliyuncs.com/2023/04/28/351458d69b464bc68cbdbf03d2292133opponent.png",
        rating: null,
      })
      store.commit("updateReversiLoser", "none");
      store.commit("updateIsReversiRecord", false);
      socket = new WebSocket(socketUrl);
      console.log(socket);

      socket.onopen = () => {
        console.log("connected!");
        store.commit("updateReversiSocket", socket)
      }
      socket.onmessage = msg => {
        const data = JSON.parse(msg.data);
        if (data.event === "playing") {
          on_playing(data);
        } else if (data.event === "put") {
          on_put(data);
        } else if (data.event === "notice") {
          on_notice(data);
        } else if (data.event === "change") {
          on_change();
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
      }
    })

    const on_playing = (data) => {
      store.commit("updateReversiOpponent", {
        username: data.opponent_username,
        photo: data.opponent_photo,
        rating: null,
      })
      setTimeout(() => {
        store.commit("updateReversiStatus", "playing")
      }, 500);
      store.commit("updateReversiGame", data.game);
      if (store.state.user.id === data.game.a_id) {
        store.commit("updateReversiRound", data.game.a_round);
      } else {
        store.commit("updateReversiRound", data.game.b_round);
      }
    }

    const on_put = (data) => {
      const game = store.state.pk.reversi.gameObject;
      const round = store.state.pk.reversi.round;
      const [reversi0, reversi1] = game.reversi;
      reversi0.set_cells(data.cellsA);
      reversi1.set_cells(data.cellsB);
      store.commit("updateReversiGameMap", data.map);
      store.commit("updateReversiRound", !round);
      store.commit("updateReversiCount", data.cnt);
    }

    const on_notice = (data) => {
      // todo
      const msg = data.content;
      console.log(msg);
    }

    const on_change = () => {
      const round = store.state.pk.reversi.round;
      store.commit("updateReversiRound", !round);
    }

    const on_result = (data) => {
      store.commit("updateReversiLoser", data.loser);
    }

    const on_chating = (data) => {
      setTimeout(() => {
        store.commit("updateReversiStatus", "chating")
      }, 200);
      store.commit("updateReversiOpponent", {
        username: data.opponent_username,
        photo: data.opponent_photo,
        rating: data.opponent_rating,
      })
    }

    const on_sendmsg = (data) => {
      let k = store.state.pk.reversi.history;
      k.push({
        id: k.length + 1,
        is_me: false,
        content: data.content,
      })
      store.commit("updateReversiHistory", k);
    }

    const on_chat_disconnect = () => {
      store.commit("updateReversiOpponent", {
        username: "我的对手",
        photo: "https://cdn.acwing.com/media/article/image/2023/03/11/36510_1fd01b93bf-16gl-questionMark.png",
        rating: null,
      });
      store.commit("updateReversiReadyStatus", "未准备");
      store.commit("updateReversiHistory", []);
      store.commit("updateReversiStatus", "matching");
    }

    const on_ready = (data) => {
      store.commit("updateReversiReadyStatus", data.ready);
    }

    const on_chat_to_game = () => {
      setTimeout(() => {
        store.commit("updateReversiReadyStatus", "未准备");
        store.commit("updateReversiHistory", []);
      }, 1000);
    }
    onUnmounted(() => {
      socket.close();
      store.commit("updateReversiStatus", "matching");
      store.commit("updateReversiHistory", []);
    })

    return {
      game,
    }
  }
}
</script>

<style scoped>
</style>