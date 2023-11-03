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
