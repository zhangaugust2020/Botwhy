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
  background: url(https://augu.oss-cn-beijing.aliyuncs.com/2023/04/29/bg25.png)
    no-repeat;
  background-size: 100% 100%;
  background-attachment: fixed;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}
.body-base {
  padding-top: 75px;
}
</style>
