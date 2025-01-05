let stompClient;
let username;
let lastTimestamp = null;
let lastSender = null;

// Fetch the username from the server
async function fetchUsername() {
    try {
        const response = await fetch('/api/auth/username', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
        });

        if (response.ok) {
            username = await response.text();
            connect(); // Connect to WebSocket
        } else {
            console.error('Failed to fetch username. Response:', response.status);
        }
    } catch (error) {
        console.error('Error fetching username:', error);
    }
}

0
function changeChatHeader(element) {
    const newName = element.getAttribute('data-name'); // Get the name from the clicked chat-item
    const profileNameElement = document.getElementById('profileName'); // Header name element

    // Update the chat header's name
    profileNameElement.textContent = newName;

    // Optionally, change the profile picture or status dynamically
    // For example:
    const profilePicElement = document.getElementById('profilePic');
    profilePicElement.style.backgroundColor = getRandomColor(); // Change avatar color dynamically
}
// Fetch previous messages

async function fetchMessages() {
    try {
        const response = await fetch('/api/messages', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
        });

        if (response.ok) {
            const messages = await response.json();
            const chatContainer = document.getElementById('chatMessages');
            chatContainer.innerHTML = ''; // Clear old messages

            messages.forEach((message) => {
                addMessageToChat(message);
            });

            // Auto-scroll to the latest message
            chatContainer.scrollTop = chatContainer.scrollHeight;
        } else {
            console.error('Failed to fetch messages. Status:', response.status);
        }
    } catch (error) {
        console.error('Error fetching messages:', error);
    }
}

function addMessageToChat(message) {
    const chatContainer = document.getElementById('chatMessages');
    const currentTimestamp = new Date(message.timestamp);
    const formattedTime = currentTimestamp.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

    // Add a timestamp if there's a time gap of 10 minutes or this is the first message
    if (!lastTimestamp || currentTimestamp - lastTimestamp > 10 * 60 * 1000) {
        const messageTime = document.createElement('div');
        messageTime.classList.add('message-time');
        messageTime.textContent = formattedTime;
        chatContainer.appendChild(messageTime);
    }

    lastTimestamp = currentTimestamp;

    // If the message is a join message, center it under the timestamp
    if (message.type === 'JOIN') {
        const joinMessage = document.createElement('div');
        joinMessage.classList.add('join-message');
        joinMessage.textContent = `${message.sender} joined the chat.`;
        joinMessage.style.textAlign = 'center';
        joinMessage.style.color = '#b9bbbe';
        joinMessage.style.margin = '10px 0';
        chatContainer.appendChild(joinMessage);
        return;
    }

    // Create the message element
    const messageElement = document.createElement('div');
    const isCurrentUser = message.sender === username;
    messageElement.classList.add('message', isCurrentUser ? 'right' : 'left');
    messageElement.dataset.sender = message.sender;

    // Create a container for the avatar and message content
    const avatarAndMessageContainer = document.createElement('div');
    avatarAndMessageContainer.style.display = 'flex';
    avatarAndMessageContainer.style.alignItems = 'flex-end';

    // Add avatar for left-aligned messages only if it is the latest message in a group
    const avatarElement = document.createElement('div');
    avatarElement.classList.add('avatar');
    avatarElement.style.width = '32px';
    avatarElement.style.height = '32px';
    avatarElement.style.marginRight = '10px';
    avatarElement.style.borderRadius = '50%';
    avatarElement.style.display = 'flex';
    avatarElement.style.alignItems = 'center';
    avatarElement.style.justifyContent = 'center';
    avatarElement.style.backgroundColor = getSenderColor(message.sender); // Assign unique color
    avatarElement.style.color = '#fff';
    avatarElement.textContent = getSenderInitials(message.sender); // Assign initials
    avatarElement.style.visibility = 'hidden';

    // Create a container for the username and message content
    const messageContentContainer = document.createElement('div');
    messageContentContainer.style.display = 'flex';
    messageContentContainer.style.flexDirection = 'column';
    messageContentContainer.style.alignItems = isCurrentUser ? 'flex-end' : 'flex-start';

    // Add username label for left-aligned messages if it's a new group
    const isNewGroup = lastSender !== message.sender;
    if (!isCurrentUser && isNewGroup) {
        const usernameLabel = document.createElement('div');
        usernameLabel.textContent = message.sender;
        usernameLabel.style.fontSize = '12px';
        usernameLabel.style.color = '#b9bbbe';
        usernameLabel.style.marginBottom = '4px';
        usernameLabel.style.marginTop = '45px';
        messageContentContainer.appendChild(usernameLabel);
    }

    // Add message content
    const messageContent = document.createElement('div');
    messageContent.classList.add('message-content', isCurrentUser ? 'right' : 'left');
    messageContent.textContent = message.content;
    messageContentContainer.appendChild(messageContent);

    // Append avatar and message content container based on alignment
    if (!isCurrentUser) {
        avatarAndMessageContainer.appendChild(avatarElement);
    }
    avatarAndMessageContainer.appendChild(messageContentContainer);

    messageElement.appendChild(avatarAndMessageContainer);
    chatContainer.appendChild(messageElement);

    // Update visibility of avatars
    updateAvatarVisibility(chatContainer);

    // Update last sender
    lastSender = message.sender;

    // Auto-scroll to the bottom
    chatContainer.scrollTop = chatContainer.scrollHeight;
}

