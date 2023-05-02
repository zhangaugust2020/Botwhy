export default {
  state: {
    status: "matching", // matching 匹配, playing 对战, chating聊天
    direction: -1,
    socket: null,
    opponent_username: "",
    opponent_photo: "",
    opponent_rating: null,
    opponent_ready_status: "未准备",
    self_rating: null,
    gamemap: localStorage.getItem("record-map") ? JSON.parse(localStorage.getItem("record-map")) : null,
    a_id: 0,
    a_sx: 0,
    a_sy: 0,
    b_id: 0,
    b_sx: 0,
    b_sy: 0,
    gameObject: null,
    loser: "none", //none, all, A, B
    history: [],
  },
  mutations: {
    updateSocket (state, socket) {
      state.socket = socket;
    },
    updateOpponent (state, opponent) {
      state.opponent_username = opponent.username;
      state.opponent_photo = opponent.photo;
      state.opponent_rating = opponent.rating;
    },
    updateReadyStatus (state, status) {
      state.opponent_ready_status = status;
    },
    updateSelfRating (state, rating) {
      state.self_rating = rating;
    },
    updateStatus (state, status) {
      state.status = status;
    },
    updateGame (state, game) {
      state.gamemap = game.map;
      state.a_id = game.a_id;
      state.a_sx = game.a_sx;
      state.a_sy = game.a_sy;
      state.b_id = game.b_id;
      state.b_sx = game.b_sx;
      state.b_sy = game.b_sy;
    },
    updateGameObject (state, gameObject) {
      state.gameObject = gameObject;
    },
    updateLoser (state, loser) {
      state.loser = loser;
    },
    updateRecordGamemap (state, gamemap) {
      state.gamemap = gamemap;
    },
    updateHistory (state, history) {
      state.history = history;
    },
    updateDirection (state, d) {
      state.direction = d;
    }
  },
  actions: {
  },
  modules: {
  }
}
