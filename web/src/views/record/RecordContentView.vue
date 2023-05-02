<template>
  <div>
    <PlayGround class="body-base" />
  </div>
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import { onUnmounted } from 'vue';
import { useStore } from 'vuex'
export default {
  name: "PkIndexView",
  components: {
    PlayGround,
  },
  setup () {
    const store = useStore();
    onUnmounted(() => {
      clearInterval(store.state.record.interval_id);
      store.commit("updateIntervalId", null);
      if (store.state.record.is_snake_record) {
        localStorage.removeItem("record-snake-map");
        store.commit("updateIsSnakeRecord", false);
      }
      if (store.state.record.is_reversi_record) {
        store.commit("updateIsReversiRecord", false);
      }
    })
  }
}
</script>

<style scoped>
</style>