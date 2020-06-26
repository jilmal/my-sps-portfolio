// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

function getComments() {
  fetchBlobstoreUrl();
  fetch('/data').then(response => response.json()).then((comments) => {
    console.log(comments);

    const commentsListElement = document.getElementById('comments-section');
    commentsListElement.innerHTML = '';
    
    comments.forEach((com) => {
      commentsListElement.appendChild(createListElement(com));
    })
  });
}

function fetchBlobstoreUrl() {
  fetch('/blobstore-upload-url')
      .then((response) => {
        return response.text();
      })
      .then((imageUploadUrl) => {
        const messageForm = document.getElementById('my-form');
        messageForm.action = imageUploadUrl;
      });
}

/** Creates an <li> element containing text and image. */
function createListElement(comment) {
  const liElement = document.createElement('li');
  liElement.innerText = comment.input;
  const imgElement = document.createElement('img');
  imgElement.src = comment.imageUrl;
  liElement.appendChild(imgElement);
  return liElement;
}
