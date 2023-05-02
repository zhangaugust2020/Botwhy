<template>
  <div class="matchground">
    <div class="row">
      <div class="col-12 timer">
        <div v-if="match_btn_info === '取消'">{{ waiting_time }}s</div>
      </div>
      <div class="col-5">
        <div class="user-photo">
          <img :src="$store.state.user.photo" alt="" />
        </div>
        <div class="user-name">{{ $store.state.user.username }}</div>
      </div>
      <div class="col-2">
        <div class="user-select-bot">
          <select
            v-model="select_bot"
            class="form-select"
            aria-label="Default select example"
          >
            <option selected value="-1">亲自出马</option>
            <option :value="bot.id" v-for="bot in bots" :key="bot.id">
              {{ bot.title }}
            </option>
          </select>
        </div>
      </div>
      <div class="col-5">
        <div class="user-photo">
          <img class="opponent_photo" :src="opponent_photo.value" alt="" />
        </div>
        <div class="user-name">
          {{ opponent_username.value }}
        </div>
      </div>
      <div class="col-12" style="text-align: center">
        <button
          type="button"
          class="btn btn-matching"
          @click="click_match_btn()"
        >
          {{ match_btn_info }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onBeforeMount, computed } from 'vue'
import { useStore } from 'vuex'
import $ from 'jquery'
export default {
  props: {
    game: {
      type: String,
      required: true,
    }
  },
  setup (props) {
    const store = useStore();
    let timer = null;
    let match_btn_info = ref('开始匹配');
    let waiting_time = ref(0);
    let bots = ref([]);
    let select_bot = ref("-1");
    let opponent_photo = ref("");
    let opponent_username = ref("");
    if (props.game === "snake") {
      opponent_photo.value = computed(() => store.state.pk.snake.opponent_photo);
      opponent_username.value = computed(() => store.state.pk.snake.opponent_username);
    } else if (props.game === "reversi") {
      opponent_photo.value = computed(() => store.state.pk.reversi.opponent_photo);
      opponent_username.value = computed(() => store.state.pk.reversi.opponent_username);
    }
    const refresh_bots = () => {
      $.ajax({
        url: `https://app459.acapp.acwing.com.cn/api/user/bot/list/${props.game}`,
        type: "get",
        contentType: "application/json",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            bots.value = resp.data;
          }
        },
      })
    }
    refresh_bots();
    const set_waiting_time = () => {
      timer = setInterval(() => {
        waiting_time.value++;
      }, 1000)
    }
    const back_waiting_time = () => {
      waiting_time.value = 0;
      clearInterval(timer)
    }
    onBeforeMount(() => {
      back_waiting_time();
    })
    const do_snake_start_match = () => {
      store.state.pk.snake.socket.send(JSON.stringify({
        event: "start-matching",
        bot_id: select_bot.value,
      }))
    }
    const do_snake_end_match = () => {
      store.state.pk.snake.socket.send(JSON.stringify({
        event: "stop-matching",
      }))
    }

    const do_reversi_start_match = () => {
      store.state.pk.reversi.socket.send(JSON.stringify({
        event: "start-matching",
        bot_id: select_bot.value,
      }))
    }

    const do_reversi_end_match = () => {
      store.state.pk.reversi.socket.send(JSON.stringify({
        event: "stop-matching",
      }))
    }
    const click_match_btn = () => {
      if (match_btn_info.value === "开始匹配") {
        set_waiting_time();
        match_btn_info.value = "取消";
        if (props.game === "snake") do_snake_start_match();
        else if (props.game === "reversi") do_reversi_start_match();
      } else {
        match_btn_info.value = "开始匹配";
        back_waiting_time();
        if (props.game === "snake") do_snake_end_match();
        else if (props.game === "reversi") do_reversi_end_match();
      }
    }
    return {
      match_btn_info,
      click_match_btn,
      waiting_time,
      bots,
      select_bot,
      opponent_photo,
      opponent_username,
    }
  }
}
</script>


<style scoped>
div.matchground {
  width: 60vw;
  height: 70vh;
  margin: auto;
  margin-top: 100px;
  border-radius: 1vh;
  background-color: rgba(50, 50, 50, 0.5);
}
div.user-photo {
  text-align: center;
}
div.user-photo > img {
  width: 20vh;
  margin-top: 12vh;
  border-radius: 50%;
  border: 1px solid rgb(102, 47, 47);
}
div.user-name {
  text-align: center;
  color: pink;
  font-size: 24px;
  font-weight: bold;
  margin-top: 3vh;
}
.btn-matching {
  background-color: rgb(199, 130, 92);
  margin-top: 12vh;
  color: white;
  font-size: 20px;
  width: 15vh;
  border: none;
}
.btn-matching:hover {
  scale: 1.1;
}
.timer {
  position: absolute;
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  color: white;
  margin-top: 2vh;
  width: 15vh;

  left: 50%;
  transform: translateX(-50%);
}
.user-select-bot {
  margin-top: 20vh;
}
.user-select-bot > select {
  width: 120%;
  margin: 0 auto;
}
</style>
