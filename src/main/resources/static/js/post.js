'use strict';

// sending post data after pressing post button
async function sendPost() {
    let parsedTitle = document.querySelector('.post-new__body-textarea').value;
    if (parsedTitle === '') {
        console.error('Nothing to post...!');
        return;
    }

    let parsedContent;
    if (document.querySelector('.preview img'))
        parsedContent = document.querySelector('.preview img').getAttribute('src');
    console.log(parsedContent);

    const data = {
        title: parsedTitle,
        content: parsedContent
    };
    console.log('sending post data to the backend...');
    const response = await fetch("http://localhost:8080/posts/create", {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        console.error(await response.text());
        return; // TODO test
    }
    const receivedData = await response.json();
    console.log('received data:')
    console.log(receivedData);

    placePost(receivedData);
    addPressedButtonColor(); // TODO improve this line - it can be done simpler way
    closeNewPost();
}

// creating post div from received data and placing it to the page
function placePost(data) {
    if (document.querySelector('.preview img'))
        data.content = document.querySelector('.preview img').getAttribute('src');
    const newPost = document.createElement('div');
    newPost.classList.add("post");
    newPost.id = `post${data.postId}`;
    const photo = data.photoURL || `images/user.svg`;
    // todo update datetime element
    newPost.innerHTML += `
          <span class="close" onclick="deletePost(${data.postId})">&times;</span>
          <div class="post__head">
            <picture>
<!--              <source srcset="images/martin-50px.webp 1x, images/martin-100px.webp 2x, images/martin-200px.webp 3x" type="image/webp">-->
              <img alt="user's profile photo" class="profile-photo" src="${photo}">
            </picture>
            <span class="post__id"><span class="post__username">${data.author}</span><br>
              <span class="post__time"> <time datetime="2023-10-31T14:01:26">${new Date(data.createdAt).toLocaleString()}</time> </span>
            </span>
          </div>
          <div class="post__title">
            ${data.title}
          </div>`;
    if (data.content)
        newPost.innerHTML += `<img alt="posted image" src="${data.content}" width="580px">`;
    newPost.innerHTML += `
          <div class="post__foot">
            <div class="likes-container color--primary">
              <button class="btn-upvote btn--round color--primary check-login" onclick="vote(${data.postId}, 'up')">
                <svg class="icon thumb-up" viewBox="0 0 1792 1792" xmlns="http://www.w3.org/2000/svg">
                  <path d="M320 1344q0-26-19-45t-45-19q-27 0-45.5 19t-18.5 45q0 27 18.5 45.5T256 1408q26 0 45-18.5t19-45.5zm160-512v640q0 26-19 45t-45 19H128q-26 0-45-19t-19-45V832q0-26 19-45t45-19h288q26 0 45 19t19 45zm1184 0q0 86-55 149 15 44 15 76 3 76-43 137 17 56 0 117-15 57-54 94 9 112-49 181-64 76-197 78h-129q-66 0-144-15.5t-121.5-29T766 1580q-123-43-158-44-26-1-45-19.5t-19-44.5V831q0-25 18-43.5t43-20.5q24-2 76-59t101-121q68-87 101-120 18-18 31-48t17.5-48.5T945 310q7-39 12.5-61t19.5-52 34-50q19-19 45-19 46 0 82.5 10.5t60 26 40 40.5 24 45 12 50 5 45 .5 39q0 38-9.5 76t-19 60-27.5 56q-3 6-10 18t-11 22-8 24h277q78 0 135 57t57 135z"/>
                </svg>
              </button>
              <span class="likes-count" id="a${data.postId}">${data.likes}</span>
              <button class="btn-downvote btn--round color--primary check-login" onclick="vote(${data.postId}, 'down')">
                <svg class="icon thumb-down" viewBox="0 0 1792 1792" xmlns="http://www.w3.org/2000/svg">
                  <path d="M320 1344q0-26-19-45t-45-19q-27 0-45.5 19t-18.5 45q0 27 18.5 45.5T256 1408q26 0 45-18.5t19-45.5zm160-512v640q0 26-19 45t-45 19H128q-26 0-45-19t-19-45V832q0-26 19-45t45-19h288q26 0 45 19t19 45zm1184 0q0 86-55 149 15 44 15 76 3 76-43 137 17 56 0 117-15 57-54 94 9 112-49 181-64 76-197 78h-129q-66 0-144-15.5t-121.5-29T766 1580q-123-43-158-44-26-1-45-19.5t-19-44.5V831q0-25 18-43.5t43-20.5q24-2 76-59t101-121q68-87 101-120 18-18 31-48t17.5-48.5T945 310q7-39 12.5-61t19.5-52 34-50q19-19 45-19 46 0 82.5 10.5t60 26 40 40.5 24 45 12 50 5 45 .5 39q0 38-9.5 76t-19 60-27.5 56q-3 6-10 18t-11 22-8 24h277q78 0 135 57t57 135z"/>
                </svg>
              </button>
            </div>
            <button class="btn--primary btn-comments">
              <svg class="icon icon--secondary" viewBox="0 0 29.34 29.34" xml:space="preserve" xmlns="http://www.w3.org/2000/svg">
                    <path d="M27.18 1.6H2.16C.96 1.6 0 2.58 0 3.77v17.57c0 1.19.97 2.16 2.16 2.16H15.7l5.06 3.77c.41.31.84.47 1.26.47.8 0 1.64-.6 1.64-1.92v-2.32h3.52c1.2 0 2.16-.97 2.16-2.16V3.76c0-1.19-.97-2.15-2.16-2.15zm.16 19.73c0 .09-.07.16-.15.16h-5.53v3.95l-5.3-3.95H2.17a.15.15 0 0 1-.16-.16V3.76c0-.09.07-.16.16-.16h25.02c.1 0 .16.08.16.16v17.57zM5.5 10.8h4.34v4.34H5.5v-4.34zm7 0h4.34v4.34H12.5v-4.34zm7 0h4.34v4.34H19.5v-4.34z"/>
              </svg>
              <span class="number margin-left5">${data.comments}</span>
            </button>
          </div>`;
    const parent = document.querySelector('main');
    const placeToPost = parent.children[1];
    document.querySelector('main').insertBefore(newPost, placeToPost);
}

async function deletePost(id) {
    if (await checkLogin()) {
        const response = await fetch(`http://localhost:8080/posts/${id}/delete`, {method: 'DELETE'});
        if (!response.ok) {
            const errMsg = await response.text();
            console.error(errMsg);
            inform(3, errMsg);
            return;
        }
        document.querySelector('main').removeChild(document.querySelector(`#post${id}`));
        console.log("Hopefully post deleted!"); // if id si wrong, nothing happens
        inform(4, "Deleted!");
    }
}

function closeNewPost() {
    document.getElementById('overlay__post').style.display = 'none';
}

async function openNewPost() {
    if (await checkLogin())
        document.querySelector('#overlay__post').style.display = 'block';
}