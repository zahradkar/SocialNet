'use strict';

function colorPressedButton() {
    const buttons = document.querySelectorAll('button');
    buttons.forEach(function (button) {
        button.addEventListener('mousedown', function () {
            this.classList.add('pressed');
        });
        button.addEventListener('mouseup', function () {
            this.classList.remove('pressed');
        });
    });
}

// Add class 'pressed' to every button when pressed and vice versa
colorPressedButton();

// deletes image element from post__body if there is no image
const image = document.querySelector(".post>img");
// console.log(image.getAttribute("src"));
// todo update condition accordingly
if (image.getAttribute("src") === "#" || image.getAttribute("src") === "")
    document.querySelector(".post>img").style.display = 'none';


// open login/register form after pressing login icon
function openRegisterForm() {
    document.getElementById('overlay-login').style.display = 'block';
}

function closeRegisterForm() {
    document.getElementById('overlay-login').style.display = 'none';
}

// close new post window
function closeNewPostWindow() {
    document.getElementById('overlay-post').style.display = 'none';
}

// if user is logged in after loading/refreshing page
fetch('/check-login', {
    method: 'GET',
    credentials: 'include'  // Include credentials (cookies) in the request
})
    .then(response => {
        if (response.ok) {
            loadLoginIcon();
            console.log('User is logged in');
        } else
            console.log('User is not logged in');
    })
    .catch(error => console.error('Error:', error));

// check if user is logged in after pressing certain buttons
/*document.querySelectorAll('.check-login').forEach(function (button) {
    button.addEventListener('click', function () {
        fetch('/check-login', {
            method: 'GET',
            credentials: 'include'  // Include credentials (cookies) in the request
        })
            .then(response => {
                if (response.ok) {
                    console.log('User is logged in');
                } else {
                    console.log('User is not logged in');
                    openModal();
                }
            })
            .catch(error => console.error('Error:', error));
    });
});*/

function openNewPostWindow() {
    checkLogin()
        .then(response => {
            if (response.ok)
                // User is logged in -> display new post 'window'
                document.querySelector('#overlay-post').style.display = 'block';
            else
                // User is not logged in -> display register form
                openRegisterForm();
        });
}

// if not logged in, ask for login
function checkLogin() {
    return fetch('/check-login', {
        method: 'GET',
        credentials: 'include'  // Include credentials (cookies) in the request
    });
}

// changes registration form label
const registerForm = document.querySelector('#login-register');

function optionChanged() {
    const registrationLabel = document.querySelector("#login-register>button");
    const loginOption = document.querySelector("#login-option");
    const confirmPasswordDiv = document.querySelector('#visibility');
    if (loginOption.checked) {
        confirmPasswordDiv.style.visibility = 'hidden';
        registrationLabel.textContent = "Login";
    } else {
        registrationLabel.textContent = "Register";
        confirmPasswordDiv.style.visibility = 'visible';
    }
}

// registering/logging in
registerForm.addEventListener('submit', async (ev) => {
    ev.preventDefault();

    let username = document.querySelector('#username').value;
    let password = document.querySelector('#password').value;
    const dataToSend = {
        username: username,
        password: password
    };

    const login = document.querySelector('#login-option');
    let endpoint = `http://localhost:8080/login`;
    let type = 'application/x-www-form-urlencoded';
    let content = `username=${encodeURIComponent(dataToSend.username)}&password=${encodeURIComponent(dataToSend.password)}`;

    if (login.checked) {
        console.log('logging in...');
    } else {
        console.log('registering...');
        type = 'application/json';
        endpoint = `http://localhost:8080/users/register`;
        content = JSON.stringify(dataToSend);
    }

    const response = await fetch(endpoint, {
        method: 'POST',
        headers: {'Content-Type': type},
        body: content
    });
    // todo fix logging in: after fail attempt to log in, frontend behaves like the login was successful. That's an issue.
    if (response.ok) {
        if (login.checked) {
            closeRegisterForm();
            loadLoginIcon();
            console.log(`${username} was successfully logged in :)`);
            username = '';
            password = '';
        } else {
            const data = await response.json();
            // here is possible to process data
            console.log(`${username} was successfully registered :)`);
            console.log(data);
        }
    } else
        console.error(await response.text())
});

// changing login icon after logging in
function loadLoginIcon() {
    document.querySelector('.header>button').innerHTML =
        `<svg class="icon icon&#45;&#45;primary" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
           <g stroke="#000" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
             <path d="M16 7a4 4 0 1 1-8 0 4 4 0 0 1 8 0ZM12 14a7 7 0 0 0-7 7h14a7 7 0 0 0-7-7Z" style="&#45;&#45;darkreader-inline-stroke:#747372"/>
           </g>
          </svg>`;
}

// processing of down and upvote
async function vote(button, value) {
    const postElement = button.closest('.post');
    console.log('Upvoting post with ID:', postElement.id);
    let url;
    let dir;
    let response;

    switch (value) {
        case 'up':
            response = await fetch(`http://localhost:8080/posts/${postElement.id}/upvote`, {method: 'POST'});
            if (response.ok) {
                const data = await response.json();
                console.log(data);
                data.status ? document.querySelector('.likes-count').innerText++ : document.querySelector('.likes-count').innerText--;
            } else
                console.error(await response.text());
            break;
        case 'down':
            // todo test
            response = await fetch(`http://localhost:8080/posts/${postElement.id}/downvote`, {method: 'POST'});
            if (response.ok) {
                const data = await response.json();
                console.log(data);
                // TODO implement counting of dislikes
                data.status ? document.querySelector('.likes-count').innerText-- : document.querySelector('.likes-count').innerText++;
            } else
                console.error(await response.text());
            break;
    }
}

function handleUpVote(button) {
    // Find the parent post element
    const postElement = button.closest('.post');

    console.log('Upvoting post with ID:', postElement.id);
}


// general sendData function
async function sendData(url, method, headers, body) {
    const response = await fetch(url, {
        method: method,
        headers: headers,
        body: body
    });

    if (response.ok)
        return response.json();
    else
        return response.text();
}
