$(document).ready(
    function () {
        var enlace;
        $("#shortener").submit(
            function (event) {
                event.preventDefault();
                $.ajax({
                    type: "POST",
                    url: "/link",
                    data: $(this).serialize(),
                    success: function (msg) {
                        $("#result").html(
                            "<div class='alert alert-success lead'><a target='_blank' href='"
                            + msg.uri
                            + "'>"
                            + msg.uri
                            + "</a></div>"
                            );
                        enlace = msg.uri;
                        $("#botonQR").fadeIn();

                    },
                    error: function () {
                        $("#result").html(
                            "<div class='alert alert-danger lead'>ERROR</div>");
                    }
                });
            });
        $('#generateQR').click(function () {
            $.ajax({
                type: "POST",
                url: "/qr",
                data: { url: enlace},
                success:function (data) {
                   $("#QRresult").html("<img style='width:400px;height:400px;' src=" + data + " />");
                },
                error: function () {

                    $("#QRresult").html(
                        "<div class='alert alert-danger lead'>SOMETHING WENT WRONG</div>");

                }
            })

        })


    });