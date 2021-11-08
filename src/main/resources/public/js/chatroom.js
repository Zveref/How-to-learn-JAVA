'use strict';

const webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatapp?userId=" + window.localStorage.getItem('userId'));


var timerID = 0;

function keepAlive() {
    var timeout = 15000;
    if (webSocket.readyState == webSocket.OPEN) {
        console.log("webscocket send")
        webSocket.send("Keep alive");
    }
    timerID = setTimeout(keepAlive, timeout);
}

function cancelKeepAlive() {
    if (timerID) {
        clearTimeout(timerID);
    }
}

keepAlive();


var today = new Date();
var date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
var dateTime = date+' '+time;   //2018-8-3 11:12:40


if (window.localStorage.getItem('isLogin') !== 'true') {
    window.location.href = './index.html'
}
$(function () {

    $(".chat").niceScroll();
    $("#input-message").emojioneArea({
        pickerPosition: "bottom",
        tonesStyle: "radio"
    });
    $("#RoomManager").val(window.localStorage.getItem('username'))

});

let createModal = $("#create-modal")[0];
let notifyModal = $("#notif-modal")[0];
let profileModal = $("#profile-modal")[0];

$("#plus-btn").click(() => {
    createModal.style.display = "block";
});

$("#joined-rooms").click(() => {
    getJoinedChannels(window.localStorage.getItem('userId'), false);
});

$("#all-rooms").click(() => {
    getAllChannels();
});

$("#notification-btn").click(() => {
    notifyModal.style.display = "block";
});

$("#profile-btn").click(() => {

    $('#username').val(window.localStorage.getItem('username'));
    $('#school').val(window.localStorage.getItem('school'));
    $('#interest').val(window.localStorage.getItem('interest'));
    $('#DOB').val(window.localStorage.getItem('birthDate'));


    profileModal.style.display = "block";
});

$("#logout-btn").click(() => {
    window.localStorage.removeItem('username');
    window.localStorage.removeItem('school');
    window.localStorage.removeItem('interest');
    window.localStorage.removeItem('birthDate');
    window.localStorage.setItem('isLogin', 'false');

    window.location.href = './index.html';
    cancelKeepAlive();
})

window.onload = function() {

    keepAlive();
    let userId = window.localStorage.getItem("userId");


};


function searchChannel() {
    let input = document.getElementById("searchInput");
    let searchInput = input.value;
    $.post("/searchChannel", {keyWords: searchInput}, function (data) {
        // console.log(data)
        displayChatRoomList(JSON.parse(data), false)
    });
}

function getAllChannels() {
    $.post("/viewAllChannels", function (data) {
        // console.log(JSON.parse(data))
        if(data.length > 0) {
            displayChatRoomList(JSON.parse(data), false)
        }
    });
}
function loadJoinedChannels() {

}

$("#quit-create-chatroom").click(() => {
    createModal.style.display = "none";
})

$("#leave-room").click(() => {

    const leaveRoomValues = {
        userId: window.localStorage.getItem("userId"),
        channelId: window.localStorage.getItem('channelId'),
    }
    $.post("/quitChannel", leaveRoomValues, function (data) {
        // console.log(data)
        getJoinedChannels(window.localStorage.getItem('userId'))
    });
    today = new Date();
    date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
    time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
    dateTime = date+' '+time;   //2018-8-3 11:12:40

    const msgValues = {
        senderId: '0',
        receiverId: '1',
        channelId: window.localStorage.getItem('channelId'),
        time: dateTime, //2018-8-3 11:12:40
        content: `${window.localStorage.getItem('userId')}leave`
    }
    webSocket.send(JSON.stringify(msgValues));
})

$("#leave-all-rooms").click(() => {

    $.get("/quitAllChannels", {userId: window.localStorage.getItem('userId')}, function (data) {
        // console.log(data)
    });
})

