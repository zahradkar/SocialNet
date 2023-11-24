'use strict';
// creating preview after pasting URL in new post
const isValidUrl = urlString => {
    const urlPattern = new RegExp('^(https?:\\/\\/)?' + // validate protocol
        '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // validate domain name
        '((\\d{1,3}\\.){3}\\d{1,3}))' + // validate OR ip (v4) address
        '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // validate port and path
        '(\\?[;&a-z\\d%_.~+=-]*)?' + // validate query string
        '(\\#[-a-z\\d_]*)?$', 'i'); // validate fragment locator
    return !!urlPattern.test(urlString);
}

function isImage(url) {
    const ext = url.split('.').pop();
    return ['jpg', 'jpeg', 'png', 'gif'].indexOf(ext) !== -1;
}

const textarea = document.querySelector('.post-new__body-textarea');

async function processImage(url) {
    const receivedData = await sendData("http://localhost:8080/image", 'POST', {'Content-Type': 'application/json'}, JSON.stringify({url: url}));
}

function scrapData() {
    setTimeout(async () => {
        let url = textarea.value;
        const preview = document.querySelector('.preview');
        console.log("is valid url: " + isValidUrl(url));
        if (isValidUrl(url)) {
            const prev = document.createElement('div');
            prev.classList.add("preview");
            if (url.startsWith("https://") && isImage(url)) {
                prev.innerHTML = `<img alt="#" class="preview__img" src="${url}"/>`;
                document.querySelector('.post__body').appendChild(prev);
            } else {
                if (!url.startsWith("http"))
                    url = "https://" + url;

                const response = await fetch("http://localhost:8080/scrap", {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({url: url})
                });

                if (response.ok) {
                    const receivedData = await response.json();
                    if (receivedData.image !== '')
                        prev.innerHTML = `<img alt="presentation image of inserted webpage" class="preview__img" src="${receivedData.image}"/>`;
                    prev.innerHTML += `
                      <p class="preview__title">${receivedData.title}</p>
                      <p class="preview__description">${receivedData.description}</p>
                      <p class="preview__url">${receivedData.url}</p>`;
                    if (preview) {
                        if (preview.innerHTML === prev.innerHTML) {
                            console.log('preview = prev')
                            return;
                        }
                        erase();
                    }
                    document.querySelector('.post__body').appendChild(prev);
                } else {
                    if (preview)
                        erase();
                    console.error(await response.text());
                }
            }
        } else if (preview)
            erase();
    }, 0);
}

const timeoutDuration = 200; // Define the debouncing timeout duration in milliseconds
let timeoutId;

textarea.addEventListener('input', () => {
    clearTimeout(timeoutId); // Clear previous timeout if any

    timeoutId = setTimeout(() => {
        // Perform action after timeout duration has elapsed
        console.log('User has stopped typing');
        scrapData();
    }, timeoutDuration);
});

function erase() {
    const prev = document.querySelector('.preview');
    console.log('trying to delete preview component in window for new post');
    // if (textarea.value === '')
    prev.parentNode.removeChild(prev);
}