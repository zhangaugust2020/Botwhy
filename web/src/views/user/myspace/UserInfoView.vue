<template>
  <div class="container">
    <div class="card">
      <div class="card-header">
        <span class="card-header-name">个人信息</span>
      </div>
      <div class="card-body">
        <div class="row justify-content-md-center">
            <div class="col-10">
                <form @submit.prevent="updateUser">
                    <div class="mb-3 row justify-content-md-center">
                      <label for="username" class="col-sm-2 col-form-label lable_font">用户名：</label>
                      <div class="col-sm-6">
                        <input type="text" class="form-control" id="username" v-model="update_user.username"> 
                      </div>
                    </div>
                    <div class="mb-3 row justify-content-md-center">
                      <label for="email" class="col-sm-2 col-form-label lable_font">邮件地址：</label>
                      <div class="col-sm-6">
                        <input type="email" class="form-control" id="email" v-model="update_user.email">
                      </div>
                    </div>
                    <div class="mb-3 row justify-content-md-center">
                      <label for="rating" class="col-sm-2 col-form-label lable_font">天梯分：</label>
                      <div class="col-sm-6">
                        <input type="text" readonly class="form-control" id="rating" v-model="update_user.rating">
                      </div>
                    </div>
                    <center>
                      <button type="submit" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#update_notice">更新信息</button>
                    </center>
                </form>
            </div>
        </div>


      </div>

      <div
        class="modal fade"
        id="update_notice"
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
                style="
                  background-color: #5CB85C;
                  color: white;
                  border-style: none;
                "
                @click="modalhide()"
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
import { ref, reactive } from 'vue';
import { useStore } from 'vuex';
import { Modal } from 'bootstrap/dist/js/bootstrap'
import $ from 'jquery';
export default {
  components: {
  },
  setup () {
    let user = reactive({});
    const store = useStore();
    let msg = ref("");
    msg.value = "信息更新失败";

    user.value = store.state.user;
    let update_user = JSON.parse(JSON.stringify(user.value));

    const updateUser = () => {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/account/update",
        type: "post",
        data: {
          username: update_user.username,
          email: update_user.email,
        },
        headers: {
          "Authorization": "Bearer " + store.state.user.token,
        },
        success (resp) {
          if (resp.success) {
            store.dispatch("getInfo", {
              success () {
                msg.value = "信息更新成功";
                // router.push({ name: "myspace_index" });
              }
            })
          } else {
            msg.value = "信息更新失败";
          }
        }
      })

    }

    const modalhide = () => {
      Modal.getInstance("#update_notice").hide();
      msg.value = "信息更新失败";
    }

    return {
      update_user,
      msg,
      updateUser,
      modalhide
    }
  }
}
</script>

<style scoped>
.card-header-name {
  font-weight: bold;
  font-size: 24px;
}

.lable_font{ 
  font-weight: 600;
  font-size: 16px;
}

button {
  background: #5CB85C;
}

button:hover {
  scale: 1.1;
}
</style>