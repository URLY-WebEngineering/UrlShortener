var stompClient = null;

function connect() {
    // Set the entry where the data is going to be sent
    // var socket = new SockJS('/user/link');
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    // Conecting to the server
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/confirmation/message', function (msg) {
            // get html and delete it
            var resp = JSON.parse(msg.body);
            var element = `<li>${resp.hash}: ${resp.information}</li>`
            $("#" + resp.hash).html(element);
        });
    });
}

function sendDelete(msg) {
    stompClient.send("/app/delete", {}, msg);
}
