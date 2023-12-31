'use strict';
let oldFirstName;
let oldLastName;
// let oldPhoto;
// let oldUsername;

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

    if (login.checked)
        console.log('logging in...');
    else {
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

    if (response.ok) {
        // console.log("response status:");
        // console.log(response.status);
        if (login.checked) { // wants to log in & is registered
            const response2 = await fetch('/check-login', {credentials: 'include'})
            if (!response2.ok) { // wants to log in & is not registered
                // console.log("response2 status:");
                // console.error(response2.status);
                const errMsg = await response2.text();
                console.error(errMsg);
                inform(3, errMsg);
                return;
            }
            closeLoginForm();
            await updateVotingIcons();
            await updateUserDetailWindow(username);
            loadLoginIconAndProfilePicture(); // toto musi byt az po updateUserDetailWindow
            updateNameInNewPost(username);
            console.log(`${username} was successfully logged in :)`);
            inform(1); // 1 = logged in
            username = '';
            password = '';
        } else { // wants to register
            // json (from backend) has no data (return type = void)
            console.log(`${username} was successfully registered :)`);
            inform(2); // 2 = registered
        }
    } else {
        const errMsg = await response.text();
        console.error(errMsg);
        // console.log("response status:");
        // console.log(response.status);
        inform(3, errMsg);
    }
});

function updateNameInNewPost(username) {
    const firstName = document.querySelector("#first-name").value;
    const lastName = document.querySelector("#last-name").value;
    let nameElement = document.querySelector("#post-new .post__id");
    nameElement.textContent = !firstName && !lastName ? username : `${firstName} ${lastName}`;
}

async function updateUserDetailWindow(username) {
    document.querySelector("#overlay__registration-details .register-details__label").textContent = username + ' details';
    document.querySelector("#overlay__registration-details .register-info > strong").textContent = username;
    const response = await fetch(`http://localhost:8080/users/getDetails`);
    if (!response.ok) {
        console.error(await response.text());
        return;
    }
    console.log('Expecting json data:');
    const data = await response.json();
    console.log(data);
    document.getElementById('first-name').value = data.firstName;
    document.getElementById('last-name').value = data.lastName;
    document.getElementById('e-mail').value = data.email;
    document.getElementById('photoURL').value = data.profilePictureURL;
    document.getElementById('location').value = data.location;
}

async function openUserDetails() {
    if (await checkLogin())
        document.getElementById('overlay__registration-details').style.display = 'block';
    // document.getElementById('date-of-birth').value = new Date().toISOString().substring(0, 10);
    oldFirstName = document.querySelector("#first-name").value;
    oldLastName = document.querySelector("#last-name").value;
    // oldPhoto = document.querySelector("#photoURL").value;
    // oldUsername = document.querySelector("#overlay__registration-details .register-info strong").textContent;
    // console.log("detected oldUsername: " + oldUsername);
}

function closeUserDetails() {
    document.getElementById('overlay__registration-details').style.display = 'none';
}

// changing login icon after logging in
function loadLoginIconAndProfilePicture() {
    /*document.querySelector('.header>button').innerHTML =
        `<svg class="icon icon&#45;&#45;primary" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
           <g stroke="#000" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
             <path d="M16 7a4 4 0 1 1-8 0 4 4 0 0 1 8 0ZM12 14a7 7 0 0 0-7 7h14a7 7 0 0 0-7-7Z" style="&#45;&#45;darkreader-inline-stroke:#747372"/>
           </g>
         </svg>`;*/
    const photo = document.querySelector("#photoURL").value || `images/user.svg`;
    document.querySelector('.header>button').innerHTML = `<img class="profile-photo" src="${photo}" alt="user's photo picture"/>`;
    document.querySelector("#post-new > div.post__head > picture > img").setAttribute('src', `${photo}`);
    if (photo === `images/user.svg`) // sets up size of log in button picture
        // TODO i don't like this approach; consider improvement
        document.querySelector('.header>button').innerHTML = `<img style="height: 25px; width: 25px" src="${photo}" alt="user's profile picture"/>`;
    else
        document.querySelector('.header>button').innerHTML = `<img style="height: 40px; width: 40px; border-radius: 50%" src="${photo}" alt="user's profile picture"/>`;
}

