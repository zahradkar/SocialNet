'use strict';
//display all published posts after opening the app
/*fetch('http://localhost:8080/posts/all')
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
    }).catch(error => console.error(error.message));*/
loadPosts();
async function loadPosts() {
    const response = await fetch('http://localhost:8080/posts/all')
    if (response.ok) {
        const data = await response.json();
        console.log(`All posts:`);
        console.log(data);
        for (let i = 0; i < data.length; i++)
            placePost(data[i]);
        addPressedButtonColor();
    }
    else
        console.log(await response.text());
}

// loads data after reloading page (only if user is logged in)
loadDataFromDB();
async function loadDataFromDB() {
    const response = await fetch('/check-login', {credentials: 'include'}) // Include credentials (cookies) in the request

    if (response.ok) { // it is not necessary handle !response.ok
        const username = await response.text();
        await updateUserDetailWindow(username);
        loadLoginIconAndProfilePicture();
        await updateVotingIcons();
        // updateProfilePictures();
        updateNameInNewPost(username);
    }
}

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

// displays informative messages
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
        case 3: // awaiting negative response
            infoElement.style.color = 'red';
            infoElement.textContent = msgTxt;
            break;
        case 4: // awaiting positive response
            infoElement.style.color = 'green';
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

// create name(label) from first and last name
function getLabel(first, last, username) {
    return first + ' ' + last || username;
}