var username = document.getElementById('span1').innerHTML;
var listOfRoom = document.querySelector('#listRoom');
var popupCreateRoomForm = document.querySelector('#popupCreateRoomForm');

//Chat Room Page Variable
var roomPage = document.querySelector('#room-page');
var listOfRoom = document.querySelector('#listRoom');
var createRoomForm = document.querySelector('#createRoomForm');
var popupCreateRoomForm = document.querySelector('#popupCreateRoomForm');
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
var roomId = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    if(username) {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
		//~ (void) connect(headers, connectCallback, errorCallback)
		//onConnected - connect 콜백 함수
		//onError - error 콜백 함수
    }
}




function onConnected() {

   createRoom();

	window.close();
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}




function createRoom(){
	
    var roomNameValue = roomName.value.trim(); 

    if(roomNameValue){

        var chatRoom = {
                    roomid: roomNameValue 
        };
        stompClient.send("/app/chat/rooms", {}, JSON.stringify(chatRoom)); 
	}
    event.preventDefault();

}


popupCreateRoomForm.addEventListener("submit", connect, true);
