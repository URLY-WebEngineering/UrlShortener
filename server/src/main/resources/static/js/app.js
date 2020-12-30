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
                        if (msg.qr != null){
                            $("#QRresult").html(
                                "<img  style='width:400px;height:400px;'"
                                + "src='"
                                + msg.qr
                                +"' />"
                            );
                        }
                        else{
                            $("#QRresult").html(
                                '<div id="QRresult" >' +   ' </div>'
                            );
                        }

                    },
                    error: function () {
                        // TODO: think about displaying the error from server
                        $("#result").html(
                            "<div class='alert alert-danger lead'>SOMETHING WENT WRONG</div>");
                    }
                });
            });
    });