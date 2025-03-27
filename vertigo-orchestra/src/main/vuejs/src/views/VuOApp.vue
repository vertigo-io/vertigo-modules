<template>
  <div><router-view :api-url="apiUrl" :orchestra-ui-read-only="readOnly"></router-view></div>
</template>

<script>
import router from "../router";
export default {
  name: "app",
  router,

  data() {
    return {
      left: false,
    };
  },
  props: ["apiUrl", "readOnly"],
};
</script>
<style lang="scss">
div.q-loading-bar {
  display: none;
}
</style>
