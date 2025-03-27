import router from "./router/index.js";
import AppStandalone from "./App-standalone.vue";
import VuOApp from "./views/VuOApp.vue";
import VuOHome from "./views/VuOHome.vue";
import VuOProcess from "./views/VuOProcess.vue";

import Fr from "./lang/vertigo-orchestra-fr.js";
import EnUs from "./lang/vertigo-orchestra-en.js";

import * as Quasar from "quasar"

var VertigoOrchestraUi = {

//Quasar.lang.set(Quasar.lang.fr);

  install : function (vueApp , options) {
    vueApp.component("vui-orchestra-standalone", AppStandalone);
    vueApp.component("vui-orchestra", VuOApp);
    vueApp.component("vui-orchestra-home", VuOHome);
    vueApp.component("vui-orchestra-process", VuOProcess);
  },
  lang : {
    enUS: EnUs,
    fr: Fr
  }  
}

if (Quasar.lang.enUS) {
  Quasar.lang.enUS.vuiOrchestra = {...Quasar.lang.enUs.vui, ...VertigoOrchestraUi.lang.enUS};
}
if (Quasar.lang.fr) {
  Quasar.lang.fr.vuiOrchestra = {...Quasar.lang.fr.vui, ...VertigoOrchestraUi.lang.fr};
}

if (window) {
  window.VertigoOrchestraUi = VertigoOrchestraUi
  window.addEventListener('vui-before-plugins', function(event) {
    let vuiApp = event.detail.vuiAppInstance;
    vuiApp.use(router).use(VertigoOrchestraUi);
  });
}

export default VertigoOrchestraUi;
