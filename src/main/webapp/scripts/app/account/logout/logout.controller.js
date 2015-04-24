'use strict';

angular.module('hillcresttooldieApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
