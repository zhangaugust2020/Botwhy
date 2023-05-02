<template>
  <ContentField class="body-base">
    <table class="table" style="user-select: none; text-align: center">
      <thead class="table-dark bot-th">
        <tr>
          <th>左下方</th>
          <th>右上方</th>
          <th>对战结果</th>
          <th>对战时间</th>
          <th>操作</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="record in records" :key="record.record.id" class="bot-tr">
          <td>
            <img
              :src="record.a_photo"
              alt=""
              class="record-user-photo"
              @click="to_user_profile(record.record.aid)"
            />
            &nbsp;
            <span :class="record.record.loser === 'A' ? 'loser' : 'winner'">{{
              record.a_username
            }}</span>
          </td>
          <td>
            <img
              :src="record.b_photo"
              alt=""
              class="record-user-photo"
              @click="to_user_profile(record.record.bid)"
            />
            &nbsp;
            <span :class="record.record.loser === 'B' ? 'loser' : 'winner'">{{
              record.b_username
            }}</span>
          </td>

          <td>{{ record.result }}</td>

          <td>{{ record.record.createTime }}</td>

          <td>
            <button
              class="watch_videotape"
              @click="open_record_content(record.record.id)"
            >
              <span>查看录像</span>
            </button>
          </td>
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
  name: "RecordIndexView",
  components: {
    ContentField,
  },
  setup () {
    const store = useStore();
    let records = ref([]);
    let total_records = 0;
    let current_page = 1;
    let pages = ref([]);

    const click_page = page => {
      if (page === -2) page = current_page - 1;
      else if (page === -1) page = current_page + 1;
      let max_pages = parseInt(Math.ceil(total_records / 10));
      if (page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    }
    const update_page = () => {
      let max_pages = parseInt(Math.ceil(total_records / 10));
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
        url: "http://127.0.0.1:10300/api/record/page",
        type: "get",
        data: {
          page
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          records.value = resp.data.records;
          total_records = resp.data.recordsCount;
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
    const stringTo2D = map => {
      let g = [];
      for (let i = 0, k = 0; i < 13; i++) {
        let line = [];
        for (let j = 0; j < 14; j++, k++) {
          if (map[k] === '0') line.push(0);
          else line.push(1);
        }
        g.push(line);
      }
      return g;
    }
    const open_record_content = recordId => {
      for (const record of records.value) {
        if (record.record.id === recordId) {
          store.commit("updateIsRecord", true);
          localStorage.setItem("record-map", JSON.stringify(stringTo2D(record.record.map)));

          store.commit("updateRecordGamemap", stringTo2D(record.record.map))

          store.commit("updateSteps", {
            a_steps: record.record.asteps,
            b_steps: record.record.bsteps,
          })
          store.commit("updateRecordLoser", record.record.loser);
          store.commit("updateInfo", {
            a_username: record.a_username,
            a_photo: record.a_photo,
            b_username: record.b_username,
            b_photo: record.b_photo,
          })

          router.push({
            name: "record_content",
            params: {
              recordId,
            }
          })
          break;
        }
      }
    }

    return {
      records,
      to_user_profile,
      open_record_content,
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
img.record-user-photo {
  width: 4vh;
  border-radius: 50%;
}
.record-user-username {
  font-weight: bold;
}
img:hover {
  cursor: pointer;
}
.winner {
  color: #edce7d;
  font-weight: bold;
}
.loser {
  color: #859aa6;
  font-weight: bold;
}
</style>