function updateAvatarVisibility(chatContainer) {
    const messages = chatContainer.querySelectorAll('.message.left');

    messages.forEach((message, index) => {
        const currentSender = message.dataset.sender;
        const nextMessage = messages[index + 1];
        const isLastInGroup = !nextMessage || nextMessage.dataset.sender !== currentSender;
        const avatar = message.querySelector('.avatar');

        if (avatar) {
            if (isLastInGroup) {
                avatar.style.visibility = 'visible';
            } else {
                avatar.style.visibility = 'hidden';
            }
        }
    });
}

// Helper function to generate unique color for each sender
function getSenderColor(sender) {
    const colors = ['#FF5733', '#33FF57', '#3357FF', '#F4D03F', '#8E44AD'];
    let hash = 0;
    for (let i = 0; i < sender.length; i++) {
        hash = sender.charCodeAt(i) + ((hash << 5) - hash);
    }
    return colors[Math.abs(hash) % colors.length];
}

// Helper function to generate initials for each sender
function getSenderInitials(sender) {
    const names = sender.split(' ');
    return names.map(name => name.charAt(0).toUpperCase()).join('');
}



// Connect to WebSocket
function connect() {
    const socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, (error) => {
        console.error('WebSocket connection failed:', error);
    });
}

function onConnected() {
    stompClient.subscribe('/topic/public', onMessageReceived);

    const joinMessage = {
        sender: username,
        content: `${username} joined!`,
        type: 'JOIN',
    };

    stompClient.send('/app/chat.send', {}, JSON.stringify(joinMessage));
}

function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    addMessageToChat(message);
}

function sendMessage() {
    const messageInput = document.getElementById('messageInput');
    const messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        const userMessage = {
            sender: username,
            content: messageContent,
            type: 'CHAT',
            timestamp: new Date().toISOString(),
        };

        // Send the message through WebSocket
        stompClient.send('/app/chat.send', {}, JSON.stringify(userMessage));

        // Clear the input field
        messageInput.value = '';

        // If the message is for the chatbot, display it immediately
        if (messageContent.startsWith('@Chatbot')) {
            addMessageToChat(userMessage); // Show the user's message in the chat
        }
    }
}


// Attach send button functionality
document.getElementById('send-message').addEventListener('click', sendMessage);

// Enable sending message with Enter key
document.getElementById('messageInput').addEventListener('keydown', function (event) {
    if (event.key === 'Enter') {
        event.preventDefault(); // Prevent newline in input
        sendMessage();
    }
});



// Initialize message fetch on page load
document.addEventListener('DOMContentLoaded', () => {
    fetchMessages();
    fetchUsername(); // Ensure both username and messages are loaded
});