$("#online-user").click(() => {
    $("#channel-user-list").css("display", "block")
    $("#all-user-list").css("display", "none")
})

$("#submit-create-chatroom").click(() => {

    let roomName = $('#RoomName').val()
    let capacity = $('#RoomCapacity').val()
    let isPrivate =  document.getElementById("ifPrivate").checked === true
    if (checkIsValid(roomName) && checkIsValid(capacity)) {
        const createChannelValues = {
            adminId: window.localStorage.getItem('userId'),
            channelName: roomName,
            capacity: capacity,
            isPrivate: isPrivate,
        }

        $.post("/createChannel", createChannelValues, function (data) {

            const dataParse = JSON.parse(data)



            createModal.style.display = "none";

            // display room
            $('#chat-header-id').empty();
            $('#chat-header-id').append(
                `<h6>${dataParse.channelName}</h6>` +
                `<p style="padding: 2px">Room Capacity: ${dataParse.capacity}</p>`
            )

            if (dataParse.admin.id === parseInt(window.localStorage.getItem('userId'))) {
                $('#invite-user-id').empty()
                $('#invite-user-id').append(
                    `<button onclick="inviteUser()" class="btn btn-block room-btn">Invite User</button>`
                )
            }
            // assert chat room
            $("#chat-body-id").css('display', 'block')
            // console.log(dataParse)
            // window.localStorage.setItem('channelId', roomId);
            // return joined list
            getJoinedChannels(window.localStorage.getItem('userId'), true)
            // console.log(data)
            window.localStorage.setItem('channelId', dataParse.channelId);
            $("#message-area-id").empty();

            today = new Date();
            date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
            time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
            dateTime = date+' '+time;   //2018-8-3 11:12:40

            const msgValues = {
                senderId: '0',
                receiverId: '1',
                channelId: window.localStorage.getItem('channelId'),
                time: dateTime, //2018-8-3 11:12:40
                content: `${window.localStorage.getItem('userId')}join`
            }
            webSocket.send(JSON.stringify(msgValues));

        });
    } else {

        if (!checkIsValid(roomName)) {
            $("#room-name-invalid-feedback").css('display', 'block')
            $("#room-name-invalid-feedback").css('color', 'red')
        }
        if (!checkIsValid(capacity)) {
            $("#room-capacity-invalid-feedback").css('display', 'block')
            $("#room-capacity-invalid-feedback").css('color', 'red')
        }
    }

    if (checkIsValid(roomName)) {
        $("#room-name-invalid-feedback").css('display', 'none')
    }

    if (checkIsValid(capacity)) {
        $("#room-capacity-invalid-feedback").css('display', 'none')
    }

})

function checkIsValid(value) {
    return value === undefined || value === '' ? false : true
}

// When the user clicks anywhere outside of the modal, close it
$(window).click(function (event) {
    if (event.target === createModal || event.target === notifyModal || event.target === profileModal) {
        event.target.style.display = "none";
    }
});


// get chat room list

const chatRoomData = [
    {
        roomId: 1,
        members: 5,
        roomName: 'Music',
        isPrivate: false
    },
    {
        roomId: 2,
        members: 3,
        roomName: 'Sports',
        isPrivate: true
    },
    {
        roomId: 3,
        members: 23,
        roomName: 'Life In Houston',
        isPrivate: false
    },
    {
        roomId: 4,
        members: 23,
        roomName: 'Sports',
        isPrivate: false
    },
    {
        roomId: 5,
        members: 23,
        roomName: 'Sports',
        isPrivate: false
    }
]

function inviteUser() {
    $.get(`/switchChannel/${window.localStorage.getItem('channelId')}`, function(data){
        const dataParse = JSON.parse(data)


        $.get(`/getAllUsers`, function(data){
            // console.log(JSON.parse(data))
            displayAllUserList(JSON.parse(data), dataParse.roommates)
        })

    })


}

