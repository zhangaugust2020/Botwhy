<template>
  <div class="playground">
    <SnakeGameMap />
  </div>
</template>

<script>
import SnakeGameMap from './SnakeGameMap.vue'
export default {
  components: {
    SnakeGameMap
  },
}
</script>


<style scoped>
div.playground {
  margin: 30px auto;
  width: 70vw;
  height: 80vh;
}
</style>
