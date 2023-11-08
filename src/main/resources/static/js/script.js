'use strict';
const buttons = document.querySelectorAll('button');

// Iterate over the selected buttons and add event listeners
buttons.forEach(function (button) {
    button.addEventListener('mousedown', function () {
        this.classList.add('pressed');
    });

    button.addEventListener('mouseup', function () {
        this.classList.remove('pressed');
    });
});

// deletes image element from post__body if there is no image
const image = document.querySelector(".post>img");
// console.log(image.getAttribute("src"));
// todo update condition accordingly
if (image.getAttribute("src") === "#" || image.getAttribute("src") === "")
    document.querySelector(".post>img").style.display = 'none';


// open login/register form after pressing login icon
function openModal() {
    document.getElementById('overlay').style.display = 'block';
}

function closeModal() {
    document.getElementById('overlay').style.display = 'none';
}

// check if user is logged in
document.querySelectorAll('.check-login').forEach(function (button) {
    button.addEventListener('click', function () {
        fetch('/check-login', {
            method: 'GET',
            credentials: 'include'  // Include credentials (cookies) in the request
        })
            .then(response => {
                if (response.ok) {
                    // User is logged in
                    console.log('User is logged in');
                } else {
                    // User is not logged in
                    console.log('User is not logged in');
                    openModal();
                }
            })
            .catch(error => console.error('Error:', error));
    });
});


const form = document.querySelector('#login-register');

// changes registration form label
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
form.addEventListener('submit', async (ev) => {
    ev.preventDefault();

    const dataToSend = {
        username: document.querySelector('#username').value,
        password: document.querySelector('#password').value
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

    // TODO in frontend inform about success register/login

    if (response.ok) {
        const data = await response.json();
        // here is possible to process data
        console.log(`${data.username} successfully registered!`);
        console.log(data);
    } else {
        const errorMessage = await response.text();
        console.error(errorMessage)
    }
});