function changeChannel(roomId, isInRoom) {
    $.get(`/switchChannel/${roomId}`, function(data){
        window.localStorage.setItem('channelId', roomId);
        const parseData = JSON.parse(data);

        if(isInRoom) {
            $("#chat-body-id").css('display', 'block')
            // {admin ..., roommates: [
            //              d.id
            //             d.name
            //             d.blockNum
            //             d.muteStatus
            // ]}
            // admin

            $('#chat-header-id').empty();
            $('#chat-header-id').append(
                `<h6>${parseData.channelName}</h6>` +
                `<p style="padding: 2px">Room Capacity: ${parseData.capacity}</p>`
            )

            $('#invite-user-id').empty()

            if (parseData.admin.id === parseInt(window.localStorage.getItem('userId'))) {
                $('#invite-user-id').append(
                    `<button onclick="inviteUser()" class="btn btn-block room-btn">Invite User</button>`
                )
            }
            // clear message
            // load history message
            $("#message-area-id").empty();

            displayChannelUserList(parseData.roommates, parseData);

            displayReceivers(parseData.roommates);

            if (parseData.history.length > 0) {
                parseData.history.forEach((historyMsg) => {
                    displayChatMessage(historyMsg)
                })

            }
        }



    })

}

function getJoinedChannels (userId, ifDisplayUser) {
    $.get(`/joinedChannels/${userId}`, function (data) {
        // console.log(data)
        if(data.length > 0) {
            const dataParse = JSON.parse(data)
            // console.log(dataParse)
            displayChatRoomList(dataParse, ifDisplayUser)
        }
    });
}

function joinChannel(userId, channelId) {
    const channelValues = {
        userId: userId,
        channelId: channelId
    }
    // console.log(channelValues)
    $.post(`/joinChannel`, channelValues, function (data) {
        // console.log(data)
        const dataParse = JSON.parse(data)
        // display room
        $('#chat-header-id').empty();
        $('#chat-header-id').append(
            `<h6>${dataParse.channelName}</h6>` +
            `<p style="padding: 2px">Room Capacity: ${dataParse.capacity}</p>`
        )
        // assert chat room
        $("#chat-body-id").css('display', 'block')
        $(`#join-room-btn-${channelId}`).remove();
        // console.log(dataParse)
        // if(dataParse.roommates.length > 0) {
        displayChannelUserList(dataParse.roommates, dataParse)
        // }
        window.localStorage.setItem('channelId', channelId);
        $("#message-area-id").empty();

        today = new Date();
        date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
        time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
        dateTime = date+' '+time;   //2018-8-3 11:12:40

        const msgValues = {
            senderId: '0',
            receiverId: '-1',
            channelId: window.localStorage.getItem('channelId'),
            time: dateTime, //2018-8-3 11:12:40
            content: `${window.localStorage.getItem('userId')}join`
        }
        webSocket.send(JSON.stringify(msgValues));
        displayReceivers(dataParse.roommates);
    });
}


