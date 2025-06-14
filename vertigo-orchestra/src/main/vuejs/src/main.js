import router from "./router/index.js";
import AppStandalone from "./App-standalone.vue";
import VuOApp from "./views/VuOApp.vue";
import VuOHome from "./views/VuOHome.vue";
import VuOProcess from "./views/VuOProcess.vue";

import Fr from "./lang/vertigo-orchestra-fr.js";
import EnUs from "./lang/vertigo-orchestra-en.js";

var VertigoOrchestraUi = {

  install : function (vueApp , options) {
	vueApp.use(router);
    vueApp.component("vui-orchestra-standalone", AppStandalone);
    vueApp.component("vui-orchestra", VuOApp);
    vueApp.component("vui-orchestra-home", VuOHome);
    vueApp.component("vui-orchestra-process", VuOProcess);	
	
	VertigoUi.lang.enUS.vuiOrchestra = EnUs;
    VertigoUi.lang.fr.vuiOrchestra = Fr;		
  }
}

window.VertigoOrchestraUi = VertigoOrchestraUi;

export default VertigoOrchestraUi;
