'use strict';

angular.module('hillcresttooldieApp')
    .factory('Password', function ($resource) {
        return $resource('api/account/change_password', {}, {
        });
    });
