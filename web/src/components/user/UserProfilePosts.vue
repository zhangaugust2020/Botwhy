<template>
  <div class="card">
     <div class="card-header card-header-name" v-if="isme">
        <span class="card-header-name">我的动态</span>
        <span>
          <form class="d-flex float-end" role="search" @submit.prevent="post_search_event">
            <input class="form-control me-2" type="search" placeholder="内容" style="width: 8vw;" v-model="content">
            <button class="btn btn-outline-success" type="submit">Search</button>
          </form>
        </span>
      </div>
      <div class="card-header card-header-name" v-else>
        <span class="card-header-name">{{ name }} 的动态</span>
      </div>
    <div class="body-card">
      <div class="card single-post" v-if="$store.state.user.postCount === 0">
        
        <div class="card-body" v-if="isme">
          有什么新鲜事想告诉大家！快去发帖吧！
        </div>
      </div>
      <div v-for="post in posts.posts" :key="post.id" class="card-margin">
        <div class="card single-post">

          <div class="card-header card-title">
            <span class="card-title">{{ post.title }}</span>
            <span class="badge card-time">发帖时间：{{ post.createtime }}</span>
          </div>

          <div class="card-body">
            <p>{{ post.content }}</p>
            <button
              v-if="isme"
              type="button"
              class="btn"
              style="
                background-color: #d9534f;
                color: white;
                border-style: none;
                float: right;
              "
              data-bs-toggle="modal"
              data-bs-target="#delete_notice"
              @click="confirm_delete_post_id(post.id)"
            >
              删除
            </button>

            <button
              v-if="isme"
              type="button"
              class="btn"
              style="
                background-color: #b7c5d2;
                border-style: none;
                float: right;
                outline: none;
                margin-right: 10px;
              "
              data-bs-toggle="modal"
              :data-bs-target="'#update-post-modal-' + post.id"
              :id="'update_button' + post.id"
            >
              <span>修改</span>
            </button>

            <div
              class="modal fade"
              id="delete_notice"
              tabindex="-1"
              aria-labelledby="exampleModalLabel"
              aria-hidden="true"
            >
              <div class="modal-dialog modal-dialog-centered">
                <div
                  class="modal-content"
                  style="background-color: white; width: 340px; margin: 0 auto"
                >
                  <div class="modal-header">
                    <img
                      src="https://cdn.acwing.com/media/article/image/2022/09/02/36510_233881192a-热门.png"
                      alt="警告!"
                      style="height: 20px; margin: 0 auto"
                    />
                  </div>
                  <div
                    class="modal-body notice_msg"
                    style="margin: 0 auto; color: #838383"
                  >
                    你确定删除吗？
                  </div>
                  <div class="modal-footer" style="margin: 0 auto">
                    <button
                      type="button"
                      class="btn delete_cancel"
                      data-bs-dismiss="modal"
                      style="background-color: #f0f0f0; border-style: none"
                    >
                      取消
                    </button>
                    <button
                      type="button"
                      class="btn delete_confrim"
                      style="
                        background-color: #d9534f;
                        color: white;
                        border-style: none;
                      "
                      @click="delete_a_post()"
                    >
                      删除
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <div
              class="modal fade"
              :id="'update-post-modal-' + post.id"
              tabindex="-1"
            >
              <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title">Update Post</h5>
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
                        v-model="post.title"
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
                        v-model="post.content"
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
                      @click="update_post_event(post)"
                    >
                      提交
                    </button>
                  </div>
                </div>
              </div>
            </div>




          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useStore } from "vuex";
import $ from "jquery";
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { ref } from 'vue'
export default {
  name: "UserProfilePosts",
  props: {
    posts: {
      type: Object,
      required: true,
    },
    isme: {
      type: Boolean,
      required: true,
    },
    name: {
      type:  String,
      required: false,
    },
  },

  setup (props, context) {
    let delete_post_id = ref("");
    let error_msg = ref("");
    let content = ref("");
    const store = useStore();

    const confirm_delete_post_id = (id) => {
      delete_post_id.value = id;
    }

    const delete_a_post = () => {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/post/remove",
        type: "post",
        data: {
          id: delete_post_id.value,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          Modal.getInstance("#delete_notice").hide();
          if (resp.success) {
            context.emit("delete_a_post", delete_post_id.value);
          }
        },
      });
    };

    const update_post_event = (post) => {
      error_msg.value = "";
      if (post.title === "" || post.content === "") {
        error_msg.value = "帖子标题或内容为空！";
        setTimeout(() => {
          error_msg.value = "";
        }, 1000)
        return false;
      }
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/post/update",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(post),
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            Modal.getInstance("#update-post-modal-" + post.id).hide();
            error_msg.value = "";
          } else {
            error_msg.value = resp.msg;
            setTimeout(() => {
              error_msg.value = '';
            }, 4000)
          }
        },
        error (resp) {
          error_msg.value = resp.responseJSON.msg;
          setTimeout(() => {
            error_msg.value = '';
          }, 4000)
        }
      })
    };

    const post_search_event = () => {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/post/searchmyList",
        type: "post",
        data: {
          id: store.state.user.id,
          content: content.value,
        },
        headers: {
          "Authorization": "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            context.emit("post_search_event", resp.data);
          }
        }
      })
    };
    

    return {
      delete_a_post,
      confirm_delete_post_id,
      update_post_event,
      post_search_event,
      error_msg,
      content,
    };
  },
};
</script>

<style scoped>
button:hover {
  scale: 1.1;
}
.single-post {
  margin: 15px;
}

.card-header-name {
  font-weight: bold;
  font-size: 24px;
}

.card-title {
  font-size: medium;
  font-weight: bold;
  padding-bottom: 5px;
}

.card-input {
  width: 60px;
}

p {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  font-kerning: auto;
  font-size: 16px;
  font-stretch: normal;
  font-style: normal;
  font-variant: normal;
  font-variant-ligatures: normal;
  font-weight: normal;
}

.card-margin {
  margin-top: 20px;
}

.card-time {
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  font-weight: normal;
  float: right;
  font-size: 14px;
  background-color: #5CB85C;
  color: white;
}

.error-msg {
  color: red;
}
</style>