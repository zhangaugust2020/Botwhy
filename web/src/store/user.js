import $ from 'jquery'
export default {
  state: {
    id: "",
    username: "",
    rating: "",
    email: "",
    photo: "",
    token: "",
    refresh: "",
    botCount: localStorage.getItem('botCount') ? localStorage.getItem('botCount') : 0,
    postCount: localStorage.getItem('postCount') ? localStorage.getItem('postCount') : 0,
    is_login: localStorage.getItem('jwt_token') ? true : false,
  },
  mutations: {
    updateUser (state, user) {
      state.id = user.id;
      state.username = user.username;
      state.photo = user.photo;
      state.email = user.email;
      state.is_login = user.is_login;
      state.rating = user.rating;
    },
    updateToken (state, token) {
      state.token = token;
    },
    logout (state) {
      state.id = "";
      state.username = "";
      state.photo = "";
      state.token = "";
      state.is_login = false;
    },
    updateBotCount (state, count) {
      state.botCount = count;
    },
    updatePostCount (state, count) {
      state.postCount = count;
    },
    updatePhoto (state, url) {
      state.photo = url;
    },
  },
  actions: {
    login (context, data) {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/account/token",
        type: "post",
        data: {
          username: data.username,
          password: data.password,
        },
        success (resp) {
          if (resp.success) {
            localStorage.setItem("jwt_token", resp.data)
            context.commit("updateToken", resp.data)
            data.success();
          } else {
            data.error();
          }
        },
        error () {
          data.error();
        }
      })
    },
    getInfo (context, data) {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/account/info",
        type: "get",
        headers: {
          Authorization: "Bearer " + context.state.token,
        },
        success (resp) {
          if (resp.success) {
            context.commit("updateUser", {
              ...resp.data,
              is_login: true,
            });
            data.success(resp);
          }
        },
        error () {
          data.error();
        }
      })
    },
    logout (context) {
      localStorage.removeItem("jwt_token");
      localStorage.removeItem("user_id");
      localStorage.removeItem("postCount");
      localStorage.removeItem("botCount");
      context.commit("logout");
    },
    register (context, data) {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/account/register",
        type: "post",
        data: {
          username: data.username,
          password: data.password,
          confirmedPassword: data.confirmed_password,
        },
        success (resp) {
          if (resp.success) {
            data.success(resp);
          } else {
            data.error(resp)
          }
        },
        error (resp) {
          data.error(resp)
        }
      })
    }
  },
  modules: {
  }
}
