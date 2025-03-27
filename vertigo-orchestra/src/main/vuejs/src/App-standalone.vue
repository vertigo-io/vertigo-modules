<template>
  <q-layout view="hHh LpR fFf">
    <q-header elevated class="text-white gradient-bg">
      <q-toolbar class="">
        <q-toolbar-title
          ><div>
            <q-avatar @click="$router.push('/')">
              <img src="./assets/vertigo-io.png" /> </q-avatar
            >Orchestra UI
          </div>
        </q-toolbar-title>
        <q-space /><q-btn-toggle
          v-model="lang"
          :options="[
            { label: 'Fr', value: 'fr' },
            { label: 'En', value: 'en' },
          ]"
          @input="changeLang()"
        >
        </q-btn-toggle
        ><q-btn flat round icon="power_settings_new" />
      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view :api-url="apiUrl" :orchestra-ui-read-only="readOnly"></router-view>
    </q-page-container>

    <q-footer elevated class="bg-grey-8 text-white">
      <q-toolbar>
        <q-toolbar-title class="absolute-center">
          Copyright &copy; 2020 - 2024
        </q-toolbar-title>
      </q-toolbar>
    </q-footer>
  </q-layout>
</template>

<script>
import router from "./router";
import * as Quasar from "quasar"
export default {
  name: "app",
  router,
  props: ["apiUrl", "readOnly"],
  data() {
    return {
      lang: "fr",
    };
  },
  methods: {
    changeLang: function() {
      Quasar.lang.set(this.lang == "fr" ? Quasar.lang.fr : Quasar.lang.en);
    },
  },
};
</script>
<style lang="scss">
$primary: #4396cb; // Duplicate of quasar variables (To refactor)
$secondary: #4353cb;
.gradient-bg {
  background-image: linear-gradient(to right, $secondary, $primary);
}
div.q-loading-bar {
  display: none;
}
img:hover {
  cursor: pointer;
}
</style>
