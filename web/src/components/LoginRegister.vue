<template>
  <div class="box container">
    <div class="pre-box">
      <h1>WELCOME</h1>
      <p>JOIN US!</p>
      <div class="img-box">
        <img
          src="https://augu.oss-cn-beijing.aliyuncs.com/2023/04/28/f91efc21fc5e46f3b858df1a56993b48waoku.jpg"
          alt=""
        />
      </div>
    </div>
    <div class="register-form">
      <div class="title-box">
        <h1 class="form-h1-text bounce-top">注册</h1>
      </div>
      <div class="register-error-msg">{{ register_error_msg }}</div>
      <div class="input-box">
        <input v-model="register_username" type="text" placeholder="用户名" />
        <input v-model="register_password" type="password" placeholder="密码" />
        <input
          v-model="confirmed_password"
          type="password"
          placeholder="确认密码"
        />
      </div>
      <div class="btn-box">
        <button @click="register()">注册</button>
        <p @click="mySwitch()">已有账号?去登录</p>
      </div>
    </div>

    <div class="login-form">
      <div class="title-box">
        <h1 class="form-h1-text bounce-top">登录</h1>
      </div>
      <div class="login-error-msg">{{ login_error_msg }}</div>
      <div class="input-box">
        <input v-model="login_username" type="text" placeholder="用户名" />
        <input v-model="login_password" type="password" placeholder="密码" />
      </div>
      <div class="btn-box">
        <button @click="login()">登录</button>
        <p @click="mySwitch()">没有账号?去注册</p>
      </div>
      <!-- <div class="thrid-party-login">
        <p>
          更多登录方式：
          <img
            @click="acwing_login()"
            src="https://cdn.acwing.com/media/article/image/2022/09/06/1_32f001fd2d-acwing_logo.png"
            alt="acwing"
          />
        </p>
      </div> -->
    </div>
  </div>
</template>

