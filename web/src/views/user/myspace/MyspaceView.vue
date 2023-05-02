<template>
  <div class="container body-base">
    <div class="row">
      <div class="col-xs-4 col-sm-4 col-md-3">
        <UserProfileInfo />
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
import UserProfileInfo from '../../../components/user/UserProfileInfo.vue'
import MyspaceNavBar from '../../../components/navbar/MyspaceNavBar'
import { ref, nextTick, provide } from "vue";
import $ from 'jquery'
import { useStore } from 'vuex'
import router from "@/router/index";
export default {
  components: {
    MyspaceNavBar,
    UserProfileWrite,
    UserProfileInfo,
  },
  setup () {
    const isRouterAlive = ref(true);
    const store = useStore();

    const init = () => {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/bot/count",
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
        url: "https://app459.acapp.acwing.com.cn/api/user/post/count",
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

    const reload = () => {
      isRouterAlive.value = false;
      nextTick(() => {
        isRouterAlive.value = true;
      });
    };

    provide("reload", reload);
    return {
      isRouterAlive,
    };
  }
}
</script>

<style scoped>
</style>