function displayChatRoomList(data, isDisplayUserList) {
    // console.log(data)
    $("#chat-room-list").empty()
    data.forEach((d)=>{
        // console.log(d)
        const isInRoom = d.roommates.some((roommate)=> roommate.id === parseInt(window.localStorage.getItem('userId')))

        if (d.ifLocked) {
            $("#chat-room-list").append(
                `<div style="display: flex">` +
                    `<button class="user" onclick="changeChannel(${d.channelId}, true)">` +
                        '<div class="avatar">' +
                            '<svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" fill="currentColor" class="bi bi-chat-square" viewBox="0 0 16 16">' +
                                '<path d="M14 1a1 1 0 0 1 1 1v8a1 1 0 0 1-1 1h-2.5a2 2 0 0 0-1.6.8L8 14.333 6.1 11.8a2 2 0 0 0-1.6-.8H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v8a2 2 0 0 0 2 2h2.5a1 1 0 0 1 .8.4l1.9 2.533a1 1 0 0 0 1.6 0l1.9-2.533a1 1 0 0 1 .8-.4H14a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"/>' +
                            '</svg>' +
                        '</div>' +
                       `<div class="room-name">${d.channelName} <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-lock" viewBox="0 0 16 16">` +
                            '<path d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2zm3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2zM5 8h6a1 1 0 0 1 1 1v5a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V9a1 1 0 0 1 1-1z"/>' +
                       ' </svg>' +
                        '</div>' +
                        '<div class="room-description">Group locked</div>' +
                    '</button>' +
                    // `<button style="height: 20px" class="chat-room-list" onclick="joinChannel(${window.localStorage.getItem('userId')}, ${d.channelId})">` +
                    //     ' <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-plus-circle pull-right" viewBox="0 0 16 16">' +
                    //     ' <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>' +
                    //     ' <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"/>' +
                    //     ' </svg>' +
                    // `</button>` +
                '</div>'
            )



        } else {

            if (isInRoom) {
                $("#chat-room-list").append(
                    `<div style="display: flex">` +
                        `<button class="user" onclick="changeChannel(${d.channelId}, true)">` +
                            '<div class="avatar">' +
                            '<svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" fill="currentColor" class="bi bi-chat-square-dots" viewBox="0 0 16 16" alt="User name">' +
                            '<path d="M14 1a1 1 0 0 1 1 1v8a1 1 0 0 1-1 1h-2.5a2 2 0 0 0-1.6.8L8 14.333 6.1 11.8a2 2 0 0 0-1.6-.8H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v8a2 2 0 0 0 2 2h2.5a1 1 0 0 1 .8.4l1.9 2.533a1 1 0 0 0 1.6 0l1.9-2.533a1 1 0 0 1 .8-.4H14a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"/>' +
                            '<path d="M5 6a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm4 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm4 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0z"/>' +
                            '</svg>' +
                            '</div>' +
                            `<div class="room-name">${d.channelName}</div>` +
                            `<div class="room-description">Capacity ${d.capacity}</div>` +
                        '</button>' +
                    '</div>'
                )
            } else {
                $("#chat-room-list").append(
                    `<div style="display: flex">` +
                        `<button class="user" onclick="changeChannel(${d.channelId}, false)">` +
                        '<div class="avatar">' +
                            '<svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" fill="currentColor" class="bi bi-chat-square-dots" viewBox="0 0 16 16" alt="User name">' +
                                '<path d="M14 1a1 1 0 0 1 1 1v8a1 1 0 0 1-1 1h-2.5a2 2 0 0 0-1.6.8L8 14.333 6.1 11.8a2 2 0 0 0-1.6-.8H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v8a2 2 0 0 0 2 2h2.5a1 1 0 0 1 .8.4l1.9 2.533a1 1 0 0 0 1.6 0l1.9-2.533a1 1 0 0 1 .8-.4H14a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"/>' +
                                '<path d="M5 6a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm4 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm4 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0z"/>' +
                            '</svg>' +
                        '</div>' +
                        `<div class="room-name">${d.channelName}</div>` +
                        `<div class="room-description">Capacity ${d.capacity}</div>` +
                        '</button>' +
                        `<button id="join-room-btn-${d.channelId}" style="height: 20px" class="chat-room-list" onclick="joinChannel(${window.localStorage.getItem('userId')}, ${d.channelId})">` +
                            ' <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-plus-circle pull-right" viewBox="0 0 16 16">' +
                                ' <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>' +
                                ' <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"/>' +
                            ' </svg>' +
                        `</button>` +
                    '</div>'
                )
            }



        }

        if (isDisplayUserList) {
            displayChannelUserList(d.roommates, d)
        } else {
            $("#channel-user-list").empty()
        }

    })

}

