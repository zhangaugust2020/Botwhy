<template>
  <ContentField class="body-base">
    <div class="row">
      <div
        class="alert alert-info alert-dismissible fade show col-11"
        role="alert"
        style="margin-left: 3.5vw"
      >
        欢迎来到 <strong>Bot why</strong>！你可以点击<router-link
          :to="{ name: 'game_index' }"
          >游戏大厅</router-link
        >来选择你感兴趣游戏的进行游玩~
        <button
          type="button"
          class="btn-close small"
          data-bs-dismiss="alert"
          aria-label="Close"
        ></button>
      </div>
    </div>
    <div class="row">
      <div class="col-11 desc">
        <div class="desc-title">Bot why是什么？</div>

        <div class="desc-simple-content">
          &nbsp;&nbsp;&nbsp;&nbsp;Bot why是<span>在线的程序对抗平台</span>，AI
          可以根据已有的游戏规则进行比赛一决胜负!
        </div>
        <hr />
        <div class="desc-title">我不知道怎么用 Bot why啊？</div>
        <div class="desc-how-ans">
          <div>
            &nbsp;&nbsp;&nbsp;&nbsp;1.
            <router-link :to="{ name: 'login_view' }">注册</router-link> Super
            Of Bots 账户
          </div>
          <div>
            &nbsp;&nbsp;&nbsp;&nbsp;2. 按照<a
              href="https://www.yuque.com/docs/share/52aa60de-45d0-4fcd-9ace-f2e5699a8ad7?# 《游戏说明》"
              target="_blank"
              >游戏说明</a
            >编写自己的
            <span
              ><a
                href="https://www.yuque.com/docs/share/679878f3-95f6-4827-b41f-513852ac97a0?#%20%E3%80%8ABot%E4%BB%A3%E7%A0%81%E6%A8%A1%E6%9D%BF%E3%80%8B"
                target="_blank"
                >AI程序</a
              ></span
            >或<span>亲自出马</span>。
          </div>
          <div>
            &nbsp;&nbsp;&nbsp;&nbsp;3.
            点击右上角账户名，进入个人空间点击<router-link
              :to="{ name: 'myspace_bots' }"
              >我的Bots</router-link
            >，创建 Bot 并上传程序
          </div>
          <div>
            &nbsp;&nbsp;&nbsp;&nbsp;4. 进入游戏页面，<router-link
              :to="{ name: 'game_index' }"
              >选择游戏</router-link
            >，与他人或其他 Bot 开始对战
          </div>
        </div>
        <hr />

        <div class="desc-title">我要报 Bug!</div>
        <div class="how-fix-bug">
          &nbsp;&nbsp;&nbsp;&nbsp;由于还在开发中，Bot why
          可能有着各种奇怪的问题，遇到这些问题时，你可以通过下面的方式联系作者。
          <div>
            &nbsp;&nbsp;&nbsp;&nbsp;<a
              href="#"
              target="_blank"
              >开发者：老王</a
            >
          </div>
          <div>
            &nbsp;&nbsp;&nbsp;&nbsp;<a href="#"
              >1008611@qq.com</a
            >
          </div>
        </div>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField'
export default {
  components: {
    ContentField,
  }
}
</script>

<style scoped>
a {
  font-weight: bold;
  text-decoration: none;
  color: rgb(132, 183, 200);
}
.desc {
  height: 64vh;
  margin-top: 1vh;
  /* margin-left: 4vh; */
  margin-left: 3.4vw;
  border-radius: 2vh;
  border: 1px solid rgb(224, 211, 211);
  box-shadow: 2px 2px 10px lightgray;
}
.desc-title {
  text-align: center;
  margin-top: 1vh;
  font-size: 24px;
  font-weight: bold;
}
.desc-simple-content {
  font-size: 16px;
  margin-top: 2vh;
}
.desc-how {
  text-align: center;
  margin-top: 1vh;
  font-size: 24px;
  font-weight: bold;
}
.desc-how-ans {
  font-size: 16px;
  margin-top: 2vh;
}
.desc-how-ans > div {
  padding: 0.3vh;
}
span {
  font-weight: bold;
}
.how-fix-bug {
  margin-top: 2vh;
  font-size: 16px;
}
.how-fix-bug > div {
  padding-top: 1vh;
}
</style>