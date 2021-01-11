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

$(document).ready(
    function () {
        $("#shortener").submit(function (event) {
            event.preventDefault();
            $.ajax({
                type: "POST",
                url: "/link",
                data: $(this).serialize(),
                success: function (msg) {
                    successShortLink(msg)
                },
                error: function (msg) {
                    errorShortLink(msg)
                }
            });
        });
    }
);