function displayChannelUserList(data, alldata) {
    console.log(alldata)
    $("#channel-user-list").empty()


    data.forEach((d)=>{
        // console.log(d)
        // should not use localstorage to decide admin
        if (parseInt(window.localStorage.getItem('userId')) === parseInt(alldata.admin.id)) {
            if (d.id===parseInt(alldata.admin.id)) {
                $("#channel-user-list").append(
                    '<div class="user">' +
                    '<div class="avatar">' +
                    '<img src="https://bootdey.com/img/Content/avatar/avatar6.png" alt="User name" />' +
                    '<div class="status online" style="visibility: hidden"></div>' +
                    '</div>' +
                    '<img class="admin-symbol" src="./images/admin-star.png"/>' +
                    `<div class="name">${d.name}</div>` +
                    `<img id="mute-${d.id}" src="../images/mute.jpg" width="25px" height="25px" alt="mute" style="visibility: hidden"  />` +
                    '</div>'
                )
            } else {
                $("#channel-user-list").append(
                    '<div class="user">' +
                    '<div class="avatar">' +
                    ' <img src="https://bootdey.com/img/Content/avatar/avatar1.png" alt="User name" />' +
                    '<div class="status off" style="visibility: hidden"></div>' +
                    '</div>' +
                    `<div class="name">${d.name}</div>` +
                    `<div style="display: flex">` +
                    '<img id="mute-${d.id}" src="../images/mute.jpg" width="25px" height="25px" style="visibility: hidden" alt="mute" />' +
                    `<button id="block-btn-${d.id}" onclick="blockUser(${d.id},window.localStorage.getItem('channelId'))">block</button>` +
                    `</div>` +
                    '</div>'
                )
            }
        } else {
            if (d.id===parseInt(alldata.admin.id)) {
                $("#channel-user-list").append(
                    '<div class="user">' +
                    '<div class="avatar">' +
                    '<img src="https://bootdey.com/img/Content/avatar/avatar6.png" alt="User name" />' +
                    '<div class="status online" style="visibility: hidden"></div>' +
                    '</div>' +
                    '<img class="admin-symbol" src="./images/admin-star.png"/>' +
                    `<div class="name">${d.name}</div>` +
                    '<img id="mute-${d.id}" src="../images/mute.jpg" width="25px" height="25px" style="visibility: hidden" alt="mute" />' +
                    '</div>'
                )
            } else {
                $("#channel-user-list").append(
                    '<div class="user">' +
                    '<div class="avatar">' +
                    ' <img src="https://bootdey.com/img/Content/avatar/avatar1.png" alt="User name" />' +
                    '<div class="status off"></div>' +
                    '</div>' +
                    `<div class="name">${d.name}</div>` +
                    `<div style="display: flex">` +
                    '<img id="mute-${d.id}" src="../images/mute.jpg" width="25px" height="25px" style="visibility: hidden" alt="mute" />' +
                    `</div>` +
                    '</div>'
                )
            }
        }
        if (d.muteStatus) {
            $(`#mute-${d.id}`).css('visibility', 'visible')
        }

    })

}

function blockUser(userId, channelId) {
    $.get(`/block/${channelId}/${userId}`, function (data) {
        console.log(data)
        //block-btn
        var btn = document.getElementById(`block-btn-${userId}`);
        btn.innerHTML = 'BLOCKED';
        document.getElementById(`block-btn-${userId}`).disabled = 'disabled';
    });
}

function inviteUserClick(userId) {
    $.get(`/invite/${window.localStorage.getItem('channelId')}/${userId}`, function (data) {
        // console.log(data)
        $(`#invite-btn-${userId}`).css('visibility', 'hidden')
    });
}

function displayReceivers(roommates) {

    roommates.forEach((roommate) => {
        if (roommate.id !== parseInt(window.localStorage.getItem('userId'))) {
            if (!$(`#receiver-${roommate.id}`).length) {
                $("#receiver-users").append(
                    `<option value="${roommate.id}" id="receiver-${roommate.id}">${roommate.name}</option>`
                )
            }
        }

    })

}

