<template>
  <ContentField class="body-base">
    <table class="table" style="user-select: none; text-align: center">
      <thead class="table-dark bot-th">
        <tr>
          <th>玩家</th>
          <th>天梯分</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="user in users" :key="user.id" class="bot-tr">
          <td>
            <img
              :src="user.photo"
              alt=""
              class="user-photo"
              @click="to_user_profile(user.id)"
            />
            &nbsp;
            <span class="user-name">{{ user.username }}</span>
          </td>
          <td>{{ user.rating }}</td>
        </tr>
      </tbody>
    </table>
    <nav aria-label="..." style="float: right">
      <ul class="pagination">
        <li class="page-item">
          <a
            class="page-link"
            href="#"
            aria-label="Previous"
            @click="click_page(-2)"
          >
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>

        <li
          :class="'page-item ' + page.is_active"
          v-for="page in pages"
          :key="page.number"
          @click="click_page(page.number)"
        >
          <a class="page-link" href="#">{{ page.number }}</a>
        </li>

        <li class="page-item">
          <a
            class="page-link"
            href="#"
            aria-label="Next"
            @click="click_page(-1)"
          >
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField'
import $ from 'jquery'
import { useStore } from 'vuex'
import { ref } from 'vue'
import router from "@/router/index";
export default {
  name: "userIndexView",
  components: {
    ContentField,
  },
  setup () {
    const store = useStore();
    let users = ref([]);
    let total_users = 0;
    let current_page = 1;
    let pages = ref([]);

    const click_page = page => {
      if (page === -2) page = current_page - 1;
      else if (page === -1) page = current_page + 1;
      let max_pages = parseInt(Math.ceil(total_users / 10));
      if (page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    }
    const update_page = () => {
      let max_pages = parseInt(Math.ceil(total_users / 10));
      let new_pages = [];
      for (let i = current_page - 2; i <= current_page + 2; i++) {
        if (i >= 1 && i <= max_pages) {
          new_pages.push({
            number: i,
            is_active: i === current_page ? "active" : "",
          });
        }
      }
      pages.value = new_pages;
    }
    const pull_page = page => {
      current_page = page;
      $.ajax({
        url: "http://127.0.0.1:10300/api/ranklist/page",
        type: "get",
        data: {
          page
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          users.value = resp.data.users;
          total_users = resp.data.usersCount;
          update_page();
        },
        error (resp) {
          console.log(resp);
        }
      })
    }
    const to_user_profile = userId => {
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
    pull_page(current_page);

    return {
      users,
      to_user_profile,
      pages,
      click_page
    }
  }
}
</script>

<style scoped>
.bot-tr:hover {
  background-color: #d7d9da;
}
button:hover {
  scale: 1.1;
}
.watch_videotape {
  border: 1px;
  border-radius: 5px;
  background-color: #b7c5d2;
  outline: none;
}
img.user-photo {
  width: 4vh;
  border-radius: 50%;
}
.user-name {
  font-weight: bold;
}
img:hover {
  cursor: pointer;
}
</style>