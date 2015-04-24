'use strict';

angular.module('hillcresttooldieApp')
    .factory('Material', function ($resource) {
        return $resource('api/materials/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