function displayAllUserList(data, roommates) {
    $("#channel-user-list").css("display", "none")
    $("#all-user-list").css("display", "block")
    $("#all-user-list").empty()

    // console.log(roommates)
    const roommatesIds = roommates.map((roommate)=> {
        return roommate.id
    })
    // console.log(data)

    data.forEach((d)=>{
        console.log(d)
        if (d.id !== parseInt(window.localStorage.getItem('userId'))) {
            if (roommatesIds.includes(d.id)) {
                $("#all-user-list").append(
                    '<div class="user">' +
                    '<div class="avatar">' +
                    '<img src="https://bootdey.com/img/Content/avatar/avatar1.png" alt="User name" />' +
                    '<div class="status off" style="visibility: hidden"></div>' +
                    '</div>' +
                    `<div class="name">${d.name}</div>` +
                    `<button id="invite-btn-${d.id}" style="border-radius: 5px; visibility: hidden"> Invite </button>` +
                    '</div>'

                )
            } else {
                $("#all-user-list").append(
                    '<div class="user">' +
                    '<div class="avatar">' +
                    '<img src="https://bootdey.com/img/Content/avatar/avatar1.png" alt="User name" />' +
                    '<div class="status off" style="visibility: hidden"></div>' +
                    '</div>' +
                    `<div class="name">${d.name}</div>` +
                    `<button id="invite-btn-${d.id}" onclick="inviteUserClick(${d.id})" style="border-radius: 5px"> Invite </button>` +
                    '</div>'

                )
            }
        }

    })

}

/**
 * Entry point into chat room
 */
window.onload = function() {
    // local is http should use ws://
    // deploy to heroku should change as wss://
    // webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatapp?userId=" + window.localStorage.getItem('userId'))
    //
    webSocket.onclose = () => alert("WebSocket connection closed");
    webSocket.onmessage = (msg) => updateChatRoom(msg);


};

// $("#msgBtn").click(sendMessage($("#inputMsg").val()));
$("#btn-message").click(() => {

    sendMessage($("#input-message").val())
})

/**
 * Send a message to the server.
 * @param msg  The message to send to the server.
 */
function sendMessage(msg) {
    if (msg !== "") {

        today = new Date();
        date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
        time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
        dateTime = date + ' ' + time;   //2018-8-3 11:12:40

        let msgValues;

        let receiver = $("#receiver-users").val();
        if (receiver === "all") {
            msgValues = {
                senderId: window.localStorage.getItem('userId'),
                receiverId: '0',
                channelId: window.localStorage.getItem('channelId'),
                time: dateTime, //2018-8-3 11:12:40
                content: msg
            };
        } else {
            console.log(receiver);
            console.log(typeof receiver);
            msgValues = {
                senderId: window.localStorage.getItem('userId'),
                receiverId: receiver,
                channelId: window.localStorage.getItem('channelId'),
                time: dateTime, //2018-8-3 11:12:40
                content: msg
            };
            console.log(msgValues);
        }


        webSocket.send(JSON.stringify(msgValues));

    }
    $("#input-message").val('');
    $("#input-message")[0].emojioneArea.setText('')
}

function sendEditedMessage(msg, idNum) {
    let msgValues;
    today = new Date();
    date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
    time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
    dateTime = date + ' ' + time;   //2018-8-3 11:12:40

    if (msg !== "") {
        msgValues = {
            senderId: window.localStorage.getItem('userId'),
            receiverId: window.localStorage.getItem('userId'),
            channelId: window.localStorage.getItem('channelId'),
            time: dateTime, //2018-8-3 11:12:40
            content: `${idNum}${msg}`
        }
        webSocket.send(JSON.stringify(msgValues));
        $("#message-invalid-feedback").css("display", "none");


        $("#input-message").val('');
        $("#input-message")[0].emojioneArea.setText('');
    } else {
        $("#message-invalid-feedback").css("display", "block");

    }

}