<script>
import $ from 'jquery'
import { ref } from 'vue'
import { useStore } from 'vuex'
import router from '../router/index'
export default {
  setup () {
    const store = useStore();
    let login_error_msg = ref("");
    let register_error_msg = ref("");
    let login_username = ref("");
    let login_password = ref("");
    let register_username = ref("");
    let register_password = ref("");
    let confirmed_password = ref("");
    let flag = true;
    const mySwitch = () => {
      if (flag) {
        $(".pre-box").css("transform", "translateX(100%)");
        $(".pre-box").css("background-color", "#c9e0ed");
        $(".img-box > img").attr("src", "https://augu.oss-cn-beijing.aliyuncs.com/2023/04/28/4d8f86ff41dc485f9efbac31ebb0651cwuwu.jpg");
      }
      else {
        $(".pre-box").css("transform", "translateX(0%)");
        $(".pre-box").css("background-color", "#edd4dc");
        $(".img-box > img").attr("src", "https://augu.oss-cn-beijing.aliyuncs.com/2023/04/28/f91efc21fc5e46f3b858df1a56993b48waoku.jpg");
      }
      flag = !flag;
    }
    const login = () => {
      store.dispatch("login", {
        username: login_username.value,
        password: login_password.value,
        success () {
          // 登录成功 获取信息
          store.dispatch("getInfo", {
            success () {
              // localStorage.setItem("user_id", resp.data.id)
              router.push({ name: "home" });
            }
          })
        },
        error () {
          login_error_msg.value = "用户名或密码错误";
          setTimeout(() => {
            login_error_msg.value = '';
          }, 2000);
        }
      })
    }
    const register = () => {
      store.dispatch("register", {
        username: register_username.value,
        password: register_password.value,
        confirmed_password: confirmed_password.value,
        success () {
          store.dispatch("login", {
            username: register_username.value,
            password: register_password.value,
            success () {
              // 登录成功 获取信息
              store.dispatch("getInfo", {
                success () {
                  router.push({ name: "home" });
                }
              })
            }
          })
        },
        error (resp) {
          register_error_msg.value = resp.msg;
          setTimeout(() => {
            register_error_msg.value = '';
          }, 2000);
        }
      })
    }

    const acwing_login = () => {
      $.ajax({
        url: "https://app459.acapp.acwing.com.cn/api/user/account/acwing/web/apply_code/",
        typt: "get",
        success: resp => {
          if (resp.result === "success") {
            window.location.replace(resp.apply_code_url);
          }
        }
      })
    }
    return {
      mySwitch,
      login,
      register,
      login_error_msg,
      register_error_msg,
      login_username,
      login_password,
      register_username,
      register_password,
      confirmed_password,
      acwing_login
    }
  }
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
.register-error-msg {
  position: absolute;
  top: 40%;
  font-size: 20px;
  color: #c3404b;
  writing-mode: tb-rl;
  font-family: cursive;
}
.login-error-msg {
  position: absolute;
  top: 40%;
  /* left: 69%; */
  writing-mode: tb-rl;
  font-size: 20px;
  color: #c3404b;
  font-family: cursive;
}
.form-h1-text {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
/* 去除input的轮廓 */
input {
  outline: none;
}
.box {
  height: 100%;
}
.box {
  overflow-x: hidden;
  display: flex;
  background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
}
span {
  position: absolute;
  z-index: 0;
  bottom: 0;
  border-radius: 50%;
  /* 径向渐变 */
  background: radial-gradient(
    circle at 72% 28%,
    #fff 3px,
    #ff7edf 8%,
    #5b5b5b,
    #aad7f9 100%
  );
  /* 动画 */
  animation: myMove 4s linear infinite;
}

@keyframes myMove {
  0% {
    transform: translateY(0%);
    opacity: 1;
  }

  50% {
    transform: translate(10%, -1000%);
  }

  75% {
    transform: translate(-20%, -1200%);
  }

  99% {
    opacity: 0.9;
  }

  100% {
    transform: translateY(-1800%) scale(1.5);
    opacity: 0;
  }
}

.box {
  width: 1050px;
  height: 600px;
  display: flex;
  position: relative;
  top: 50px;
  z-index: 2;
  margin: auto;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.6);
  box-shadow: 2px 1px 19px rgba(0, 0, 0, 0.1);
}

.pre-box {
  width: calc(1050px / 2);
  height: 100%;
  position: absolute;
  left: 0;
  top: 0;
  z-index: 99;
  border-radius: 4px;
  background-color: #edd4dc;
  box-shadow: 2px 1px 19px rgba(0, 0, 0, 0.1);
  transition: 0.5s ease-in-out;
}

.pre-box h1 {
  margin-top: 150px;
  text-align: center;
  letter-spacing: 5px;
  color: white;
  user-select: none;
  text-shadow: 4px 4px 3px rgba(0, 0, 0, 0.1);
}

.pre-box p {
  height: 30px;
  line-height: 30px;
  text-align: center;
  margin: 20px 0;
  user-select: none;
  font-weight: bold;
  color: white;
  text-shadow: 4px 4px 3px rgba(0, 0, 0, 0.1);
}

.img-box {
  width: 200px;
  height: 200px;
  margin: 20px auto;
  border-radius: 50%;
  user-select: none;
  overflow: hidden;
  box-shadow: 4px 4px 3px rgba(0, 0, 0, 0.1);
}

.img-box img {
  width: 100%;
  transition: 0.5s;
}

.login-form,
.register-form {
  flex: 1;
  height: 100%;
}

.title-box {
  height: 300px;
  line-height: 500px;
}

.title-box h1 {
  text-align: center;
  color: white;
  user-select: none;
  letter-spacing: 5px;
  text-shadow: 4px 4px 3px rgba(0, 0, 0, 0.1);
}

.input-box {
  display: flex;
  flex-direction: column;
  align-items: center;
}

input {
  width: 60%;
  height: 40px;
  margin-bottom: 20px;
  text-indent: 10px;
  border: 1px solid #fff;
  background-color: rgba(255, 255, 255, 0.3);
  border-radius: 120px;
  backdrop-filter: blur(10px);
}

input:focus::placeholder {
  opacity: 0;
}
h1 {
  display: block;
  font-size: 2em;
  margin-block-start: 0.67em;
  margin-block-end: 0.67em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
  font-weight: bold;
}
.btn-box {
  display: flex;
  justify-content: center;
}

button {
  width: 100px;
  height: 30px;
  margin: 0 7px;
  line-height: 30px;
  border: none;
  border-radius: 4px;
  background-color: #69b3f0;
  color: white;
}

button:hover {
  cursor: pointer;
  opacity: 0.8;
  -webkit-animation: wobble-hor-bottom 0.8s both;
  animation: wobble-hor-bottom 0.8s both;
}

.btn-box p {
  height: 30px;
  line-height: 30px;
  user-select: none;
  font-size: 14px;
  color: #426277;
}

.btn-box p:hover {
  cursor: pointer;
  border-bottom: 1px solid #426277;
}

.bounce-top {
  -webkit-animation: bounce-top 0.9s both;
  animation: bounce-top 0.9s both;
}

@-webkit-keyframes bounce-top {
  0% {
    -webkit-transform: translateY(-45px);
    transform: translateY(-45px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
    opacity: 1;
  }
  24% {
    opacity: 1;
  }
  40% {
    -webkit-transform: translateY(-24px);
    transform: translateY(-24px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  65% {
    -webkit-transform: translateY(-12px);
    transform: translateY(-12px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  82% {
    -webkit-transform: translateY(-6px);
    transform: translateY(-6px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  93% {
    -webkit-transform: translateY(-4px);
    transform: translateY(-4px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  25%,
  55%,
  75%,
  87% {
    -webkit-transform: translateY(0px);
    transform: translateY(0px);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  100% {
    -webkit-transform: translateY(0px);
    transform: translateY(0px);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
    opacity: 1;
  }
}
@keyframes bounce-top {
  0% {
    -webkit-transform: translateY(-45px);
    transform: translateY(-45px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
    opacity: 1;
  }
  24% {
    opacity: 1;
  }
  40% {
    -webkit-transform: translateY(-24px);
    transform: translateY(-24px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  65% {
    -webkit-transform: translateY(-12px);
    transform: translateY(-12px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  82% {
    -webkit-transform: translateY(-6px);
    transform: translateY(-6px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  93% {
    -webkit-transform: translateY(-4px);
    transform: translateY(-4px);
    -webkit-animation-timing-function: ease-in;
    animation-timing-function: ease-in;
  }
  25%,
  55%,
  75%,
  87% {
    -webkit-transform: translateY(0px);
    transform: translateY(0px);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
  }
  100% {
    -webkit-transform: translateY(0px);
    transform: translateY(0px);
    -webkit-animation-timing-function: ease-out;
    animation-timing-function: ease-out;
    opacity: 1;
  }
}
@-webkit-keyframes wobble-hor-bottom {
  0%,
  100% {
    -webkit-transform: translateX(0%);
    transform: translateX(0%);
    -webkit-transform-origin: 50% 50%;
    transform-origin: 50% 50%;
  }
  15% {
    -webkit-transform: translateX(-30px) rotate(-6deg);
    transform: translateX(-30px) rotate(-6deg);
  }
  30% {
    -webkit-transform: translateX(15px) rotate(6deg);
    transform: translateX(15px) rotate(6deg);
  }
  45% {
    -webkit-transform: translateX(-15px) rotate(-3.6deg);
    transform: translateX(-15px) rotate(-3.6deg);
  }
  60% {
    -webkit-transform: translateX(9px) rotate(2.4deg);
    transform: translateX(9px) rotate(2.4deg);
  }
  75% {
    -webkit-transform: translateX(-6px) rotate(-1.2deg);
    transform: translateX(-6px) rotate(-1.2deg);
  }
}
@keyframes wobble-hor-bottom {
  0%,
  100% {
    -webkit-transform: translateX(0%);
    transform: translateX(0%);
    -webkit-transform-origin: 50% 50%;
    transform-origin: 50% 50%;
  }
  15% {
    -webkit-transform: translateX(-30px) rotate(-6deg);
    transform: translateX(-30px) rotate(-6deg);
  }
  30% {
    -webkit-transform: translateX(15px) rotate(6deg);
    transform: translateX(15px) rotate(6deg);
  }
  45% {
    -webkit-transform: translateX(-15px) rotate(-3.6deg);
    transform: translateX(-15px) rotate(-3.6deg);
  }
  60% {
    -webkit-transform: translateX(9px) rotate(2.4deg);
    transform: translateX(9px) rotate(2.4deg);
  }
  75% {
    -webkit-transform: translateX(-6px) rotate(-1.2deg);
    transform: translateX(-6px) rotate(-1.2deg);
  }
}

.thrid-party-login {
  margin-top: 2vh;
  text-align: center;
  font-size: 14px;
  user-select: none;
}
.thrid-party-login > p > img {
  height: 4vh;
  margin-right: 1vw;
  cursor: pointer;
}
</style>