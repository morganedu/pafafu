(function(){
	var mainDiv = document.createElement('div');
	var image = document.createElement('img');

	var openWidget = function(){
		window.open("form.html", "_blank", "toolbar=yes, scrollbars=no, resizable=yes, top=300, left=200, width=450, height=600");
	}

	mainDiv.setAttribute('id','widget_main_div_target');
	mainDiv.setAttribute('style','width:300px;height:100px;background-color:Gray;cursor:pointer;');
	mainDiv.addEventListener('click',function() {openWidget();},false);

	image.setAttribute('src','https://www.cminds.com/wp-content/uploads/ac_uploads/69984048.png');

	mainDiv.appendChild(image);
	this.document.getElementsByTagName('body')[0].appendChild(mainDiv);
})();