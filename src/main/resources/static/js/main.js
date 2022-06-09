'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var stompClient = null;
var username = null;
var recievername = null
var colors = [
	'#2196F3', '#32c787', '#00BCD4', '#ff5652',
	'#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];
connect();
function connect(/*event*/) {
	username = currentUserId/* document.querySelector('#name').value.trim();*/

	if (username) {
		//usernamePage.classList.add('hidden');
		//chatPage.classList.remove('hidden');
		var socket = new SockJS('/ws');
		stompClient = Stomp.over(socket);

		stompClient.connect({}, onConnected, onError);
	}
	/*event.preventDefault();*/
}


function onConnected() {
	// Subscribe to the Public Topic
	/*stompClient.unsubscribe('/user/' + currentUserId + '/queue/messages');*/
	stompClient.subscribe('/user/' + currentUserId + '/queue/messages', onMessageReceived);
	// Tell your username to the server
	stompClient.send("/app/chat.register",
		{},
		JSON.stringify({ sender: username, type: 'JOIN' })
	)

	connectingElement.classList.add('hidden');
}


function onError(error) {
	connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	connectingElement.style.color = 'red';
}


function send(event) {
	var messageContent = messageInput.value.trim();
	recievername = document.querySelector('#recievername').value.trim();
	//alert(recievername)
	if (messageContent && stompClient) {
		var chatMessage = {
			sender: username,
			reciever: recievername,
			content: messageInput.value,
			type: 'CHAT'
		};

		stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
		messageInput.value = '';
	}
	event.preventDefault();
}

function onMessageReceived(payload,isOther) {
	var message = JSON.parse(payload.body);
	var messageElement = document.createElement('li');
	if(!isOther)
			{
				otherMessage.push(payload);
			}
	recievername = document.querySelector('#recievername').value.trim();
	if (message.sender != currentUserId) {
		if (message.sender != recievername) {
			console.log("....... from"+message.sender+" & "+message.content)
			if(typeof(isOther)=='undefined')
			{
				notification(message.sender,message.content);
			}
			return;
		}
	}
	else
	{
		if(message.reciever!=recievername)
		{
			console.log("....... from"+message.sender+" & "+message.content)
			return;
		}
	}
	if (message.type === 'JOIN') {
		messageElement.classList.add('event-message');
		message.content = message.sender + ' joined!';
	} else if (message.type === 'LEAVE') {
		messageElement.classList.add('event-message');
		message.content = message.sender + ' left!';
	} else {
		messageElement.classList.add('chat-message');

		var avatarElement = document.createElement('i');
		var avatarText = document.createTextNode(message.sender[0]);
		avatarElement.appendChild(avatarText);
		avatarElement.style['background-color'] = getAvatarColor(message.sender);

		messageElement.appendChild(avatarElement);

		var usernameElement = document.createElement('span');
		var usernameText = document.createTextNode(message.sender);
		usernameElement.appendChild(usernameText);
		messageElement.appendChild(usernameElement);
	}

	var textElement = document.createElement('p');
	var messageText = document.createTextNode(message.content);
	textElement.appendChild(messageText);

	messageElement.appendChild(textElement);

	messageArea.appendChild(messageElement);
	messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
	var hash = 0;
	for (var i = 0; i < messageSender.length; i++) {
		hash = 31 * hash + messageSender.charCodeAt(i);
	}

	var index = Math.abs(hash % colors.length);
	return colors[index];
}

//usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', send, true)



