'use strict';

// open login/register form after pressing login icon
function openLoginForm() {
    document.getElementById('overlay__login').style.display = 'block';
}

function closeLoginForm() {
    document.getElementById('overlay__login').style.display = 'none';
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

// if not logged in, ask for login
async function checkLogin() {
    const response = await fetch('/check-login', {credentials: 'include'}) // Include credentials (cookies) in the request
    if (response.ok)
        return true;
    else {
        console.log(`User is not logged in, opening register window.`);
        openLoginForm();
    }
}

// changes registration form label
const loginForm = document.querySelector('#login-register');

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
loginForm.addEventListener('submit', async (ev) => {
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
        if (login.checked) { // wants to log in & is registered
            const response2 = await fetch('/check-login', {credentials: 'include'})
            if (!response2.ok) { // wants to log in & is not registered
                console.error('User not registered!');
                inform(0); // 0 = user not registered
                return;
            }
            closeLoginForm();
            loadLoginIcon();
            await updateVotingIcons();
            updateUserDetailWindow(username);
            console.log(`${username} was successfully logged in :)`);
            inform(1); // 1 = logged in
            username = '';
            password = '';
        } else { // wants to register
            // const data = await response.json();
            console.log(`${username} was successfully registered :)`);
            inform(2); // 2 = registered
            // closeLoginForm();
            // openUserDetails();
            // console.log(data);
        }
    } else {
        const errMsg = await response.text();
        console.error(errMsg);
        inform(3, errMsg);
    }
});

function updateUserDetailWindow(username) {
    document.querySelector("#overlay__registration-details > form > span.register-details__label").textContent = username + ' details';
    document.querySelector("#overlay__registration-details > form > span.register-info > strong").textContent = username;
}

async function openUserDetails() {
    if (await checkLogin())
        document.getElementById('overlay__registration-details').style.display = 'block';
    // document.getElementById('date-of-birth').value = new Date().toISOString().substring(0, 10);
}

function closeUserDetails() {
    document.getElementById('overlay__registration-details').style.display = 'none';
}

// changing login icon after logging in
function loadLoginIcon() {
    document.querySelector('.header>button').innerHTML =
        `<svg class="icon icon&#45;&#45;primary" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
           <g stroke="#000" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
             <path d="M16 7a4 4 0 1 1-8 0 4 4 0 0 1 8 0ZM12 14a7 7 0 0 0-7 7h14a7 7 0 0 0-7-7Z" style="&#45;&#45;darkreader-inline-stroke:#747372"/>
           </g>
         </svg>`;
}

// TODO add profile picture to user details
// saving user details at login (after registration)
const detailsElement = document.querySelector('#overlay__registration-details form');
detailsElement.addEventListener('submit', async (ev) => {
    ev.preventDefault();
    console.log(`Saving details not implemented yet...`);

    // const response = await fetch()
});

// submitting logout
async function logout() {
    const response = await fetch('/logout', {method: 'POST',});
    if (response.ok)
        window.location.reload();
    console.log(await response.text());
}