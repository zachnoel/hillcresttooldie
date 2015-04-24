'use strict';

angular.module('hillcresttooldieApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


