<template>
  <div class="chatground">
    <div class="row">
      <div class="col-7 chat-board">
        <div class="chat-board-content" ref="board_content">
          <div
            :class="
              msg.is_me
                ? 'chat-board-content-item item-me'
                : 'chat-board-content-item item-opponent'
            "
            v-for="msg in history.value"
            :key="msg.id"
          >
            <img
              class="item-avatar"
              :src="msg.is_me ? $store.state.user.photo : opponent_photo.value"
              alt=""
            />
            <span
              :class="msg.is_me ? 'item-content-me' : 'item-content-opponent'"
              >{{ msg.content }}</span
            >
          </div>
        </div>
        <hr />
        <div>
          <textarea
            class="form-control chat-board-send"
            placeholder="战斗前说点什么吧~"
            @keydown="enter"
            rows="2"
            v-model="now_msg"
          ></textarea>
          <button class="btn btn-send" @click="send_msg()">
            <span>发送(S)</span>
          </button>
        </div>
      </div>

      <div class="col-3 info">
        <div class="card">
          <div class="card-body">
            <div class="both-sides row">
              <div class="col-3">
                <img
                  class="both-sides-avatar"
                  :src="$store.state.user.photo"
                  alt=""
                />
              </div>
              <div class="col-5">
                <div class="info-username">
                  {{ $store.state.user.username }}
                </div>
                <div class="info-rating">
                  rank {{ $store.state.user.rating }}
                </div>
              </div>
              <div class="col-4">
                <button
                  class="btn info-status"
                  :style="me_ready_status === '已准备' ? 'color: #283B4C;' : ''"
                >
                  {{ me_ready_status }}
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="card">
          <div class="card-body">
            <div class="both-sides row">
              <div class="col-3">
                <img
                  class="both-sides-avatar"
                  :src="opponent_photo.value"
                  alt=""
                />
              </div>
              <div class="col-5">
                <div class="info-username">
                  {{ opponent_username.value }}
                </div>
                <div class="info-rating">rank {{ opponent_rating.value }}</div>
              </div>
              <div class="col-4">
                <button
                  class="btn info-status"
                  :style="
                    opponent_ready_status.value === '已准备'
                      ? 'color: #283B4C;'
                      : ''
                  "
                >
                  {{ opponent_ready_status.value }}
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="col-12 notice-msg">准备完毕? 请点击准备按钮</div>
        <button class="btn btn-ready" @click="click_ready">
          {{ btn_ready_info }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { useStore } from 'vuex';
import { computed, onUpdated, ref } from 'vue'
export default {
  props: {
    game: {
      type: String,
      required: true,
    }
  },
  setup (props) {
    const store = useStore();
    let btn_ready_info = ref("开始准备");
    let me_ready_status = ref("未准备");
    let now_msg = ref("");
    let board_content = ref();
    let history = ref([]);
    let opponent_photo = ref("");
    let opponent_username = ref("");
    let opponent_rating = ref("");
    let opponent_ready_status = ref("");

    if (props.game === "snake") {
      history.value = computed(() => store.state.pk.snake.history);
      opponent_photo.value = computed(() => store.state.pk.snake.opponent_photo);
      opponent_username.value = computed(() => store.state.pk.snake.opponent_username);
      opponent_rating.value = computed(() => store.state.pk.snake.opponent_rating);
      opponent_ready_status.value = computed(() => store.state.pk.snake.opponent_ready_status);
    } else if (props.game === "reversi") {
      history.value = computed(() => store.state.pk.reversi.history);
      opponent_photo.value = computed(() => store.state.pk.reversi.opponent_photo);
      opponent_username.value = computed(() => store.state.pk.reversi.opponent_username);
      opponent_rating.value = computed(() => store.state.pk.reversi.opponent_rating);
      opponent_ready_status.value = computed(() => store.state.pk.reversi.opponent_ready_status);
    }

    onUpdated(() => {
      board_content.value.scrollTop = board_content.value.scrollHeight;
    })
    const do_snake_send_msg = (now_msg) => {
      store.state.pk.snake.socket.send(JSON.stringify({
        event: "chat",
        content: now_msg.value,
      }))
      let k = store.state.pk.snake.history;
      k.push({
        id: k.length + 1,
        is_me: true,
        content: now_msg.value,
      })
      store.commit("updateSnakeHistory", k);
    }

    const do_reversi_send_msg = (now_msg) => {
      store.state.pk.reversi.socket.send(JSON.stringify({
        event: "chat",
        content: now_msg.value,
      }))
      let k = store.state.pk.reversi.history;
      k.push({
        id: k.length + 1,
        is_me: true,
        content: now_msg.value,
      })
      store.commit("updateReversiHistory", k);
    }


    const send_msg = () => {
      if (now_msg.value === "") {
        return false;
      }
      if (props.game === "snake")
        do_snake_send_msg(now_msg);
      else if (props.game === "reversi")
        do_reversi_send_msg(now_msg);

      now_msg.value = "";
    }
    const enter = (e) => {
      if (e.key === 'Enter') {
        e.preventDefault();
        send_msg();
      }
    }
    const do_snake_ready = (ready) => {
      store.state.pk.snake.socket.send(JSON.stringify({
        event: "ready",
        status: ready,
      }))
    }
    const do_reversi_ready = (ready) => {
      store.state.pk.reversi.socket.send(JSON.stringify({
        event: "ready",
        status: ready,
      }))
    }
    const click_ready = () => {
      if (btn_ready_info.value === "开始准备") {
        btn_ready_info.value = "取消准备";
        me_ready_status.value = "已准备";
        if (props.game === "snake")
          do_snake_ready(1);
        else if (props.game === "reversi")
          do_reversi_ready(1);
      } else if (btn_ready_info.value === "取消准备") {
        btn_ready_info.value = "开始准备";
        me_ready_status.value = "未准备";
        if (props.game === "snake")
          do_snake_ready(0);
        else if (props.game === "reversi")
          do_reversi_ready(0);
      }
    }
    return {
      now_msg,
      send_msg,
      enter,
      board_content,
      btn_ready_info,
      click_ready,
      me_ready_status,
      history,
      opponent_photo,
      opponent_username,
      opponent_rating,
      opponent_ready_status,
    }
  },
}
</script>

<style scoped>
.chatground {
  width: 70vw;
  height: 70vh;
  margin: 0 auto;
  margin-top: 100px;
  border-radius: 1vh;
  background-color: rgba(158, 148, 131, 0.5);
}
.btn-ready {
  background-color: rgb(117, 135, 234);
  margin-top: 4vh;
  margin-left: 6vw;
  color: white;
  font-size: 20px;
  width: 10vw;
  border: none;
}
.btn-ready:hover {
  scale: 1.1;
}
.chat-board {
  height: 58vh;
  position: relative;
  left: 4vw;
  top: 6vh;
  border-radius: 1vh;
  background-color: #f5f5f5;
}
.chat-board-content {
  box-sizing: content-box;
  height: 65%;
  margin-top: 1.5vh;
  border: 1px solid gainsboro;
  border-radius: 2px;
  overflow-y: scroll;
  overflow-x: hidden;
}

/*滚动条样式*/
.chat-board-content::-webkit-scrollbar {
  width: 4px;
}
.chat-board-content::-webkit-scrollbar-thumb {
  background: #d2d2d2;
}
.chat-board-content::-webkit-scrollbar-track {
  background: #f5f5f5;
}

.btn-send {
  margin-top: 1vh;
  width: 6vw;
  height: 4vh;
  float: right;
  border: none;
  background-color: #e9e9e9;
}

hr {
  margin: 1.8vh;
}
.btn-send:hover {
  background-color: #d2d2d2;
}
.btn-send > span {
  position: relative;
  bottom: 0.7vh;
  font-size: 16px;
  color: #06ae57;
}
textarea {
  resize: none;
}
.info {
  margin-left: 5vw;
  margin-top: 14vh;
}
.card {
  width: 22vw;
  height: 12vh;
  background-color: rgb(195, 214, 220);
  box-shadow: 2px 2px 10px lightgray;
  margin-bottom: 2vh;
}
.both-sides-avatar {
  position: relative;
  top: -0.5vh;
  height: 8vh;
  border-radius: 50%;
}
.both-sides-avatar:hover {
  -webkit-animation: rotate-center 0.6s ease-in-out both;
  animation: rotate-center 0.6s ease-in-out both;
  cursor: pointer;
}
.both-sides {
  display: flex;
}
.info-username {
  justify-content: center;
  align-items: center;
  font-weight: bold;
  font-size: 18px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: rgb(212, 96, 166);
}
.info-rating {
  font-weight: bold;
  font-size: 18px;
  color: rgb(97, 122, 221);
}
.info-status {
  position: relative;
  border: 1px solid rgb(188, 182, 182);
  font-weight: bold;
  font-size: 24px;
  right: 2vw;
  width: 8vw;
  color: rgb(115, 121, 197);
  height: 100%;
  box-shadow: 2px 2px 10px lightgray;
}
.info-status:hover {
  -webkit-animation: flip-horizontal-bottom 0.4s
    cubic-bezier(0.455, 0.03, 0.515, 0.955) both;
  animation: flip-horizontal-bottom 0.4s cubic-bezier(0.455, 0.03, 0.515, 0.955)
    both;
}
.notice-msg {
  font-size: 20px;
  font-weight: bold;
  color: #a4cbb8;
  text-align: center;
  margin-top: 6vh;
  margin-left: 3vw;
}
.item-avatar {
  border-radius: 50%;
  height: 5vh;
}
.chat-board-content-item:nth-child(1) {
  padding-top: 1vh;
}
.chat-board-content-item {
  padding-bottom: 1vh;
  width: 100%;
  font-size: 16px;
  font-family: ‘Times New Roman’, Times, serif;
}
.item-me {
  position: relative;
  right: 3vh;
  display: flex;
  flex-direction: row-reverse;
}
.item-opponent {
  display: flex;
  position: relative;
  left: 3vh;
}
.item-content-me {
  border-radius: 1vh;
  height: 5vh;
  line-height: 5vh;
  padding: 0 1vh;
  margin-right: 2vh;
  background-color: #95ec69;
}
.item-content-opponent {
  background-color: #ffffff;
  border-radius: 1vh;
  height: 5vh;
  line-height: 5vh;
  padding: 0 1vh;
  margin-left: 2vh;
}

@-webkit-keyframes flip-horizontal-bottom {
  0% {
    -webkit-transform: rotateX(0);
    transform: rotateX(0);
  }
  100% {
    -webkit-transform: rotateX(-180deg);
    transform: rotateX(-180deg);
  }
}
@keyframes flip-horizontal-bottom {
  0% {
    -webkit-transform: rotateX(0);
    transform: rotateX(0);
  }
  100% {
    -webkit-transform: rotateX(-180deg);
    transform: rotateX(-180deg);
  }
}

@-webkit-keyframes rotate-center {
  0% {
    -webkit-transform: rotate(0);
    transform: rotate(0);
  }
  100% {
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@keyframes rotate-center {
  0% {
    -webkit-transform: rotate(0);
    transform: rotate(0);
  }
  100% {
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
</style>