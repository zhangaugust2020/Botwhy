<template>
  <div class="card">
    <div class="body-card">
      <div class="card single-post" v-if="$store.state.user.postCount === 0">
        <div class="card-body">有什么新鲜事想告诉大家！快去发帖吧！</div>
      </div>
      <div v-for="post in posts.posts" :key="post.id">
        <div class="card single-post">
          <div class="card-body">
            {{ post.content }}
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
            >
              删除
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
                      @click="delete_a_post(post.id)"
                    >
                      删除
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
  },

  setup (props, context) {
    const store = useStore();
    const delete_a_post = (post_id) => {
      $.ajax({
        url: "http://127.0.0.1:10300/api/user/post/remove",
        type: "post",
        data: {
          id: post_id,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          Modal.getInstance("#delete_notice").hide();
          if (resp.success) {
            context.emit("delete_a_post");
          }
        },
      });
    };
    return {
      delete_a_post,
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
</style>