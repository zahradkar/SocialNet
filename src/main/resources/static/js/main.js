/*const collapsibles = document.querySelectorAll(".collapsible");
collapsibles.forEach((item) =>
  item.addEventListener("click", function () {
    this.classList.toggle("collapsible--expanded");
  })
);*/


/*
const tlacidlo = document.querySelector(".media__body>button");
tlacidlo.forEach((item) =>
    item.addEventListener("click", function () {
        this.classList.toggle("pressed");
    })
);*/



// Select all buttons with the class "btn--primary"
const buttons = document.querySelectorAll('.btn--primary');

// Iterate over the selected buttons and add event listeners
buttons.forEach(function(button) {
    button.addEventListener('mousedown', function() {
        this.classList.add('pressed');
    });

    button.addEventListener('mouseup', function() {
        this.classList.remove('pressed');
    });
});
