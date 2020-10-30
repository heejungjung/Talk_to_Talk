//User Name Page Variable
var userid = document.getElementById('span1').innerHTML;
var username = document.getElementById('span2').innerHTML;

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
var noticeArea = document.querySelector('#noticeArea');

var stompClient = null;
var currentSubscription = null;
var roomId = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

$(document).ready(function(){connect();});

// Function Triggered After clicking ENter the application Button
function connect() {
    if(username) {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
		//~ (void) connect(headers, connectCallback, errorCallback)
		//onConnected - connect 콜백 함수
		//onError - error 콜백 함수
    }
}

//아래 두개를 위에 콜백 함수
function onConnected() {
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
		//그 방의 사람수도 -1
    }
    currentSubscription = stompClient.subscribe(`/app/chat/rooms`, onListofRoom); //`/chatapp/chat/rooms`?????

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

		/*방번호 나오도록*/
        var num_textElement = document.createElement('label');
        num_textElement.setAttribute("id","list_num_css");
        var num_roomText = document.createTextNode(i+1);
        num_textElement.appendChild(num_roomText);


        var textElement = document.createElement('label');
        textElement.setAttribute("style","margin-right: 50px");
        var roomText = document.createTextNode(rooms[i].roomid);
        textElement.appendChild(roomText);

        var buttonElement = document.createElement('button');
        buttonElement.setAttribute("type","submit");
        buttonElement.setAttribute("class","btn btn-primary join");
        buttonElement.setAttribute("value",rooms[i].roomid);
		buttonElement.onclick=function k(event){
			if (event.type == 'click') {
				    var roomNameValue = event.target.value; //그 줄의 방제
					join(roomNameValue);
				    event.preventDefault();
			}
		};
		
		//방개설시 방개설 이름 초기화
		roomName.value = '';
        var buttonText = document.createTextNode("Join");
        buttonElement.appendChild(buttonText);

		/*방번호 나오도록*/
        formElement.appendChild(num_textElement);
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
        var chatRoom = {
			roomid: roomNameValue //방이름 전달
        };
        stompClient.send("/app/chat/rooms", {}, JSON.stringify(chatRoom)); //JSON객체를 String 객체로 변환
		join(roomNameValue);
	}
    event.preventDefault(); //올바르지 않은 텍스트가 입력란에 입력되는것을 막음
}

function join(newRoomId){
    roomPage.classList.add('hidden');
    chatPage.classList.remove('hidden');
	enterRoom(newRoomId);
}
		
// Leave the current room and enter a new one.
function enterRoom(newRoomId) {
	var roomId = newRoomId;
	Cookies.set("roomId", newRoomId);
	roomIdDisplay.textContent = roomId;

	if (currentSubscription) {
		currentSubscription.unsubscribe();
	}
		var chatRoom = {
			sender:username,
			type:'JOIN',
			roomid: newRoomId
		};
	
	stompClient.subscribe('/subscribe/chat/${roomId}/getPrevious', onPreviousMessage); //chatappcontroller후에 onPreviousMessage실행
	currentSubscription = newRoomId;
	alert("currentSubscription:"+currentSubscription);
	stompClient.subscribe('/subscribe/chat/room/${roomId}', onMessageReceived);
	stompClient.send('/app/chat/${newRoomId}/addUser',{},JSON.stringify(chatRoom)); //구독자들에게 보낸다
}

function onPreviousMessage(payload)
{
	notice(noticemsg);
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
    join(newRoomId);
    while (messageArea.firstChild) {
      messageArea.removeChild(messageArea.firstChild);
    }
  }
  else if(messageContent.startsWith('/leave'))
  {
	leave();
  }

  else if (messageContent.startsWith('/notice ')){
    var trimStr = messageInput.value.substring(8);
	notice(trimStr);
  }

  else if (messageContent && stompClient) {
    var chatMessage = {
      senderid: userid,
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

	    var textElement = document.createElement('p');
	    var messageText = document.createTextNode(message.content);
	    textElement.appendChild(messageText);

    	messageElement.appendChild(textElement);
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + '님이 퇴장하셨습니다.';

	    var textElement = document.createElement('p');
	    var messageText = document.createTextNode(message.content);
	    textElement.appendChild(messageText);

    	messageElement.appendChild(textElement);
    }
	else if(message.type === 'NOTICE'){
		alert("notice:"+message.content);
        messageElement.classList.add('event-message');
        message.content = message.sender + '님이 공지를 남겼습니다.';

        var textElement = document.createElement('p');
	    var messageText = document.createTextNode(message.content);
	    textElement.appendChild(messageText);

        messageElement.appendChild(textElement); 
	}else {
		if(message.sender === username){
        	messageElement.classList.add('chat-message-group');
        	messageElement.classList.add('writer-user');
		} else{
			messageElement.classList.add('chat-message-group');
		}
        var div_avatar = document.createElement('div');
        	div_avatar.classList.add('chat-thumb');
        var figure = document.createElement('figure');
        	figure.classList.add('image');
        	figure.classList.add('is-32x32');
        var image = document.createElement('img');
			image.setAttribute('src',message.pic);
        figure.appendChild(image);
        div_avatar.appendChild(figure);
        messageElement.appendChild(div_avatar);

        var div_msg = document.createElement('div');
        	div_msg.classList.add('chat-messages');
        var usernameElement = document.createElement('span');
        	usernameElement.classList.add('from');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        div_msg.appendChild(usernameElement);
        messageElement.appendChild(div_msg);

	    var textElement = document.createElement('p');
	    var messageText = document.createTextNode(message.content);
        	textElement.classList.add('message');
	    textElement.appendChild(messageText);
	    div_msg.appendChild(textElement);
	    messageElement.appendChild(div_msg);
	}
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function popup(){
	var url = "roompopup";
	var name = "popup test";
	var option = "width = 500, height = 600, top = 100, left = 200, location = no"
	window.open(url, name, option);
}

function leave(){
	alert(currentSubscription);
	
    chatPage.classList.add('hidden');
    roomPage.classList.remove('hidden');
	
	var chatRoom = {
		sender:username,
		type:'LEAVE',
		roomid: currentSubscription
	};
	
    stompClient.send("/app/chat/{roomId}/leaveuser",{},JSON.stringify(chatRoom)); 
	currentSubscription = null;
    listRoom();

    while (messageArea.firstChild) {
        messageArea.removeChild(messageArea.firstChild);
    }
}

function notice(notice){
    var noticeMessage = {
		roomid: currentSubscription,
        sender: username,
        content: notice,
        type: 'NOTICE'
    };
    stompClient.send("/app/chat/${roomId}/notice", {}, JSON.stringify(noticeMessage));
    alert("공지등록:"+notice);
    noticeArea.textContent = notice;
}

createRoomForm.addEventListener("submit", createRoom, true);
messageForm.addEventListener("submit", sendMessage, true);
document.getElementById('createRoomButton').addEventListener('click', popup);
document.getElementById('backButton').addEventListener('click', leave);