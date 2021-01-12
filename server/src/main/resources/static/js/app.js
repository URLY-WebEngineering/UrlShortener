function successShortLink(msg) {
    $("#result").html(
        `<div class="alert alert-success lead">
            <a target="_blank" href="${msg.uri}"> ${msg.uri} </a>
        </div>`);

    if (msg.qr != null) {
        $("#QRresult").html(
            `<img style="width:400px;height:400px;" src="${msg.qr}" />`
        );
    }
}

function errorShortLink(msg) {
    $("#result").html("<div class='alert alert-danger lead'>SOMETHING WENT WRONG</div>");
}

function setupShortenerButton() {
    $("#shortener").submit(function (event) {
        event.preventDefault();

        // Prepare authorization headers if necessary
        var headers = {};
        if (keycloak.authenticated) {
            headers.Authorization = "Bearer " + keycloak.token;
        }

        $.ajax({
            type: "POST",
            url: "/link",
            data: $(this).serialize(),
            headers: headers,
            success: function (msg) {
                successShortLink(msg)
            },
            error: function (msg) {
                errorShortLink(msg)
            }
        });
    });
}

$(document).ready(
    function () {
        initializeKeycloak(setupShortenerButton);
    }
);