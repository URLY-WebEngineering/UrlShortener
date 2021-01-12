function successShowUserLinks(msg) {
    var htmlForDisplayingUrls = msg.urls.map(function(url) {
        return `<li class="list-group-item">
                    <a target="_blank" href="${url.uri}">${url.uri}</a>
                    <input type="checkbox" name="${url.uri}" checked="">
                </li>`;
    }).join("");

    var htmlListUrls = '<ul class="list-group col-8">' + htmlForDisplayingUrls + "</ul>";

    $("#result").html(htmlListUrls);
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

$(document).ready(function () {
    initializeKeycloak(getUserLinks);
});
