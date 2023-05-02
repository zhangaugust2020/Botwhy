<template>
  <div class="container">
    <div class="card">
      <div class="card-header">
        <span class="card-header-name">我的Bots</span>
        <span>
          <form class="d-flex float-end" role="search" @submit.prevent="bot_search_event">
            <input class="form-control me-2" type="search" placeholder="游戏类型" style="width: 8vw;" v-model="content">
            <button class="btn btn-outline-success" type="submit">Search</button>
          </form>
        </span>
        <button
          class="add-bot float-end"
          data-bs-toggle="modal"
          data-bs-target="#add-bot-button"
        >
          <span>+ Bot</span>
        </button>
        <!-- Modal -->
        <div class="modal fade" id="add-bot-button" tabindex="-1">
          <div class="modal-dialog modal-lg">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title">Create Bot</h5>
                <button
                  type="button"
                  class="btn-close"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                ></button>
              </div>
              <div class="modal-body">
                <div class="mb-2">
                  <label for="bot-title" class="form-label">Bot Title</label>
                  <input
                    v-model="bot_add.title"
                    type="text"
                    class="form-control"
                    id="bot-title"
                    placeholder="名称"
                  />
                </div>
                <div class="mb-2">
                  <label for="description" class="form-label"
                    >Description</label
                  >
                  <textarea
                    v-model="bot_add.description"
                    class="form-control"
                    id="description"
                    placeholder="简介"
                    rows="2"
                  ></textarea>
                </div>
                <div class="row">
                  <div class="mb-2 col-6">
                    <label for="language" class="form-label">Language</label>
                    <select
                      v-model="bot_add.language"
                      class="form-select"
                      aria-label="Default select example"
                    >
                      <option selected>cpp</option>
                      <option>java</option>
                      <option>python3</option>
                      <option>python</option>
                    </select>
                  </div>

                  <div class="mb-2 col-6">
                    <label for="game" class="form-label">Game</label>
                    <select
                      v-model="bot_add.game"
                      class="form-select"
                      aria-label="Default select example"
                    >
                      <option selected>snake</option>
                      <option>reversi</option>
                    </select>
                  </div>
                </div>

                <div class="mb-2">
                  <label for="code" class="form-label">Code</label>
                  <a
                    class="code-template"
                    target="_blank"
                    href="https://www.yuque.com/docs/share/679878f3-95f6-4827-b41f-513852ac97a0?#%20%E3%80%8ABot%E4%BB%A3%E7%A0%81%E6%A8%A1%E6%9D%BF%E3%80%8B"
                    >代码模板</a
                  >
                  <VAceEditor
                    v-model:value="bot_add.content"
                    lang="c_cpp"
                    theme="textmate"
                    style="height: 235px"
                    :options="{
                      fontSize: 16,
                      enableBasicAutocompletion: true,
                      enableSnippets: true,
                      enableLiveAutocompletion: true,
                      showPrintMargin: false,
                      highlightActiveLine: true,
                    }"
                  />
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
                  @click="add_bot_event()"
                >
                  提交
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="card-body">
        <table class="table" style="text-align: center">
          <thead class="table-dark bot-th">
            <tr>
              <th>序号</th>
              <th>游戏类型</th>
              <th>名称</th>
              <th>语言</th>
              <th>创建时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(bot, index) in bots" :key="bot.id" class="bot-tr">
              <td>{{ index + 1 }}</td>
              <td>{{ bot.game }}</td>
              <td>{{ bot.title }}</td>
              <td>{{ bot.language }}</td>
              <td>{{ bot.createtime }}</td>
              <td>
                <button
                  class="update-bot"
                  data-bs-toggle="modal"
                  :data-bs-target="'#update-bot-modal-' + bot.id"
                  :id="'update_button' + bot.id"
                >
                  <span>修改</span>
                </button>
                <button
                  class="remove-bot"
                  data-bs-toggle="modal"
                  data-bs-target="#delete_bot"
                  @click="confirm_bot_id(bot.id)"
                >
                  <span style="color: white">删除</span>
                </button>
                <div
                  class="modal fade"
                  id="delete_bot"
                  tabindex="-1"
                  aria-labelledby="exampleModalLabel"
                  aria-hidden="true"
                >
                  <div class="modal-dialog modal-dialog-centered">
                    <div
                      class="modal-content"
                      style="
                        background-color: white;
                        width: 340px;
                        margin: 0 auto;
                      "
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
                          @click="remove_bot_event()"
                        >
                          删除
                        </button>
                      </div>
                    </div>
                  </div>
                </div>

                <div
                  class="modal fade"
                  :id="'update-bot-modal-' + bot.id"
                  tabindex="-1"
                >
                  <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 class="modal-title">Update Bot</h5>
                        <button
                          type="button"
                          class="btn-close"
                          data-bs-dismiss="modal"
                          aria-label="Close"
                        ></button>
                      </div>
                      <div class="modal-body">
                        <div class="mb-2">
                          <label for="bot-title" class="form-label"
                            >Bot Title</label
                          >
                          <input
                            v-model="bot.title"
                            type="text"
                            class="form-control"
                            id="bot-title"
                            placeholder="名称"
                          />
                        </div>
                        <div class="mb-2">
                          <label for="description" class="form-label"
                            >Description</label
                          >
                          <textarea
                            v-model="bot.description"
                            class="form-control"
                            id="description"
                            placeholder="简介"
                            rows="2"
                          ></textarea>
                        </div>
                        <div class="row">
                          <div class="mb-2 col-6">
                            <label for="language" class="form-label"
                              >Language</label
                            >
                            <select
                              v-model="bot.language"
                              class="form-select"
                              aria-label="Default select example"
                            >
                              <option>cpp</option>
                              <option>java</option>
                              <option>python3</option>
                              <option>python</option>
                            </select>
                          </div>
                          <div class="mb-2 col-6">
                            <label for="game" class="form-label">Game</label>
                            <select
                              v-model="bot.game"
                              class="form-select"
                              aria-label="Default select example"
                            >
                              <option>snake</option>
                              <option>reversi</option>
                            </select>
                          </div>
                        </div>

                        <div class="mb-2">
                          <label for="code" class="form-label">Code</label>
                          <a
                            class="code-template"
                            target="_blank"
                            href="https://www.yuque.com/docs/share/679878f3-95f6-4827-b41f-513852ac97a0?#%20%E3%80%8ABot%E4%BB%A3%E7%A0%81%E6%A8%A1%E6%9D%BF%E3%80%8B"
                            >代码模板</a
                          >
                          <VAceEditor
                            @init="editorInit"
                            v-model:value="bot.content"
                            :options="{
                              fontSize: 16,
                              enableBasicAutocompletion: true,
                              enableSnippets: true,
                              enableLiveAutocompletion: true,
                              showPrintMargin: false,
                              highlightActiveLine: true,
                            }"
                            lang="c_cpp"
                            theme="textmate"
                            style="height: 235px"
                          />
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
                          @click="update_bot_event(bot)"
                        >
                          提交
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import $ from 'jquery'
import { useStore } from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
export default {
  name: "UserBotIndexView",
  components: {
    VAceEditor,
  },
  setup () {
    ace.config.set(
      "basePath",
      "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/");
    let remove_bot_id = ref("");
    let bots = ref([]);
    let error_msg = ref("");
    let content = ref("");
    const bot_add = reactive({
      title: "",
      description: "",
      language: "cpp",
      content: "",
      game: "snake",
    })
    const store = useStore();

    function editorInit () {
      require("ace-builds/src-noconflict/ext-language_tools");
      require("ace-builds/src-noconflict/snippets/c_cpp");
      require("ace-builds/src-noconflict/snippets/java");
      require("ace-builds/src-noconflict/mode-c_cpp");
      require("ace-builds/src-noconflict/mode-java");
      require("ace-builds/src-noconflict/mode-javascript");
      require("ace-builds/src-noconflict/mode-python");
      require("ace-builds/src-noconflict/snippets/less");
      require("ace-builds/src-noconflict/ext-static_highlight");
      require("ace-builds/src-noconflict/ext-beautify");
    }


    const refresh_bots = () => {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/bot/list/all",
        type: "get",
        contentType: "application/json",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            bots.value = resp.data;
          }
        },
      })
    }

    const add_bot_event = () => {
      error_msg.value = "";
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/bot/add",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(bot_add),
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            bot_add.title = "";
            bot_add.description = "";
            bot_add.content = "";
            Modal.getInstance("#add-bot-button").hide();
            store.commit("updateBotCount", store.state.user.botCount + 1);
            refresh_bots();
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
    }

    const update_bot_event = (bot) => {
      error_msg.value = "";
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/bot/update",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(bot),
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            Modal.getInstance("#update-bot-modal-" + bot.id).hide();
            refresh_bots();
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
    }
    const confirm_bot_id = (id) => {
      remove_bot_id.value = id;
    }
    const remove_bot_event = () => {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/bot/remove",
        type: "post",
        data: {
          id: remove_bot_id.value,
        },
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success () {
          Modal.getInstance('#delete_bot').hide();
          store.commit("updateBotCount", store.state.user.botCount - 1);
          refresh_bots();
        },
      })
    }

    const bot_search_event = () => {
      let game = content.value;
      if (!content.value) {
        game = "all";
      }
      $.ajax({
        url: `https://app459.acapp.acwing.com.cn/api/user/bot/list/${game}`,
        type: "get",
        contentType: "application/json",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            bots.value = resp.data;
          }
        },
      })
    }

    refresh_bots();
    return {
      bots,
      bot_add,
      error_msg,
      content,
      add_bot_event,
      update_bot_event,
      remove_bot_event,
      confirm_bot_id,
      editorInit,
      bot_search_event,
    }
  }
}
</script>

<style scoped>
.card-header-name {
  font-weight: bold;
  font-size: 24px;
  margin: 0 auto;
}
.error-msg {
  margin-right: 20px;
  font-size: 16px;
  color: #c3404b;
  font-family: Verdana, Geneva, Tahoma, sans-serif;
}

.add-bot {
  height: 30px;
  width: 60px;
  border: 1px;
  border-radius: 5px;
  background-color: #409eff;
  margin-top: 5px;
  outline: none;
  margin-right: 10px;
}
.update-bot {
  width: 60px;
  border: 1px;
  border-radius: 5px;
  background-color: #b7c5d2;
  outline: none;
}
.remove-bot {
  margin-left: 10px;
  width: 60px;
  border: 1px;
  border-radius: 5px;
  background-color: #dd3545;
  outline: none;
}
button > span {
  font-size: 16px;
  color: rgb(23, 18, 18);
}
button:hover {
  scale: 1.1;
}
.bot-tr:hover {
  background-color: #d7d9da;
}
.bot-th {
  font-family: Cambria, Cochin, Georgia, Times, "Times New Roman", serif;
}
.code-template {
  float: right;
  font-weight: bold;
  text-decoration: none;
  color: rgb(132, 183, 200);
}
</style>