// saving user details at login (after registration)
const detailsElement = document.querySelector('#overlay__registration-details form');

detailsElement.addEventListener('submit', async (ev) => {
    ev.preventDefault();

    const firstName = document.getElementById('first-name').value;
    const lastName = document.getElementById('last-name').value;
    const email = document.getElementById('e-mail').value || undefined;
    const profilePictureURL = document.getElementById('photoURL').value || undefined;
    const birthday = document.getElementById('date-of-birth').value || undefined;
    const location = document.getElementById('location').value || undefined;

    if (!firstName && !lastName && !email && !profilePictureURL && !location && !birthday) {
        console.error(`You can't submit empty form!`);
        // TODO improve
        inform(3, `You can't submit empty form!`);
        return;
    }
    console.log(`Saving details...`);

    const response = await fetch(`http://localhost:8080/users/setDetails`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            firstName: firstName,
            lastName: lastName,
            email: email,
            profilePictureURL: profilePictureURL,
            birthday: birthday,
            location: location
        })
    });

    if (!response.ok) {
        const errMsg = await response.text();
        console.error(errMsg);
        inform(3, errMsg);
        return;
    }
    // const data = await response.json();
    // console.log("data from backend: ");
    // console.log(data);

    const username = document.querySelector("#overlay__registration-details .register-info > strong").textContent || author;
    document.querySelector("#post-new .post__id").textContent = getLabel(firstName, lastName, username); // if user has updated his details, updates name in new post
    updatePageUponUpdatedUserDetails(getLabel(oldFirstName, oldLastName, username), getLabel(firstName, lastName, username)); // if user has updated his details, updates name in published posts
    console.log('saved!');
    inform(4, 'Saved!');
});

function updatePageUponUpdatedUserDetails(oldName, newName) {
    console.log('oldName: ' + oldName);
    console.log('newName: ' + newName);
    const newPhoto = document.querySelector("#photoURL").value || `images/user.svg`;
    const postElements = document.querySelectorAll('.post');
    // console.log('oldPhoto: ' + oldPhoto);
    console.log('newPhoto: ' + newPhoto);

    let loginButtonPicture = document.querySelector('.header button img');
    console.log(loginButtonPicture);
    if (newPhoto === `images/user.svg`) // sets up size of log in button picture
        // TODO i don't like this approach; consider improvement
        document.querySelector('.header>button').innerHTML = `<img style="height: 25px; width: 25px" src="${newPhoto}" alt="user's profile picture"/>`;
    else
        document.querySelector('.header>button').innerHTML = `<img style="height: 40px; width: 40px; border-radius: 50%" src="${newPhoto}" alt="user's profile picture"/>`;

    // todo consider improving condition in the loop (get rid of -1)
    for (let i = 0; i < postElements.length - 1; i++) {
        if (postElements[i].querySelector('.post__username').textContent === oldName) {
            postElements[i].querySelector('.post__username').textContent = newName;
            postElements[i].querySelector(".profile-photo").setAttribute('src', `${newPhoto}`);
        }
    }
}

// processing logout request
async function logout() {
    const response = await fetch('/logout', {method: 'POST',});
    if (response.ok)
        window.location.reload();
    console.log(await response.text());
}

// processing delete account request
async function deleteAccount() {
    console.log('delete account: FEATURE IN DEVELOPMENT...')

    const response = await fetch(`http://localhost:8080/users/delete`, {
        method: 'DELETE'
    });

    if (response.ok)
        console.log('User deleted!');
    else {
        console.error('Something went wrong while deleting a user!');
        console.error(await response.text());
    }
}
