export default {
  state: {
    is_snake_record: false,
    is_reversi_record: false,
    a_steps: [],
    b_steps: [],
    record_loser: "",
    a_username: "",
    a_photo: "",
    b_username: "",
    b_photo: "",
    result: "",
    interval_id: null,
  },
  mutations: {
    updateIsSnakeRecord (state, is_record) {
      state.is_snake_record = is_record;
    },
    updateIsReversiRecord (state, is_record) {
      state.is_reversi_record = is_record;
    },
    updateSteps (state, data) {
      state.a_steps = data.a_steps;
      state.b_steps = data.b_steps;
    },
    updateRecordLoser (state, loser) {
      state.record_loser = loser;
    },
    updateInfo (state, info) {
      state.a_username = info.a_username;
      state.b_username = info.b_username;
      state.a_photo = info.a_photo;
      state.b_photo = info.b_photo;
    },
    updateIntervalId (state, interval_id) {
      state.interval_id = interval_id;
    },
    updateRecordResult (state, result) {
      state.result = result;
    }
  },
  actions: {
  },
  modules: {
  }
}
