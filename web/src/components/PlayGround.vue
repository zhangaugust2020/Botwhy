<template>
  <div class="playground">
    <SnakeGameMap
      v-if="
        $store.state.pk.snake.status === 'playing' ||
        $store.state.record.is_snake_record
      "
    />
    <ReversiGameMap
      v-if="
        $store.state.pk.reversi.status === 'playing' ||
        $store.state.record.is_reversi_record
      "
    />
  </div>
</template>

<script>
import SnakeGameMap from './SnakeGameMap.vue'
import ReversiGameMap from './ReversiGameMap.vue'
export default {
  components: {
    SnakeGameMap,
    ReversiGameMap
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
