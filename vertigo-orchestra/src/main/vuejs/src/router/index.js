import Vue from "vue";
import { createRouter, createWebHashHistory } from 'vue-router'
import VuOHome from "../views/VuOHome.vue";
import VuOProcess from "../views/VuOProcess.vue";
import NotFound from "../views/NotFound.vue";

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: "/",
      name: "Home",
      component: VuOHome,
    },
    {
      path: "/process/:name",
      name: "Process",
      component: VuOProcess,
      props : route => ({ processName: route.params.name })
    },
    {
      path: "/:pathMatch(.*)*",
      name: "NotFound",
      component: NotFound,
    },
  ]
})

export default router;
