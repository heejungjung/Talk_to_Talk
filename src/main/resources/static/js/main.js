//User Name Page Variable
var nameInput = document.querySelector('#hey');

//Chat Room Page Variable
var roomPage = document.querySelector('#room-page');
var listOfRoom = document.querySelector('#listRoom');
var createRoomForm = document.querySelector('#createRoomForm');
var roomName = document.querySelector('#roomName');

//Chat Page Variable
var chatPage = document.querySelector('#chat-page');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var roomIdDisplay = document.querySelector('#room-id-display');

var stompClient = null;
var currentSubscription;
var username = null;
var roomId = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

var tt = document.querySelector('#tt');

// Function Triggered After clicking ENter the application Button
function connect(event) {
	/*
	var savedName = Cookies.get('name');
	if (savedName) {
		nameInput=savedName;
	} 이 놈 window.on(?)이나 이런 데로 빼야함
	*/
	username = nameInput.value;
	/*
	Cookies.set('name', username); //지금으로부터 7일간 유효
*/
    if(username) {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
		//~ (void) connect(headers, connectCallback, errorCallback)
		//onConnected - connect 콜백 함수
		//onError - error 콜백 함수
    }
    event.preventDefault();
}

//아래 두개를 위에 콜백 함수
function onConnected() {
  createRoom();
  listRoom();
  connectingElement.classList.add('hidden');
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

//Function Called from On COnnected Function
function listRoom()
{
    if (currentSubscription) {
        currentSubscription.unsubscribe();
    }
    currentSubscription = stompClient.subscribe(`/subscribe/chat/rooms`, onListofRoom); //`/chatapp/chat/rooms`?????

}

// Result form subscribe function of listRoom is processed here
function onListofRoom(payload)
{
	document.getElementById('listRoom').innerHTML = "";
	
	var rooms = JSON.parse(payload.body);

     for(var i=0,len = rooms.length; i<len ; i++)
     {
        var roomElement = document.createElement('li');
        roomElement.classList.add('list-group-item');
        roomElement.setAttribute("id", i);

        var formElement = document.createElement('form');
        formElement.setAttribute("id", "joinroom");
        formElement.setAttribute("name", "joinroom");

        var textElement = document.createElement('label');
        textElement.setAttribute("style","margin-right: 50px");
        var roomText = document.createTextNode(rooms[i].roomid);
        textElement.appendChild(roomText);

        var buttonElement = document.createElement('button');
        buttonElement.setAttribute("type","submit");
        buttonElement.setAttribute("class","btn btn-primary join");
        buttonElement.setAttribute("value",rooms[i].roomid);
		buttonElement.onclick=function join(event){
			if (event.type == 'click') {
				    var roomNameValue = event.target.value; //그 줄의 방제
					
				    if(roomNameValue)
				    {
				        roomPage.classList.add('hidden');
				        chatPage.classList.remove('hidden');
				        enterRoom(roomNameValue);
				    }
				    event.preventDefault();
			}
		};
		
        var buttonText = document.createTextNode("Join");
        buttonElement.appendChild(buttonText);

        formElement.appendChild(textElement);
        formElement.appendChild(buttonElement);

        roomElement.appendChild(formElement);

        listOfRoom.appendChild(roomElement);
        listOfRoom.scrollTop = listOfRoom.scrollHeight;
     }
}

//Function triggered after clicking create room
function createRoom(){
    var roomNameValue = roomName.value.trim(); //공백 없앰
    if(roomNameValue)
    {
        //roomPage.classList.add('hidden'); //창 hidden visible 설정
        //chatPage.classList.remove('hidden');
        var chatRoom = {
                    roomid: roomNameValue //방이름 전달
        };
        stompClient.send("/app/chat/rooms", {}, JSON.stringify(chatRoom)); //JSON객체를 String 객체로 변환
	}
    event.preventDefault(); //올바르지 않은 텍스트가 입력란에 입력되는것을 막음
}

// Leave the current room and enter a new one.
function enterRoom(newRoomId) {
	var roomId = newRoomId;
	Cookies.set("roomId", newRoomId);
	roomIdDisplay.textContent = roomId;

	if (currentSubscription) {
		currentSubscription.unsubscribe();
	}
	
	stompClient.subscribe('/app/chat/${roomId}/getPrevious', onPreviousMessage); //chatappcontroller후에 onPreviousMessage실행
/*	currentSubscription = stompClient.subscribe('/app/chat/${roomId}', onMessageReceived); //?????
	alert("currentSubscription:"+currentSubscription.value);
*/
	stompClient.subscribe('/subscribe/chat/room/${roomId}', onMessageReceived);
	stompClient.send('/app/chat/${roomId}/addUser',{},JSON.stringify({sender:username, type:'JOIN'}) //구독자들에게 보낸다
	);
}

function onPreviousMessage(payload)
{
    var messages = JSON.parse(payload.body);
    for (var i=0, len = messages.length;i<len;i++ )
    {
        showMessage(messages[i]);
    }
}

function sendMessage(event) {
	var messageContent = messageInput.value.trim();

 	if (messageContent.startsWith('/join ')) {
		var newRoomId = messageContent.substring('/join '.length);
		var chatRoom = {
			roomid: newRoomId
		};
    stompClient.send("/app/chat/rooms", {}, JSON.stringify(chatRoom));
    enterRoom(newRoomId);
    while (messageArea.firstChild) {
      messageArea.removeChild(messageArea.firstChild);
    }
  }
  else if(messageContent.startsWith('/leave'))
  {
    chatPage.classList.add('hidden');
    roomPage.classList.remove('hidden');
    stompClient.send("/app/chat/{roomId}/leaveuser",{},JSON.stringify({sender: username, type: 'LEAVE', content: roomId})); 
    listRoom();
    while (messageArea.firstChild) {
        messageArea.removeChild(messageArea.firstChild);
    }
  }
  else if (messageContent && stompClient) {
    var chatMessage = {
      sender: username,
      content: messageInput.value,
      type: 'CHAT'
    };

    stompClient.send("/app/chat/${roomId}/sendMessage", {}, JSON.stringify(chatMessage));
  }
  	messageInput.value = '';
  	event.preventDefault();
}

function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);
	showMessage(message);
}
function showMessage(message)
{
    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + '님이 입장하셨습니다.';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + '님이 퇴장하셨습니다.';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i'); //프로필해줌??????????????????????????????????
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

function popup(){
	var url = "chatroom";
	var name = "popup test";
	var option = "width = 500, height = 500, top = 100, left = 200, location = no"
	window.open(url, name, option);
}
/*
function readURL(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function(e) {
		    alert(document.getElementsByClassName('preview'));
			document.getElementsByClassName('preview').createElement('img');
			document.getElementsById('gd').setAttribute('src',e.target.result);
		}
		reader.readAsDataURL(input.files[0]);
	}
}
*/
createRoomForm.addEventListener("submit", connect, true);
messageForm.addEventListener("submit", sendMessage, true);
document.getElementById('createRoomButton').addEventListener('click', popup);