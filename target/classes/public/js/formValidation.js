(function () {
    "use strict";

    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    let forms = $(".needs-validation");

    // Loop over them and prevent submission
    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener(
            "submit",
            function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();

                } else {

                    if (event.submitter.innerHTML === 'Login') {
                        const loginValues = {
                            userName: event.target[0].value,
                            password: event.target[1].value
                        }

                        $.post("/login", loginValues, function (data) {

                            if (data.isLogin) {
                                console.log(data)

                                window.localStorage.setItem('userId', data.userId);

                                window.localStorage.setItem('username', data.userName);
                                window.localStorage.setItem('birthDate', data.birthDate);
                                window.localStorage.setItem('school', data.school);
                                window.localStorage.setItem('interest', data.interest);
                                window.localStorage.setItem('isLogin', 'true');
                                window.location.href = './chatroom.html';

                            } else {
                                $("#login-error").css("display", "block");
                            }
                        }, "json");
                    } else if(event.submitter.innerHTML === 'Sign up'){
                        const registerValues = {
                            userName: event.target[0].value,
                            birthDate: event.target[1].value,
                            school: event.target[2].value,
                            interest: event.target[3].value,
                            password: event.target[4].value
                        }

                        window.localStorage.setItem('username', event.target[0].value);
                        window.localStorage.setItem('birthDate', event.target[1].value);
                        window.localStorage.setItem('school', event.target[2].value);
                        window.localStorage.setItem('interest', event.target[3].value);
                        window.localStorage.setItem('isLogin', 'false');

                        $.post("/signUp", registerValues, function (data) {
                            if (data) {
                                // const loginValues = {
                                //     userName: event.target[0].value,
                                //     password: event.target[4].value
                                // }
                                // $.post("/login", loginValues, function (data) {
                                //
                                //     if (data.isLogin) {
                                //         window.location.href = './chatroom.html'
                                //     }
                                // }, "json");
                                window.location.href = './index.html'
                            } else {
                                $("#signup-error").css("display", "block");
                            }
                        }, "json");
                    }

                    event.preventDefault();
                    event.stopPropagation();


                }
                form.classList.add("was-validated");
            },
            false
        );

        return true
    });

    $("#registerForm").click((e)=>{
        $("#signup-error").css("display", "none");
    })
    $("#loginForm").click((e)=>{
        $("#login-error").css("display", "none");
    })
})();
