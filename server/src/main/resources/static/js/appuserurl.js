function successShowUserLinks(msg) {
    $("#result").html(msg);
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
