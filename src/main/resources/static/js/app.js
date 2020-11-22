$(document).ready(
    function () {
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
                        $("#QRresult").html(
                            "<img  style='width:400px;height:400px;'"
                            + "src='"
                            + msg.qr
                            +"' />"
                        );
                    },
                    error: function () {
                        $("#result").html(
                            "<div class='alert alert-danger lead'>SOMETHING WENT WRONG</div>");
                    }
                });
            });
    });