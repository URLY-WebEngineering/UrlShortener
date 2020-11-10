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
                        $.ajax({
                            type: "POST",
                            url: "/qr",
                            data: { url: msg.uri},
                            success:function (data) {
                                $("#QRresult").html("<img alt='helmet' style='width:400px;height:400px;' src=" + data + " />");
                            },
                            error: function () {
                                $("#QRresult").html(
                                    "<div class='alert alert-danger lead'>SOMETHING WENT WRONG</div>");

                            }
                        });

                    },
                    error: function () {
                        $("#result").html(
                            "<div class='alert alert-danger lead'>ERROR</div>");
                    }
                });
            });
    });