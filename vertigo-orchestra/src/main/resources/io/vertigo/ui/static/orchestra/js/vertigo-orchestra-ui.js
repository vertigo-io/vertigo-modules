
window.addEventListener('vui-before-plugins', function(event) {
	const vuiApp = event.detail.vuiAppInstance;
	vuiApp.use(window.VertigoOrchestraUi);    
});