<template>
  <div></div>
</template>

<script>
import router from '@/router/index'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'
import $ from 'jquery'
export default {
  setup () {
    const myRoute = useRoute();
    const store = useStore();
    $.ajax({
      url: "https://app459.acapp.acwing.com.cn/api/user/account/acwing/web/receive_code/",
      type: "get",
      data: {
        code: myRoute.query.code,
        state: myRoute.query.state,
      },
      success: resp => {
        if (resp.result === "success") {
          localStorage.setItem("jwt_token", resp.jwt_token)
          store.commit("updateToken", resp.jwt_token)

          store.dispatch("getInfo", {
            success () {
              router.push({ name: "home" });
            }
          })
        } else {
          router.push({ name: "login_view" });
        }
      }
    })
  }
}
</script>

<style scoped>
</style>