<template>
  <div class="container body-base">
    <div class="row">
      <div class="col-xs-4 col-sm-4 col-md-3">
        <div class="card" style="margin-top: 20px">
          <div class="card-body">
            <button
              hidden
              data-bs-toggle="modal"
              data-bs-target="#change_avatar"
              id="btn-blank-avatar"
            ></button>
            <p class="pic">
              <img
                :src="$store.state.user.photo"
                style="
                  width: 100%;
                  cursor: pointer;
                  position: absolute;
                  top: 0;
                  left: 0;
                  height: 100%;
                "
                onerror='this.src="https://cdn.acwing.com/media/user/profile/photo/218581_lg_2ef0569124.jpg"'
                @click="change_avatar()"
              />
            </p>

            <hr />
            <div class="info-username">
              {{ $store.state.user.username }}
            </div>
            <hr />

            <div
              class="modal fade"
              id="change_avatar"
              tabindex="-1"
              aria-labelledby="exampleModalLabel"
              aria-hidden="true"
            >
              <div class="modal-dialog modal-dialog-centered">
                <div
                  class="modal-content"
                  style="background-color: white; width: 500px; margin: 0 auto"
                >
                  <div class="modal-header" style="font-weight: bold">
                    Update Avatar
                  </div>
                  <div class="modal-body avatar-info">
                    <img
                      :src="$store.state.user.photo"
                      onerror='this.src="https://cdn.acwing.com/media/user/profile/photo/218581_lg_2ef0569124.jpg"'
                      style="
                        width: 100px;
                        height: 100px;
                        border-radius: 50px;
                        margin-left: 15px;
                      "
                    />

                    <div
                      class="avatar-info-input col-8"
                      style="margin-left: 35px"
                    >
                      <textarea
                        v-model="avatar_url"
                        class="form-control"
                        rows="3"
                        id="edit"
                        placeholder="请输入头像地址"
                      />
                    </div>
                  </div>
                  <div class="avatar-msg">{{ avatar_msg }}</div>
                  <div class="modal-footer" style="margin: 0 auto">
                    <button
                      type="button"
                      class="btn"
                      style="
                        background-color: #198754;
                        border-style: none;
                        color: white;
                      "
                      @click="update_avatar()"
                    >
                      确认
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <div class="info-reputation">
              <router-link
                class="info-reputation-follow"
                :to="{ name: 'myspace_posts' }"
              >
                <div class="info-reputation-name">帖子数</div>
                <div class="info-reputation-cnt">
                  {{ $store.state.user.postCount }}
                </div>
              </router-link>
              <router-link
                class="info-reputation-fans"
                :to="{ name: 'myspace_bots' }"
              >
                <div class="info-reputation-name">Bot数</div>
                <div class="info-reputation-cnt">
                  {{ $store.state.user.botCount }}
                </div>
              </router-link>
            </div>
          </div>
        </div>
        <UserProfileWrite />
      </div>
      <div class="col-xs-8 col-sm-8 col-md-9">
        <div class="card" style="margin-top: 20px">
          <MyspaceNavBar />
          <div class="card-body router-border">
            <router-view v-if="isRouterAlive" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UserProfileWrite from '../../../components/user/UserProfileWrite.vue'
import MyspaceNavBar from '../../../components/navbar/MyspaceNavBar'
import { ref, nextTick, provide } from "vue";
import $ from 'jquery'
import { useStore } from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import router from "@/router/index";
export default {
  components: {
    MyspaceNavBar,
    UserProfileWrite
  },
  setup () {
    const isRouterAlive = ref(true);
    const store = useStore();
    let avatar_msg = ref("");
    let avatar_url = ref("");

    const init = () => {
      $.ajax({
        url: "http://127.0.0.1:10300/api/user/bot/count",
        type: "post",
        data: {
          id: store.state.user.id,
        },
        headers: {
          "Authorization": "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            localStorage.setItem('botCount', resp.data);
            store.commit("updateBotCount", resp.data);
          }
        }
      })

      $.ajax({
        url: "http://127.0.0.1:10300/api/user/post/count",
        type: "post",
        data: {
          id: store.state.user.id,
        },
        headers: {
          "Authorization": "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            localStorage.setItem('postCount', resp.data)
            store.commit("updatePostCount", resp.data);
          }
        }
      })
    }

    if (store.state.user.is_login) {
      init();
    } else {
      router.push({ name: "login_view" });
    }

    const change_avatar = () => {
      $('#btn-blank-avatar').click();
    }
    const update_avatar = () => {
      $.ajax({
        url: "http://127.0.0.1:10300/api/user/account/photo",
        type: "post",
        data: {
          url: avatar_url.value,
        },
        headers: {
          "Authorization": "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            store.commit("updatePhoto", avatar_url.value);
            avatar_url.value = "";
            Modal.getInstance('#change_avatar').hide();
          } else {
            avatar_msg.value = resp.msg;
            setTimeout(() => {
              avatar_msg.value = "";
            }, 2000);
          }
        },
      })
    }

    const reload = () => {
      isRouterAlive.value = false;
      nextTick(() => {
        isRouterAlive.value = true;
      });
    };

    provide("reload", reload);
    return {
      isRouterAlive,
      change_avatar,
      update_avatar,
      avatar_msg,
      avatar_url,
    };
  }
}
</script>

<style scoped>
.info-username {
  text-align: center;
  font-weight: bold;
}
.info-reputation {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  text-align: center;
  font-family: Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif;
  font-size: 13px;
}
.info-reputation-follow {
  width: 50%;
  cursor: pointer;
}
.info-reputation-fans {
  width: 50%;
  cursor: pointer;
}
.info-reputation-name {
  color: #99a2aa;
}
.info-reputation-cnt {
  color: #222222;
}
.info-reputation-follow:hover .info-reputation-name {
  color: #00a1d6;
}
.info-reputation-follow:hover .info-reputation-cnt {
  color: #00a1d6;
}
.info-reputation-fans:hover .info-reputation-name {
  color: #00a1d6;
}
.info-reputation-fans:hover .info-reputation-cnt {
  color: #00a1d6;
}
a {
  text-decoration: none;
}
.avatar-info {
  display: flex;
  align-items: center;
}
button:hover {
  scale: 1.1;
}
.avatar-msg {
  position: absolute;
  top: 65%;
  left: 34%;
  font-size: 14px;
  color: #c3404b;
}
.pic {
  position: relative;
  width: 100%; /*相当于100px*/
  padding-top: 100%; /*相当于100px.这是比较关键的一步，margin和padding是相对于其父元素的宽度的100%*/
}
</style>