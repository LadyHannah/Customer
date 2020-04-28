window.addEventListener(('orientationchange' in window ? 'orientationchange' : 'resize'), (function() {
	function c() {
		var d = document.documentElement;
		var cw = d.clientWidth || 1440;
		if(cw > 1440){
			d.style.fontSize=40+'px';
		}else if(cw < 540){
			d.style.fontSize=15+'px';
		}else{
			d.style.fontSize=(40 * (cw / 1440))+'px';
		}
		//d.style.fontSize = (40 * (cw / 1440)) > 40 ? 40 + 'px' : (40 * (cw / 1440)) + 'px';
	}
	c();
	return c;
})(), false);


