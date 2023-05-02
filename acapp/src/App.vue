<template>
  <NavBar />
  <router-view isRouterAlive />
</template>

<script>
import NavBar from '@/components/navbar/NarBar'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
export default {
  name: "App",
  components: {
    NavBar,
  },
  setup () {

  }
}
</script>

<style>
body {
  background: url(https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/501/mountain.jpg)
    no-repeat;
  background-size: 100% 100%;
  background-attachment: fixed;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}
.body-base {
  padding-top: 75px;
}
</style>