function sendRecalledMessage(msg, idNum) {
    let msgValues;
    today = new Date();
    date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
    time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
    dateTime = date + ' ' + time;   //2018-8-3 11:12:40



    msgValues = {
        senderId: window.localStorage.getItem('userId'),
        receiverId: window.localStorage.getItem('userId'),
        channelId: window.localStorage.getItem('channelId'),
        time: dateTime, //2018-8-3 11:12:40
        content: `${idNum}`
    }

    webSocket.send(JSON.stringify(msgValues));


    $("#input-message").val('');
    $("#input-message")[0].emojioneArea.setText('');



}



function recallMsg(idNum) {
    sendRecalledMessage("", idNum)

}

function deleteMsg(idNum) {
    let message_id = `message-${idNum}`;
    $(`#${message_id}`).empty();


}

function editMsg(idNum) {
    let msg = $("#input-message").val();

    sendEditedMessage(msg, idNum);

}




function displayChatMessage(chatData) {
    let sender;
    let receiver;
    $.get(`/getAllUsers`, function (data) {
        //console.log(JSON.parse(data))
        sender = JSON.parse(data).filter(user => user.id === chatData.senderId)[0];
        receiver = JSON.parse(data).filter(user => user.id === chatData.receiverId)[0];
        // console.log(sender);
        // console.log(receiver);
        //
        // console.log(chatData);
        // // chatData {
        //     channelId: 0
        //     messageContent: "fasfsd "
        //     messageId: 5
        //     messageTime: "Nov 5, 2021, 6:42:50 PM"
        //     messageType: "message"
        //     receivable: true
        //     receiverId: 0
        //     senderId: 1
        // }
        let id = `text-${chatData.messageId}`;
        let message_id = `message-${chatData.messageId}`;
        if (parseInt(window.localStorage.getItem("userId")) === chatData.senderId) {
            if (chatData.messageType === "directMessage") {

                $("#message-area-id").append(
                    `<div  id =${message_id} class="answer right">` +
                    `<div class="direct-message">` +
                    `<div class="avatar">` +
                    `<img src="https://bootdey.com/img/Content/avatar/avatar6.png" alt="User name" />` +
                    `<div class="status offline"></div>` +
                    `</div>` +
                    `<div class="name">${window.localStorage.getItem("username")}</div>` +
                    `<div class="message-type">(Private to you and ${receiver.name})<div>` +
                    `<div id=${id} class="text">${chatData.messageContent}</div>` +
                    `<div>` +
                    `<button id=${chatData.messageId} onclick="recallMsg(this.id)"> recall </button>` +
                    `<button id=${chatData.messageId} onclick="deleteMsg(this.id)"> delete </button>` +
                    `<button id=${chatData.messageId} onclick="editMsg(this.id)"> edit </button>` +
                    `</div>` +
                    `<div class="time">` + time + `</div>` +
                    `<div>` +
                    `</div>`
                )

            } else {
                $("#message-area-id").append(
                    `<div id =${message_id} class="answer right">` +
                    `<div class="avatar">` +
                    `<img src="https://bootdey.com/img/Content/avatar/avatar6.png" alt="User name" />` +
                    `<div class="status offline"></div>` +
                    `</div>` +
                    `<div class="name">${window.localStorage.getItem("username")}</div>` +
                    `<div id=${id} class="text">${chatData.messageContent}</div>` +
                    `<div>` +
                    `<button id=${chatData.messageId} onclick="recallMsg(this.id)"> recall </button>` +
                    `<button id=${chatData.messageId} onclick="deleteMsg(this.id)"> delete </button>` +
                    `<button id=${chatData.messageId} onclick="editMsg(this.id)"> edit </button>` +
                    `</div>` +
                    `<div class="time">` + time + `</div>` +
                    `</div>`
                )

            }

        } else {
            if (chatData.messageType === "directMessage") {
                $("#message-area-id").append(
                    `<div id =${message_id} class="answer left">` +
                    `<div class="direct-message">`
                    +
                    `<div class="avatar">` +
                    `<img src="https://bootdey.com/img/Content/avatar/avatar2.png" alt="User name" />` +
                    `<div class="status offline"></div>` +
                    `</div>` +
                    `<div class="name">` + sender.name + `</div>` +
                    `<div class="message-type">(Private to ${sender.name}  and you)<div>` +
                    `<div id=${id} class="text">${chatData.messageContent}</div>` +
                    `<button id=${chatData.messageId} onclick="recallMsg(this.id)"> recall </button>` +
                    `<button id=${chatData.messageId} onclick="deleteMsg(this.id)"> delete </button>` +
                    `<button id=${chatData.messageId} onclick="editMsg(this.id)"> edit </button>` +
                    `<div class="time">` + time + `</div>` + `<div>` +
                    `</div>`
                )
            } else {
                $("#message-area-id").append(
                    `<div id =${message_id} class="answer left">` +
                    `<div class="avatar">` +
                    `<img src="https://bootdey.com/img/Content/avatar/avatar2.png" alt="User name" />` +
                    `<div class="status offline"></div>` +
                    `</div>` +
                    `<div class="name">` + sender.name + `</div>` +
                    `<div id=${id} class="text">${chatData.messageContent}</div>` +
                    `<button id=${chatData.messageId} onclick="recallMsg(this.id)"> recall </button>` +
                    `<button id=${chatData.messageId} onclick="deleteMsg(this.id)"> delete </button>` +
                    `<button id=${chatData.messageId} onclick="editMsg(this.id)"> edit </button>` +
                    `<div class="time">` + time + `</div>` +
                    `</div>`)
            }

        }
    })


}



