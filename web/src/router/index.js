import { createRouter, createWebHistory } from 'vue-router'
import SnakeView from '../views/pk/SnakeView'
import ReversiView from '../views/pk/ReversiView'
import ChatNoirView from '../views/pk/ChatNoirView'
import PkIndexView from '../views/pk/PkIndexView'
import RanklistIndexView from '../views/ranklist/RanklistIndexView'
import RecordContentView from '../views/record/RecordContentView'
import RecordIndexView from '../views/record/RecordIndexView'
import UserBotIndexView from '../views/user/bot/UserBotIndexView'
import IndexView from '../views/index/IndexView'
import UserAccountAcWingWebReceiveCodeView from '../views/user/UserAccountAcWingWebReceiveCodeView'
import LoginView from '../views/user/LoginView'
import MyspaceView from '../views/user/myspace/MyspaceView'
import NotFound from '../views/error/NotFound'
import PostView from '../views/user/myspace/PostView'
import UserInfoView from '../views/user/myspace/UserInfoView'
import DisplayView from '../views/user/DisplayView'
import DynamicsView from '../views/dynamic/DynamicsView'
import store from '../store/index'

const routes = [
  {
    path: "/",
    name: "home",
    component: IndexView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/user/account/acwing/web/receive_code/",
    name: "user_account_acwing_web_receive_code",
    component: UserAccountAcWingWebReceiveCodeView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/dynamics/",
    name: "dynamics_view",
    component: DynamicsView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/myspace/",
    component: MyspaceView,
    children: [
      {
        path: 'index/',
        name: "myspace_index",
        component: UserInfoView
      },
      {
        path: 'posts/',
        name: "myspace_posts",
        component: PostView
      },
      {
        path: 'bots/',
        name: "myspace_bots",
        component: UserBotIndexView
      },
      {
        path: 'user/',
        name: "myspace_userinfo",
        component: UserInfoView
      },
    ],
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/game/",
    children: [
      {
        path: '',
        name: "game_index",
        component: PkIndexView,
      },
      {
        path: 'snake',
        name: "snake_view",
        component: SnakeView,
        meta: {
          requestAuth: true,
        }
      },
      {
        path: 'reversi',
        name: "reversi_view",
        component: ReversiView,
        meta: {
          requestAuth: true,
        }
      },
      {
        path: 'chatnoir',
        name: "chat_noir_view",
        component: ChatNoirView,
      }
    ]
  },
  {
    path: "/display/:userId",
    component: DisplayView,
    name: "diaplay_view",
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/",
    component: RecordIndexView,
    name: "record_index",
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/:recordId",
    component: RecordContentView,
    name: "record_content",
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/ranklist/",
    component: RanklistIndexView,
    name: "ranklist_index",
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/login/",
    component: LoginView,
    name: "login_view",
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/404/",
    component: NotFound,
    name: "error",
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/",
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  let flag = 1;
  const jwt_token = localStorage.getItem("jwt_token");

  if (to.name === "login_view" && store.state.user.is_login) {
    next({ name: "home" });
    return;
  }

  if (jwt_token) {
    store.commit("updateToken", jwt_token);
    store.dispatch("getInfo", {
      success () {
      },
      error () {
        store.commit("logout");
        router.push({ name: 'login_view' });
      }
    })
  } else {
    flag = 2;
  }

  if (to.meta.requestAuth && !store.state.user.is_login) {
    if (flag === 1) {
      next();
    } else {
      next({ name: "login_view" });
    }
  } else {
    next();
  }
})

export default router
