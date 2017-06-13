/**
 * Created by stephan on 20.03.16.
 */

$(function () {
    // VARIABLES =============================================================
    var TOKEN_KEY = "JWT"
    var $loggedIn = $("#loggedIn").hide();
    var $response = $("#response");
    var $login = $("#login");
    var $registration = $("#registration");
    var $userInfo = $("#userInfo").hide();

    // FUNCTIONS =============================================================
    function getJwtToken(token) {
        return localStorage.getItem(TOKEN_KEY);
    }

    function setJwtToken(token) {
        localStorage.setItem(TOKEN_KEY, token);
    }

    function removeJwtToken() {
        localStorage.removeItem(TOKEN_KEY);
    }

    function doRegistration(userData){
            $.ajax({
                    url: "/registration",
                    type: "POST",
                    data: JSON.stringify(userData),
                    contentType: "application/json; charset=utf-8",
                    dataType: "text",
                    success: function (data, textStatus, jqXHR) {
                    $('#registrationSuccessModal')
                                            .modal("show")
                                            .find(".modal-body")
                                            .empty();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                            $('#registrationErrorModal')
                                .modal("show")
                                .find(".modal-body")
                                .empty()
                                .html("<p>Spring exception:<br>" + jqXHR.responseText + "</p>");
                    }
                });
        }

    function doLogin(loginData) {
        $.ajax({
            url: "/auth",
            type: "POST",
            data: JSON.stringify(loginData),
            contentType: "application/json; charset=utf-8",
            dataType: "text",
            success: function (data, textStatus, jqXHR) {
                setJwtToken(data);
                $login.hide();
                $registration.hide();
                showTokenInformation()
                showUserInformation();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                    $('#loginErrorModal')
                        .modal("show")
                        .find(".modal-body")
                        .empty()
                        .html("<p>Spring exception:<br>" + jqXHR.responseText + "</p>");
            }
        });
    }

    function doLogout() {
        removeJwtToken();
        $login.show();
        $registration.show();
        $userInfo
            .hide()
            .find("#userInfoBody").empty();
        $loggedIn
            .hide()
            .attr("title", "")
            .empty();
    }

    function createAuthorizationTokenHeader() {
        var token = getJwtToken();
        if (token) {
            return {"Authorization": token};
        } else {
            return {};
        }
    }

    function showUserInformation() {
        $.ajax({
            url: "/me",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "text",
            headers: createAuthorizationTokenHeader(),
            success: function (data, textStatus, jqXHR) {

                var $userInfoBody = $userInfo.find("#userInfoBody");
                $userInfoBody.append($("<div>").text("Username: " + data));
                $userInfo.show();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                   $('#loginErrorModal')
                                   .modal("show")
                                    .find(".modal-body")
                                    .empty()
                                    .html("<p>Spring exception:<br>" + jqXHR.responseJSON.exception + "</p>");
                 }
        });
    }

    function showTokenInformation() {
        $loggedIn
            .text("Token: " + getJwtToken())
            .attr("title", "Token: " + getJwtToken())
            .show();
    }

    function showResponse(statusCode, message) {
        $response
            .empty()
            .text("status code: " + statusCode + "\n-------------------------\n" + message);
    }


    $("#loginForm").submit(function () {
        event.preventDefault();
        var $form = $(this);
        var formData = {
            username: $form.find('input[name="username"]').val(),
            password: $form.find('input[name="password"]').val()
        };
        doLogin(formData);
    });

    $("#registrationForm").submit(function () {
               event.preventDefault();
               var $form = $(this);
               var pass = $form.find('input[name="password"]').val();
               var repass = $form.find('input[name="repassword"]').val();
               if (pass !=repass) alert('Passwords are different');
               var userData = {
                   nickname: $form.find('input[name="username"]').val(),
                   name: $form.find('input[name="name"]').val(),
                   surname: $form.find('input[name="surname"]').val(),
                   password: $form.find('input[name="password"]').val()
               };
               doRegistration(userData);
           });

    $("#logoutButton").click(doLogout);

    $loggedIn.click(function () {
        $loggedIn
                .toggleClass("text-hidden")
                .toggleClass("text-shown");
    });


    if (getJwtToken()) {
        $login.hide();
        $registration.hide();
        showTokenInformation();
        showUserInformation();
    }
});