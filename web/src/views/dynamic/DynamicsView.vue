<template>
  <div class="container body-base">
    <div class="row">
      <div
        class="card col-xs-12 col-sm-8 col-md-8"
        style="margin: 0 auto; margin-top: 10px; padding: 10px; width:55%;"
        v-if="$store.state.user.is_login"
      >

        <div class="card-header card-header-name">
          <span>新鲜事~</span>
          <span>
            <form class="d-flex float-end" role="search" @submit.prevent="post_search">
              <input class="form-control me-2" type="search" placeholder="用户名" style="width: 8vw;" v-model="username">
              <input class="form-control me-2" type="search" placeholder="内容" style="width: 8vw;" v-model="content">
              <button class="btn btn-outline-success" type="submit">Search</button>
            </form>
          </span>
        </div>

        <div class="card-body">
          <div class="col-xs-2 col-sm-1">
            <img
              @click="to_user_profile($store.state.user.id)"
              class="post-user-avatar"
              :src="$store.state.user.photo"
              style="cursor: pointer"
            />
          </div>
          <div class="col-xs-10 col-sm-11" align="center" style="margin-top: 10px;">
            <lable class="add-post-text-field" data-bs-toggle="modal" data-bs-target="#modal-add-post">{{ $store.state.user.username }}，有什么新鲜事想告诉大家！</lable>
          </div>

          <div
            class="modal fade"
            id="modal-add-post"
            tabindex="-1"
          >
            <div class="modal-dialog modal-dialog-centered">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">Create Post</h5>
                  <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="modal"
                    aria-label="Close"
                  ></button>
                </div>
                <div class="modal-body">
                  <div class="mb-2">
                    <label for="post-title" class="form-label"
                      >Title</label
                    >
                    <input
                      v-model="title_add"
                      type="text"
                      class="form-control"
                      id="post-title"
                      placeholder="标题"
                    />
                  </div>
                  <div class="mb-2">
                    <label for="description" class="form-label"
                      >Content</label
                    >
                    <textarea
                      v-model="content_add"
                      class="form-control"
                      id="description"
                      placeholder="内容"
                      rows="2"
                    ></textarea>
                  </div>
                  

                </div>

                <div class="modal-footer">
                  <div class="error-msg">{{ error_msg }}</div>
                  <button
                    type="button"
                    class="btn btn-secondary"
                    data-bs-dismiss="modal"
                  >
                    取消
                  </button>
                  <button
                    type="button"
                    class="btn btn-primary"
                    @click="add_post"
                  >
                    提交
                  </button>


                </div>
              </div>
            </div>
          </div>


        </div>

        
        
      </div>

      <div v-for="post in postlist" :key="post.id" class="card-margin">
        <div
          class="card col-xs-12 col-sm-8 col-md-8"
          style="margin: 0 auto; padding: 10px; width:56%;"
        >
          <div class="card-header card-title">
            <span class="">{{ post.title }}</span>
          </div>

          <div class="card-body">
            <div class="post-user-info">
              <img
                @click="to_user_profile(post.userId)"
                class="post-user-avatar"
                :src="post.photo"
                style="cursor: pointer"
              />
              <div class="post-username">{{ post.username }}</div>
              <div class="post-time">
                <!-- 发帖时间 {{ post.createtime }} -->
                <span class="badge card-time">发帖时间：{{ post.createtime }}</span>
              </div>
            </div>
          </div>

          <div class="card" style="margin: 0 20px 10px;">
            <div class="card-body body-font">{{ post.content }}</div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import $ from 'jquery'
import { ref } from 'vue'
import router from "@/router/index";
import { useStore } from "vuex";
import { Modal } from 'bootstrap/dist/js/bootstrap'
export default {
  name: "DynamicsView",
  components: {
  },
  setup () {
    const store = useStore();
    let content_add = ref("");
    let title_add = ref("");
    let content = ref("");
    let username = ref("");
    let error_msg = ref("");
    let msg = ref("");
    let postlist = ref([]);

    const allPosts = () => {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/post/allList",
        type: "post",
        success (resp) {
          postlist.value = resp.data;
        },
      })
    }

    allPosts();

    const to_user_profile = (userId) => {
      if (store.state.user.is_login) {
        if (userId === store.state.user.id) {
          router.push({
            name: "myspace_index",
          });
        } else {
          router.push({
            name: "diaplay_view",
            params: {
              userId: userId,
            },
          });
        }
      } else {
        router.push({ name: "login_view" });
      }
    };

    const add_post = () => {
      if (content_add.value === "" || title_add.value === "") {
        error_msg.value = "标题或内容为空，不能发帖！！！";
        return false;
      }
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/post/add",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify({
          title: title_add.value,
          content: content_add.value,
        }),
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            title_add.value = "";
            content_add.value = "";
            store.commit("updatePostCount", store.state.user.postCount + 1);
            allPosts();
            Modal.getInstance('#modal-add-post').hide();
          }
        },
      });
    };

    const post_search = () => {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/post/searchcheckpostlist",
        type: "post",
        data: {
          username: username.value,
          content: content.value,
        },
        headers: {
          "Authorization": "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            username.value = "";
            content.value = "";
            postlist.value = resp.data;
          }
        }
      })
    }

    return {
      postlist,
      username,
      content,
      content_add,
      title_add,
      error_msg,
      msg,
      to_user_profile,
      add_post,
      post_search
    }
  }
}
</script>

<style scoped>

span {
  font-size: medium;
  font-weight: bold;
  padding-bottom: 5px;
}

.card-header-name {
  font-weight: bold;
  font-size: 24px;
}

.post-user-info {
  width: 100%;
  height: 5vh;
}

.post-user-avatar {
  float: left;
  height: 5vh;
  border-radius: 100%;
}

.post-username {
  /* float: left;
  line-height: 5vh;
  margin-left: 1vh;
  font-weight: bold; */
  float: left;
  margin-left: 1vh;
  font-size: 16px;
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  font-weight: bold;
  color: #333333;
}

.post-time {
  float: right;
  font-size: 14px;
  line-height: 5vh;
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
}

.card-margin {
  margin-top: 20px;
}

.body-font {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  font-kerning: auto;
  font-size: 16px;
  font-stretch: normal;
  font-style: normal;
  font-variant: normal;
  font-variant-ligatures: normal;
  font-weight: normal;
}

.card-title {
  background: #FFFFFF;
  font-size: 16px;
  line-height: 2vh;
}

.add-post-text-field {
  background-color: #F0F2F5;
  cursor: pointer;
  height: 45px;
  border-radius: 50px;
  padding: 10px 10px 10px 15px;
  font-size: 20px;
  color: #656768;
}

.add-post-text-field:hover {
  background-color: #e5e6e7;
}

.error-msg {
  color: red;
}

.card-time {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  font-weight: normal;
  float: right;
  font-size: 14px;
  background-color: #5CB85C;
  color: white;
}
</style>