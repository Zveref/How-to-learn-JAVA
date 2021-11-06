'use strict';

const webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatapp");

/**
 * Entry point into chat room
 */
window.onload = function() {

    webSocket.onclose = () => alert("WebSocket connection closed");
    webSocket.onmessage = (msg) => updateChatRoom(msg);

    $("#btn-msg").click(() => sendMessage($("#message").val()));
};

/**
 * Send a message to the server.
 * @param msg  The message to send to the server.
 */
function sendMessage(msg) {
    if (msg !== "") {
        webSocket.send(msg);
        $("#message").val("");
    }
}

/**
 * Update the chat room with a message.
 * @param message  The message to update the chat room with.
 */
function updateChatRoom(message) {
    // TODO: convert the data to JSON and use .append(text) on a html element to append the message to the chat area
    let data = JSON.parse(message.data)
    console.log(data)

    $("#chatArea").append(data.userMessage)
}

// function updateChatRoom(message) {
//     // TODO: convert the data to JSON and use .append(text) on a html element to append the message to the chat area
//     // console.log("message is " + message);
//
//     let dataObj = JSON.parse(message.data);
//     // console.log("dataObj is " + dataObj);
//
//     let dataMsg = dataObj.userMessage;
//     // console.log("dataMsg is " + dataMsg);
//
//     if (dataMsg.includes("recall")) {
//         document.getElementById("testDIV").remove();
//         console.log("dataMsg is recalled");
//     }
//     else {
//         // Use JS to create a div with an unique id
//         // another way to create id is using j2html
//         // actually, j2html is better as it can be controlled in the backend
//         let div = document.createElement('div');
//         div.id = 'testDIV';
//         div.innerHTML = dataMsg;
//
//         let chatArea = document.getElementById("chatArea");
//         chatArea.appendChild(div);
//     }
// }
//
// function recallMessage() {
//     webSocket.send("recall message");
// }