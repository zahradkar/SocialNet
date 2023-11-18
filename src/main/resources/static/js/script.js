'use strict';
const buttons = document.querySelectorAll('button');

// Add class 'pressed' to every button when pressed and vice versa
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

function createPost() {
    checkLogin()
        .then(response => {
            if (response.ok) {
                console.log('User is logged in');
                // TODO logic here

                document.querySelector('#overlay-post').style.display = 'block';
            } else {
                console.log('User is not logged in');
                openRegisterForm();
            }
        });
}

// if not logged in, ask for login
function checkLogin() {
    return fetch('/check-login', {
        method: 'GET',
        credentials: 'include'  // Include credentials (cookies) in the request
    });
}


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
            username = '';
            password = '';
            closeRegisterForm();
            loadLoginIcon();
            // console.log(`${username} was successfully logged in :)`);
        } else {
            const data = await response.json();
            // here is possible to process data
            console.log(data);
        }
    } else {
        const errorMessage = await response.text();
        console.error(errorMessage)
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

// creating preview after pasting URL in new post
const isValidUrl = urlString => {
    var urlPattern = new RegExp('^(https?:\\/\\/)?' + // validate protocol
        '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // validate domain name
        '((\\d{1,3}\\.){3}\\d{1,3}))' + // validate OR ip (v4) address
        '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // validate port and path
        '(\\?[;&a-z\\d%_.~+=-]*)?' + // validate query string
        '(\\#[-a-z\\d_]*)?$', 'i'); // validate fragment locator
    return !!urlPattern.test(urlString);
}

const textarea = document.querySelector('.post-new__body-textarea');
let exists = false;
textarea.addEventListener('paste', () => {
    // todo request authentication
    // todo consider post as a plain text
    setTimeout(() => {
        let url = textarea.value;
        if (isValidUrl(url)) {
            if (!url.startsWith("http"))
                url = "https://" + url;
            console.log("url: " + url);
            fetch("http://localhost:8080/scrap", {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({url: url})
            }).then(response => {
                if (response.ok)
                    return response.json();
                // console.log(response.json());
                else
                    console.error(response.text());
            });
        }
    }, 0);
});