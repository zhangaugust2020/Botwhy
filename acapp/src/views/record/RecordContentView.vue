<template>
  <div>
    <PlayGround class="body-base" />
  </div>
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import { onUnmounted } from 'vue';
export default {
  name: "PkIndexView",
  components: {
    PlayGround,
  },
  setup () {
    onUnmounted(() => {
      localStorage.removeItem("record-map");
    })
  }
}
</script>

<style scoped>
</style>