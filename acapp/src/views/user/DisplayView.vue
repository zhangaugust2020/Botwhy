<template>
  <div class="container body-base">
    <div class="row">
      <div class="col-xs-4 col-sm-4 col-md-3">
        <div class="card" style="margin-top: 20px">
          <div class="card-body">
            <p class="pic">
              <img
                :src="info.user.photo"
                style="
                  width: 100%;
                  cursor: pointer;
                  position: absolute;
                  top: 0;
                  left: 0;
                  height: 100%;
                "
                onerror='this.src="https://cdn.acwing.com/media/user/profile/photo/218581_lg_2ef0569124.jpg"'
              />
            </p>

            <hr />
            <div class="info-username">
              {{ info.user.username }}
            </div>
            <hr />

            <div class="info-reputation">
              <div class="info-reputation-name">帖子数</div>
              <div class="info-reputation-cnt">
                {{ info.posts.length }}
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-xs-8 col-sm-8 col-md-9">
        <div class="card" style="margin-top: 20px">
          <div class="card-header card-header-name">
            {{ info.user.username }}的动态
          </div>
          <UserProfilePosts style="margin: 18px" :posts="info" :isme="false" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useRoute } from 'vue-router'
import { reactive } from 'vue'
import router from "@/router/index";
import { useStore } from 'vuex';
import UserProfilePosts from '../../components/user/UserProfilePosts.vue'
import $ from 'jquery'
export default {
  components: {
    UserProfilePosts,
  },
  setup () {
    const store = useStore();
    const route = useRoute();
    const userId = parseInt(route.params.userId); // 获取路由中的参数
    const info = reactive({
      user: {},
      posts: {},
      count: '',
    })
    if (store.state.user.is_login) {
      $.ajax({
        url: "http://127.0.0.1:10300/api/user/account/otherInfo",
        type: "get",
        data: {
          id: userId,
        },
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success (resp) {
          info.user = resp.data;
        }
      })

      $.ajax({
        url: "http://127.0.0.1:10300/api/user/post/list",
        type: "get",
        data: {
          id: userId,
        },
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success (resp) {
          info.posts = resp.data;
          info.count = resp.data.length;
        }
      })


    } else {
      router.push({ name: "login_view" });
    }

    return {
      info,
    }
  }
}
</script>

<style  scoped>
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
.info-reputation {
  width: 100%;
}
.info-reputation-name {
  color: #99a2aa;
}
.info-reputation-cnt {
  color: #222222;
  margin-left: 5px;
  margin-top: 0.4px;
}
.pic {
  position: relative;
  width: 100%; /*相当于100px*/
  padding-top: 100%; /*相当于100px.这是比较关键的一步，margin和padding是相对于其父元素的宽度的100%*/
}
.card-header-name {
  font-weight: bold;
  font-size: 24px;
  margin: 0 auto;
}
</style>>