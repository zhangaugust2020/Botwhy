<template>
  <UserProfilePosts
    :posts="posts"
    :isme="true"
    @delete_a_post="delete_a_post"
    @post_search_event="post_search_event"
  ></UserProfilePosts>
</template>

<script>
import UserProfilePosts from '../../../components/user/UserProfilePosts.vue'
import { reactive } from 'vue'
import { useStore } from 'vuex'
import $ from 'jquery'
export default {
  components: {
    UserProfilePosts,
  },
  setup () {
    const store = useStore();
    const posts = reactive({});
    const refreshPost = () => {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/post/list",
        type: "post",
        data: {
          id: store.state.user.id,
        },
        headers: {
          "Authorization": "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            posts.posts = resp.data;
            posts.count = resp.data.length;
          }
        }
      })
    }

    const delete_a_post = (post_id) => {
      posts.posts = posts.posts.filter(post => post.id !== post_id);
      posts.count = posts.length;
      store.commit("updatePostCount", store.state.user.postCount - 1);
      refreshPost();
    };

    const post_search_event = (post) => {
      posts.posts = post
    }

    refreshPost();

    return {
      delete_a_post,
      post_search_event,
      posts,
    }
  }
}
</script>

<style scoped>
.card-header-name {
  font-weight: bold;
  font-size: 24px;
}
</style>