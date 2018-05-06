var userName = null;

function rememberName() {
    var errorStatus = document.getElementById("error");
    errorStatus.textContent = "";

    var nameElem = document.getElementById("name");
    var nameValue = nameElem.value;
    if(nameValue.length == 0) {
        errorStatus.textContent = "Name is required";
        return;
    }

    userName = nameValue;
    nameElem.disabled = true;
    nameElem.style.display = "none";

    var contentElem = document.getElementById("content")
    contentElem.disabled = false;
    contentElem.style.display = "block";
    contentElem.focus();
}

function postMessage() {
    var contentElem = document.getElementById("content");

    var errorStatus = document.getElementById("error");
    errorStatus.textContent = "";

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4) {
            if(this.status != 201) {
                errorStatus.textContent = "Message not sent: try again";
            } else if(this.status == 201) {
                errorStatus.textContent = "";
                contentElem.value = "";
            }
        }
    };

    var contentValue = contentElem.value;
    if(contentValue.length == 0) {
        errorStatus.textContent = "Message is required";
        contentElem.focus();
        return;
    }

    xhttp.open("POST", "messages", true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    xhttp.send(JSON.stringify({content: contentValue, from: userName}));
}

window.onload = function() {
    var eventSource = new EventSource("messages/stream");
    eventSource.addEventListener("message", function(e) {
        var messagesElem = document.getElementById("messages");
        messagesElem.style.display = "block";

        var newMessage = JSON.parse(e.data);

        var newMessageFromElem = document.createElement("span");
        newMessageFromElem.className += "messageFrom";
        newMessageFromElem.textContent = "[" + newMessage.from + "]";

        var newMessageContentElem = document.createElement("span");
        newMessageContentElem.className += "messageContent";
        newMessageContentElem.textContent = newMessage.content;

        var newMessageElem = document.createElement("p");
        newMessageElem.appendChild(newMessageFromElem);
        newMessageElem.appendChild(newMessageContentElem);
        messagesElem.appendChild(newMessageElem);
    }, false);
    eventSource.addEventListener("open", function(e) {
        console.log("Connection established");
    }, false);
    eventSource.addEventListener("error", function(e) {
        console.error("Connection lost");
    }, false);

    var nameElem = document.getElementById("name");
    nameElem.onkeypress = function(e) {
        if (!e) e = window.event;
        var keyCode = e.keyCode || e.which;
        if (keyCode == '13') {
            e.preventDefault();
            rememberName();
        }
    };

    var contentElem = document.getElementById("content");
    contentElem.disabled = true;
    contentElem.style.display = "none";
    contentElem.onkeypress = function(e) {
        if (!e) e = window.event;
        var keyCode = e.keyCode || e.which;
        if (keyCode == '13') {
            e.preventDefault();
            postMessage();
        }
    };

    var messagesElem = document.getElementById("messages");
    messagesElem.style.display = "none";
};

