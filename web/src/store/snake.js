export default {
  state: {
    status: "matching", // matching 匹配, playing 对战, chating聊天
    direction: -1,
    socket: null,
    opponent_username: "",
    opponent_photo: "",
    opponent_rating: null,
    opponent_ready_status: "未准备",
    gamemap: localStorage.getItem("record-snake-map") ? JSON.parse(localStorage.getItem("record-snake-map")) : null,
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
    updateSnakeSocket (state, socket) {
      state.socket = socket;
    },
    updateSnakeOpponent (state, opponent) {
      state.opponent_username = opponent.username;
      state.opponent_photo = opponent.photo;
      state.opponent_rating = opponent.rating;
    },
    updateSnakeReadyStatus (state, status) {
      state.opponent_ready_status = status;
    },
    updateSnakeStatus (state, status) {
      state.status = status;
    },
    updateSnakeGame (state, game) {
      state.gamemap = game.map;
      state.a_id = game.a_id;
      state.a_sx = game.a_sx;
      state.a_sy = game.a_sy;
      state.b_id = game.b_id;
      state.b_sx = game.b_sx;
      state.b_sy = game.b_sy;
    },
    updateSnakeGameObject (state, gameObject) {
      state.gameObject = gameObject;
    },
    updateSnakeLoser (state, loser) {
      state.loser = loser;
    },
    updateSnakeRecordGamemap (state, gamemap) {
      state.gamemap = gamemap;
    },
    updateSnakeHistory (state, history) {
      state.history = history;
    },
    updateSnakeDirection (state, d) {
      state.direction = d;
    }
  },
  actions: {
  },
  modules: {
  }
}