/**
 * Update the chat room with a message.
 * @param message  The message to update the chat room with.
 */
function updateChatRoom(message) {
    // convert the data to JSON and use .append(text) on a html element to append the message to the chat area
    let dataParse = JSON.parse(JSON.stringify(message.data));
    console.log(dataParse);


    try {
        let data = JSON.parse(dataParse);
        if (data && typeof data === "object") {
            if (data.messageType === "editMessage") {
                if (data.editContent !== "") {
                    let text_id = `text-${data.trackId}`;
                    $(`#${text_id}`)[0].innerText = data.editContent;

                } else {
                    let message_id = `message-${data.trackId}`;
                    $(`#${message_id}`).empty();
                }


            } else {
                if (data.receivable) {
                    displayChatMessage(data)
                }

            }


        }
    } catch (e) {
        displayNotification(dataParse);
    }
}

function displayNotification(notiData) {
    $("#message-area-id").append(
        `<div class="history-message">
            <div class="text">${notiData}</div>
        </div>`)
}


//
// if(window.localStorage.getItem("userId") == messageObject.senderId) {
//     $("#answer-right").append(
//         '<div class="avatar">'+
//         '<img'+
//         'src="https://bootdey.com/img/Content/avatar/avatar1.png"'+
//         'alt=userName'+
//         '/>'+
//         '<div class="status offline"></div>'+
//         '</div>'+
//         '<div class="name">'+ userName +'</div>'+
//         '<div class="text">'+
//         messageObject.content +
//         '</div>'+
//         '<div class="time"> </div>'+
//         '</div>'
//     );
// } else {
//     $("#answer-left").append(
//         '<div class="avatar">'+
//         '<img'+
//         'src="https://bootdey.com/img/Content/avatar/avatar1.png"'+
//         'alt="User name"'+  //sender name
//         '/>'+
//         '<div class="status offline"></div>'+
//         '</div>'+
//         '<div class="name">Jiacheng Sun</div>'+ //sender name
//         '<div class="text">'+
//         messageObject.content +
//         '</div>'+
//         '<div class="time"> </div>'+
//         '</div>'
//     );
// }
