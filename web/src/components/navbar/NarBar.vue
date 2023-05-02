<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <button
      class="navbar-toggler"
      type="button"
      data-bs-toggle="collapse"
      data-bs-target="#navbarText"
      aria-controls="navbarText"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="container">
      <router-link class="navbar-brand" :to="{ name: 'home' }"
        >Bot why</router-link
      >
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link
              :class="route_name == 'home' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'home' }"
              >首页</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="
                route_name == 'dynamics_view' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'dynamics_view' }"
              >动态</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="
                route_name == 'game_index' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'game_index' }"
              >游戏大厅</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="
                route_name == 'record_index' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'record_index' }"
              >对局列表</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="
                route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'ranklist_index' }"
              >排行榜</router-link
            >
          </li>
        </ul>

        <ul class="navbar-nav">
          <li class="nav-item dropdown" v-if="$store.state.user.is_login">
            <a
              class="nav-link dropdown-toggle"
              href="#"
              id="navbarDropdown"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              <!-- {{ $store.state.user.username }} -->
              <img class="img-circle" :src="$store.state.user.photo" style="margin: -5px;" width="35px" />
            </a>
            <ul
              class="dropdown-menu"
              aria-labelledby="navbarDropdown"
              style="margin: 0"
            >
              <li>
                <!-- <a
                  class="dropdown-item"
                  :href="'https://app459.acapp.acwing.com.cn/myspace/index/'"
                  >我的空间</a
                > -->
                <router-link
                  class="dropdown-item"
                  :to="{ name: 'myspace_index' }"
                  >我的空间</router-link
                >
              </li>
              <li><hr class="dropdown-divider" /></li>
              <li>
                <a class="dropdown-item" href="#" @click="logout()">退出</a>
              </li>
            </ul>
          </li>

          <li class="nav-item" v-else>
            <router-link
              :class="
                route_name == 'login_view' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'login_view' }"
              >登录 | 注册</router-link
            >
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import { useStore } from 'vuex'
export default {
  name: "NavBar",
  setup () {
    const route = useRoute();
    const store = useStore();
    let route_name = computed(() => route.name);
    const logout = () => {
      store.dispatch("logout");
    }
    return {
      route_name,
      logout,
    }
  },
}
</script>

<style scoped>
.dropdown-menu {
  z-index: 1000;
  float: left;
  min-width: 160px;
  padding: 5px 0;
  font-size: 14px;
  text-align: left;
  list-style: none;
  background-color: #fff;
  background-clip: padding-box;
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 4px;
  -webkit-box-shadow: 0 6px 12px rgb(0 0 0 / 18%);
  box-shadow: 0 6px 12px rgb(0 0 0 / 18%);
}
.navbar {
  position: fixed;
  top: 0;
  right: 0;
  left: 0;
  z-index: 10;
}

.img-circle {
  max-width: none;
  border-radius: 50%;
  vertical-align: middle;
  border: 0;
  width: 35px;
  overflow-clip-margin: content-box;
  overflow: clip;
  cursor: pointer;
}

</style>
