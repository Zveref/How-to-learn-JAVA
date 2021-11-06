(function () {
    "use strict";

    if (window.localStorage.getItem('isLogin') === 'true') {
        window.location.href = './chatroom.html'
    }

})();