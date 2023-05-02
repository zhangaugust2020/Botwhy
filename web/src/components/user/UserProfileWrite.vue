<template>
  <div class="card edit-filed">
    <div class="card-header card-header-name">
      <span class="card-header-name">创建帖子</span>
    </div>
    <div class="card-body">
      <div class="mb-3">
        <label
          for="edit"
          class="form-label"
          style="font-weight: 700; color: gray; user-select: none;"
          >帖子标题</label
        >
        <input
          v-model="title"
          class="form-control"
          id="edit"
          rows="2"
          style="border-color: #5CB85C"
          placeholder="你会取什么标题呢?"
        />
        <label
          for="edit"
          class="form-label"
          style="font-weight: 700; color: gray; user-select: none"
          >帖子内容</label
        >
        <textarea
          v-model="content"
          class="form-control"
          id="edit"
          rows="2"
          style="border-color: #5CB85C"
          placeholder="有什么新鲜事想分享给大家?"
        >
        </textarea>
      </div>

      <button type="button" @click="post_a_post" class="btn btn-primary btn-sm float-end btn-color" data-bs-toggle="modal" data-bs-target="#add_post_notice">
        发贴
      </button>

      <div
        class="modal fade"
        id="add_post_notice"
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
                src="https://augu.oss-cn-beijing.aliyuncs.com/2023/04/28/afd6e57346ba44f4838b4cdf5b8aa6cefire.png"
                alt="提示!"
                style="height: 20px; margin: 0 auto;"
              />
            </div>
            <div
              class="modal-body notice_msg"
              style="margin: 0 auto; color: #838383"
            >
              {{ msg }}
            </div>
            <div class="modal-footer" style="margin: 0 auto">
              <button
                type="button"
                class="btn delete_confrim"
                data-bs-dismiss="modal"
                style="
                  background-color: #5CB85C;
                  color: white;
                  border-style: none;
                "
              >
                确定
              </button>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import $ from "jquery";
import { useStore } from "vuex";
import { useRouter, useRoute } from 'vue-router'
import { computed, inject, ref } from 'vue'
export default {
  name: "UserProfileWrite",
  setup () {
    const store = useStore();
    const router = useRouter();
    const route = useRoute();
    let content = ref("");
    let title = ref("");
    let msg = ref("");
    let route_name = computed(() => route.name);
    const refresh = inject("reload");

    const post_a_post = () => {
      if (content.value === "" || title.value === "") {
        msg.value = "标题或内容为空，不能发帖！！！";
        return false;
      }
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/post/add",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify({
          title: title.value,
          content: content.value,
        }),
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            content.value = "";
            title.value = "";
            msg.value = "帖子发布成功";
            store.commit("updatePostCount", store.state.user.postCount + 1);

            if (route_name.value !== "myspace_posts" && route_name.value !== "myspace_index") {
              router.push({ name: "myspace_posts" });
            } else {
              refresh();
            }
          } else {
            msg.value = "帖子发布失败";
          }
        },
      });
    };
    return {
      content,
      title,
      msg,
      post_a_post,
    };
  },
};
</script>

<style scoped>
.edit-filed {
  margin-top: 10px;
}

button:hover {
  scale: 1.1;
  background-color: #33B4DE;
}

.card-header-name {
  font-weight: bold;
  font-size: 24px;
}

.lable_font{ 
  font-weight: 600;
  font-size: 16px;
}

.btn-color {
  background-color: #00A1D6;
}
</style>