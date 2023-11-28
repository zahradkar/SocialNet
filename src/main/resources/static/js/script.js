'use strict';
//display all published posts after opening the app
fetch('http://localhost:8080/posts/all')
    .then(response => {
        if (response.ok)
            return response.json();
        return Promise.reject(new Error(`Obtaining all posts failed!`));
    })
    .then(data => {
        console.log(`All posts:`);
        console.log(data);
        for (let i = 0; i < data.length; i++)
            placePost(data[i]);
        addPressedButtonColor();
    }).catch(error => console.error(error.message));

// Add class 'pressed' to every button when pressed and vice versa
function addPressedButtonColor() {
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

//updates voting icons color after login
async function updateVotingIcons() {
    const response = await fetch(`http://localhost:8080/posts/loggedUser`);
    if (!response.ok) {
        console.error(await response.text());
        return;
    }
    const data = await response.json();
    console.log(data);
    for (let i = 0; i < data.upVotedIds.length; i++)
        document.querySelector(`#post${data.upVotedIds[i]} > div.post__foot > div > button.btn-upvote.btn--round.color--primary.check-login > svg`).setAttribute('fill', '#1eff1e');
    for (let i = 0; i < data.downVotedIds.length; i++)
        document.querySelector(`#post${data.downVotedIds[i]} > div.post__foot > div > button.btn-downvote.btn--round.color--primary.check-login > svg`).setAttribute('fill', 'red');
}

// checks if user is logged in after loading/refreshing page
fetch('/check-login', {credentials: 'include'}) // Include credentials (cookies) in the request
    .then(async response => {
        console.log('Loading page...');
        if (response.ok) {
            console.log('detected: user is logged in');
            loadLoginIcon();
            await updateVotingIcons();
        } else
            console.log('detected: user is not logged in');
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

async function openNewPostWindow() {
    if (await checkLogin())
        document.querySelector('#overlay-post').style.display = 'block';
}

// if not logged in, ask for login
async function checkLogin() {
    const response = await fetch('/check-login', {credentials: 'include'}) // Include credentials (cookies) in the request
    if (response.ok)
        return true;
    else {
        console.log(`User is not logged in, opening register window.`);
        openRegisterForm();
    }
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
    const confirmPassword = document.querySelector('#confirm-password').value;
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
        if (password !== confirmPassword) {
            console.error(`Passwords do not match!`);
            return;
        }
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
            const response2 = await fetch('/check-login', {credentials: 'include'})
            if (!response2.ok) {
                console.error('User not registered!');
                inform(0); // 0 = user not registered
                return;
            }
            inform(1); // 1 = logged in
            closeRegisterForm();
            console.log(`${username} was successfully logged in :)`);
            loadLoginIcon();
            await updateVotingIcons();
            x
            username = '';
            password = '';
        } else {
            const data = await response.json();
            // here is possible to process data
            console.log(`${username} was successfully registered :)`);
            inform(2); // 2 = registered
            console.log(data);
        }
    } else {
        const errMsg =await response.text();
        console.error(errMsg);
        inform(3, errMsg);
    }
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
async function vote(id, direction) {
    if (await checkLogin()) {
        let response;
        switch (direction) {
            case 'up':
                console.log('Upvoting post ID:', id);
                response = await fetch(`http://localhost:8080/posts/${id}/upvote`, {method: 'POST'});
                if (response.ok) {
                    const data = await response.json();
                    console.log(data);
                    if (data.status) {
                        // document.querySelector(`#post${id} .thumb-up`).classList.add('upvoted');
                        document.querySelector(`#post${id} .thumb-up`).setAttribute('fill', '#1eff1e');

                        document.querySelector(`#a${id}`).innerText++;
                    } else {
                        // document.querySelector(`#post${id} .thumb-up`).classList.remove('upvoted');
                        document.querySelector(`#post${id} .thumb-up`).setAttribute('fill', 'white');
                        document.querySelector(`#a${id}`).innerText--;
                    }
                } else
                    console.error(await response.text());
                break;
            case 'down':
                console.log('Downvoting post ID:', id);
                response = await fetch(`http://localhost:8080/posts/${id}/downvote`, {method: 'POST'});
                if (response.ok) {
                    const data = await response.json();
                    console.log(data);
                    if (data.status) {
                        // document.querySelector(`#post${id} .thumb-down`).classList.add('downvoted');
                        document.querySelector(`#post${id} .thumb-down`).setAttribute('fill', 'red');
                        document.querySelector(`#a${id}`).innerText--;
                    } else {
                        // document.querySelector(`#post${id} .thumb-down`).classList.remove('downvoted');
                        document.querySelector(`#post${id} .thumb-down`).setAttribute('fill', 'white');
                        document.querySelector(`#a${id}`).innerText++;
                    }
                } else
                    console.error(await response.text());
                break;
        }
    }
}

// displaying informative messages
function inform(msgCode, msgTxt) {
    const infoElement = document.querySelector('.message');
    switch (msgCode) {
        case 0:
            infoElement.style.color = 'red';
            infoElement.textContent = 'User not found!';
            break;
        case 1:
            infoElement.style.color = 'green';
            infoElement.textContent = 'Logged in!';
            break;
        case 2:
            infoElement.style.color = '#1EFF1E';
            infoElement.textContent = 'Registered!';
            break;
        case 3:
            infoElement.style.color = 'red';
            infoElement.textContent = msgTxt;
            break;
        default:
            return;
    }
    infoElement.style.display = 'block';
    infoElement.style.opacity = 1;
// todo improve
    // Wait for 1 second and then fade out
    setTimeout(function () {
        infoElement.style.opacity = 0;
    }, 750);
    setTimeout(function () {
        // infoElement.style.opacity = 0;
        infoElement.style.display = 'none';
    }, 1750);
}