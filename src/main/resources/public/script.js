// Retrieve the stored username or prompt for one if not set.
let username = localStorage.getItem('username');
const usernameInput = document.getElementById('username-input');
const setUsernameButton = document.getElementById('set-username-button');

if (username) {
    usernameInput.value = username;
} else {
    username = prompt("Enter your username:");
    if (username) {
        localStorage.setItem('username', username);
        usernameInput.value = username;
    } else {
        username = "User";
    }
}

// Allow the user to update their username.
setUsernameButton.addEventListener('click', () => {
    const inputName = usernameInput.value.trim();
    if (inputName) {
        username = inputName;
        localStorage.setItem('username', username);
        alert('Username updated to: ' + username);
    } else {
        alert('Please enter a valid username');
    }
});

const chatWindow = document.getElementById('chat-window');
const messageInput = document.getElementById('message-input');
const sendButton = document.getElementById('send-button');

const websocket = new WebSocket('ws://localhost:7000/chat');

websocket.onopen = () => {
    console.log('Connected to WebSocket server');
};

websocket.onmessage = (event) => {
    const message = JSON.parse(event.data);
    const messageElement = document.createElement('div');
    messageElement.textContent = `${message.sender}: ${message.text}`;
    chatWindow.appendChild(messageElement);
};

sendButton.addEventListener('click', () => {
    const messageText = messageInput.value;
    if (messageText) {
        const message = {
            sender: username,
            text: messageText
        };
        websocket.send(JSON.stringify(message));
        messageInput.value = '';
    }
});

window.addEventListener('load', () => {
    fetch('/messages')
        .then(response => response.json())
        .then(messages => {
            messages.forEach(message => {
                const messageElement = document.createElement('div');
                messageElement.textContent = `${message.sender}: ${message.text}`;
                chatWindow.appendChild(messageElement);
            });
        })
        .catch(error => console.error('Error fetching messages:', error));
});
