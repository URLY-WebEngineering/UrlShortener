var stompClient = null;
function connect() {
    // Set the entry where the data is going to be sent
    var socket = new SockJS('/delete');
    socket.binaryType = "arraybuffer";
    stompClient = Stomp.over(socket);
    // Conecting to the server
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/confirmation/message',function(messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
            //Client subscribes to /user to receive non-broadcast messages
        });

    });
}

function sendMessage() {
    var user = document.getElementById('user').value;
    var hash  = document.getElementById('hash').value;
    stompClient.send("/app/delete", {},
        JSON.stringify({'user':user, 'hash':hash}));

}

function showMessageOutput(messageOutput) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(messageOutput.user + ": "
        + messageOutput.hash ));
    response.appendChild(p);
}