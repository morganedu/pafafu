var body = document.getElementsByTagName('body')[0];
var selectOption = 0;
var baseURI = "/_ah/xmpp/message/chat/";
var old_textarea_cursor = '';

var xmlhttp = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
var domElements = new Object();
var userData = null;

(function(){
	//IMPORTANT CONFIGURATION
	if (!String.prototype.contains) {
	    String.prototype.contains = function(s, i) {
	        return this.indexOf(s, i) != -1;
	    }
	}

	for(var i = 0; i<(arrayID = ['loading_frame','form_frame','form_select','form_button','chat_frame','chat_textarea','chat_input']).length; i++)
		domElements[arrayID[i]] = document.getElementById(arrayID[i]);

	body.removeChild(domElements.chat_frame);
	domElements.loading_frame.style.visibility = 'hidden';

	//EVENT LISTENERS
	domElements.form_button.addEventListener('click',
		function(){
			if(document.getElementById("lastName").value == "" || document.getElementById("firstName").value == ""){
				alert("Empty fields.");
				return;
			}
			else if(!/^\d{10}$/.test(document.getElementById("phoneNumber").value)){
				alert("Invalid phone number. It must contain 10 digits.");
				return;
			}
			else if(!/\S+@\S+\.\S+/.test(document.getElementById("email").value)){
				alert("Invalid email.");
				return;
			}
			else if(document.getElementById('form_select').selectedIndex == 0){
				alert("Select a department.");
				return;
			}
			domElements.loading_frame.style.visibility = 'visible';
			createUserData();
			ajaxCall('operator');

		},true);

	domElements.form_select.addEventListener('change',
		function(){
			selectOption = this.selectedIndex;
		},true);
})();

var createUserData = function(){
	if(userData === null){
		userData = new Object({
			department: document.getElementById('form_select').options[selectOption].value,
			jid: 'xxxxxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
			    var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
			    return v.toString(16);
			}) + '@morgan.edu',
			readyCode: '01234'
		});

		for(var i = 0; i<(arrayInput = ['lastName','firstName','email','phoneNumber']).length; i++)
			userData[arrayInput[i]] = document.getElementById(arrayInput[i]).value;

		userData.getURI = function(){
			return baseURI + "?"
				+ "lastName=" + userData.lastName 
				+ "&firstName=" + userData.firstName
				+ "&email=" + userData.email
				+ "&department=" + userData.department 
				+ "&phoneNumber=" + userData.phoneNumber 
				+ "&jid=" + userData.jid
				+ "&get=" + "true";
		}
	}
}

var ajaxCall = function(concept){
	if(concept.contains('message')){
		xmlhttp.onreadystatechange = 
			function(){
				if(xmlhttp.readyState==4 && xmlhttp.status==200 && domElements.chat_input.value !== ""){
					domElements.chat_textarea.value += userData.lastName + ": " + domElements.chat_input.value;
					domElements.chat_input.value = "";
				}
				/*
				else{
					if(domElements.chat_input.value !== ""){
						domElements.chat_textarea.value += userData.lastName + "~ERROR~ " + domElements.chat_input.value;
						domElements.chat_input.value = "";
					}
				}
				*/
			}
		xmlhttp.open("POST",baseURI,true);
		xmlhttp.send("message=" + domElements.chat_input.value);
	}
	else if(concept.contains('operator')){
		xmlhttp.onreadystatechange = 
			function(){
				console.log(xmlhttp.responseText);
				if(xmlhttp.readyState==4 && xmlhttp.status==200){
					//Add reponse to the chat_frame
					body.appendChild(domElements.chat_frame);
					domElements.chat_frame.innerHTML = xmlhttp.responseText;

					//Add the objects from response into the domElements object
					domElements.chat_textarea = document.getElementById('chat_textarea');
					domElements.chat_input = document.getElementById('chat_input');

					domElements.chat_input.addEventListener('keyup',
					function(event){
						if(event.keyCode == 13 && this.value !== ""){
							ajaxCall('message');
						}
					},true);

					domElements.loading_frame.style.visibility = 'hidden';
					body.removeChild(domElements.form_frame);
					ajaxCall('token');
				}
			}
		xmlhttp.open("GET",userData.getURI(),true);
		xmlhttp.send();
	}
	else if(concept.contains('token')){
		xmlhttp.onreadystatechange = 
			function(){
				if(xmlhttp.readyState==4 && xmlhttp.status==200){
					alert(0);
				}
			}
		xmlhttp.open("POST",baseURI,true);
		xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		xmlhttp.send("token=" + userData.readyCode);
	}
	//xmlhttp.send();
}