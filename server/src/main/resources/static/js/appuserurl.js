function successShowUserLinks(msg) {
    var htmlForDisplayingUrls = msg.urls.map(function(url) {
        console.log(url);
        return `<li class="list-group-item" class="shortenedurl" id="${url.hash}">
                    <a target="_blank" href="${url.uri}">${url.uri}</a>
                    <input type="checkbox" name="${url.hash}" checked="">
                </li>`;
    }).join("");

    $("#listofhashes").html(htmlForDisplayingUrls);
}

function errorShowUserLinks(msg) {
    $("#result").html("<div class='alert alert-danger lead'>SOMETHING WENT WRONG</div>");
}

function getUserLinks() {
    // Prepare authorization headers if necessary
    var headers = {};
    if (keycloak.authenticated) {
        headers.Authorization = "Bearer " + keycloak.token;
    }

    // Make request to get user urls
    $.ajax({
        type: "GET",
        url: "/user/links",
        headers: headers,
        success: function (msg) {
            successShowUserLinks(msg);
        },
        error: function (msg) {
            errorShowUserLinks(msg);
        }
    });
}

function deleteSelectedHashes() {
    // Get all checked urls
    $("#listofhashes li input:checked").each(function(index, li) {
        // For each checked url, get the name (this is the shortened url)
        sendDelete(li.name);
    });
}

function initialFunction() {
   getUserLinks();
   connect();
   $("#delete").submit(function (event) {
       event.preventDefault();
       deleteSelectedHashes();
   })
}

$(document).ready(function () {
    initializeKeycloak(initialFunction);
});
