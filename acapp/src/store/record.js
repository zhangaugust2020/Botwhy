export default {
  state: {
    is_record: false,
    a_steps: [],
    b_steps: [],
    record_loser: "",
    a_username: "",
    a_photo: "",
    b_username: "",
    b_photo: "",
  },
  mutations: {
    updateIsRecord (state, is_record) {
      state.is_record = is_record;
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
  },
  actions: {
  },
  modules: {
  }
}
