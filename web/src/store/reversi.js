export default {
  state: {
    gameObject: null,
    status: "matching", // matching 匹配, playing 对战, chating聊天
    socket: null,
    gamemap: null,
    round: false, // 谁的回合
    opponent_username: "",
    opponent_photo: "",
    opponent_rating: null,
    opponent_ready_status: "未准备",
    a_cnt: 2,
    b_cnt: 2,
    a_id: 0,
    b_id: 0,
    loser: "none", //none, all, A, B
    history: [],
  },
  mutations: {
    updateReversiSocket (state, socket) {
      state.socket = socket;
    },
    updateReversiGameObject (state, gameObject) {
      state.gameObject = gameObject;
    },
    updateReversiStatus (state, status) {
      state.status = status;
    },
    updateReversiOpponent (state, opponent) {
      state.opponent_username = opponent.username;
      state.opponent_photo = opponent.photo;
      state.opponent_rating = opponent.rating;
    },
    updateReversiLoser (state, loser) {
      state.loser = loser;
    },
    updateReversiGame (state, game) {
      state.gamemap = game.map;
      state.a_id = game.a_id;
      state.b_id = game.b_id;
    },
    updateReversiRound (state, round) {
      state.round = round;
    },
    updateReversiGameMap (state, map) {
      state.gamemap = map;
    },
    updateReversiReadyStatus (state, status) {
      state.opponent_ready_status = status;
    },
    updateReversiHistory (state, history) {
      state.history = history;
    },
    updateReversiCount (state, cnt) {
      state.a_cnt = cnt[0];
      state.b_cnt = cnt[1];
    }
  },
  actions: {
  },
  modules: {
  }
}
