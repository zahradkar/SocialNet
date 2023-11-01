const buttons = document.querySelectorAll('button');

// Iterate over the selected buttons and add event listeners
buttons.forEach(function(button) {
    button.addEventListener('mousedown', function() {
        this.classList.add('pressed');
    });

    button.addEventListener('mouseup', function() {
        this.classList.remove('pressed');
    });
});

// deletes image element from post__body if there is no image
const image = document.querySelector(".post__body>img");
console.log(image.getAttribute("src"));
// todo update condition accordingly
if (image.getAttribute("src") === "#" || image.getAttribute("src") === "")
    document.querySelector(".post__body").innerHTML = "";