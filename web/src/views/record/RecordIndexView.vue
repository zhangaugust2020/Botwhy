<template>
  <div class="container content-field">
    <div class="card">
      <div class="card-header card-header-name">
        <span>对局列表</span>
        <span>
          <form class="d-flex float-end" role="search" @submit.prevent="record_search_event">
            <input class="form-control me-2" type="search" placeholder="游戏类型" style="width: 8vw;" v-model="game">
            <input class="form-control me-2" type="search" placeholder="用户名" style="width: 8vw;" v-model="username">
            <button class="btn btn-outline-success" type="submit">Search</button>
          </form>
        </span>
      </div>
      <div class="card-body">
        
        <table class="table" style="user-select: none; text-align: center">
          <thead class="table-dark bot-th">
            <tr>
              <th>#</th>
              <th>游戏类型</th>
              <th>对战双方</th>
              <th>对战结果</th>
              <th>对战时间</th>
              <th>操作</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="record in records" :key="record.record.id" class="bot-tr">
              <td>{{ record.rank }}</td>
              <td>{{ record.record.game }}</td>
              <td>
                <img
                  :src="record.a_photo"
                  alt=""
                  class="record-user-photo"
                  @click="to_user_profile(record.record.aid)"
                />
                &nbsp;
                <span
                  :class="record.record.loser === 'A' ? 'loser' : 'winner'"
                  style="margin-right: 2vw"
                  >{{ record.a_username }}</span
                >
                <span style="font-weight: bold; color: #556771">VS</span>
                <img
                  style="margin-left: 2vw"
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

              <td>{{ record.record.createtime }}</td>

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
      </div>
    </div>
  </div>


  <!-- <ContentField class="body-base">
    <table class="table" style="user-select: none; text-align: center">
      <thead class="table-dark bot-th">
        <tr>
          <th>游戏类型</th>
          <th>对战双方</th>
          <th>对战结果</th>
          <th>对战时间</th>
          <th>操作</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="record in records" :key="record.record.id" class="bot-tr">
          <td>{{ record.record.game }}</td>
          <td>
            <img
              :src="record.a_photo"
              alt=""
              class="record-user-photo"
              @click="to_user_profile(record.record.aid)"
            />
            &nbsp;
            <span
              :class="record.record.loser === 'A' ? 'loser' : 'winner'"
              style="margin-right: 2vw"
              >{{ record.a_username }}</span
            >
            <span style="font-weight: bold; color: #556771">VS</span>
            <img
              style="margin-left: 2vw"
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
  </ContentField> -->


</template>

<script>
// import ContentField from '../../components/ContentField'
import $ from 'jquery'
import { useStore } from 'vuex'
import { ref } from 'vue'
import router from "@/router/index"
export default {
  name: "RecordIndexView",
  components: {
    // ContentField,
  },
  setup () {
    const store = useStore();
    let records = ref([]);
    let total_records = 0;
    let current_page = 1;
    let pages = ref([]);
    let username = ref("");
    let game = ref("");

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
        url: "https://app459.acapp.acwing.com.cn/api/record/page",
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
          records.value = resp.data.records.map((record, index) => ({
            ...record,
            rank: (current_page - 1) * 10 + index + 1,
          }));
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
          let game = record.record.game;
          if (game === "snake") {
            store.commit("updateIsSnakeRecord", true);
            localStorage.setItem("record-snake-map", JSON.stringify(stringTo2D(record.record.map)));
            store.commit("updateSnakeRecordGamemap", stringTo2D(record.record.map))
            store.commit("updateSteps", {
              a_steps: record.record.asteps,
              b_steps: record.record.bsteps,
            })
          } else if (game === "reversi") {
            store.commit("updateIsReversiRecord", true);
            store.commit("updateSteps", {
              a_steps: convert_steps(record.record.asteps),
              b_steps: convert_steps(record.record.bsteps),
            })
            store.commit("updateRecordResult", record.result);
          }

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

    const convert_steps = (steps) => {
      let new_steps = [];
      let t = steps.split(' ');
      for (let i = 0; i < t.length - 1; i++) {
        let [x, y] = t[i].split(',');
        new_steps.push([parseInt(x), parseInt(y)]);
      }
      return new_steps;
    }

    const record_search_event = () => {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/record/search/page",
        type: "get",
        data: {
          username: username.value,
          game: game.value,
          page: "1",
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          username.value = "";
          game.value = "";
          records.value = resp.data.records;
          total_records = resp.data.recordsCount;
          records.value = resp.data.records.map((record, index) => ({
            ...record,
            rank: (current_page - 1) * 10 + index + 1,
          }));
          update_page();
        },
        error (resp) {
          console.log(resp);
        }
      })
    }

    return {
      records,
      to_user_profile,
      open_record_content,
      pages,
      click_page,
      username,
      record_search_event,
      game
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

.card-header-name {
  font-weight: bold;
  font-size: 24px;
}

div.content-field {
  margin-top: 85px;
}
</style>