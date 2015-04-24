'use strict';

angular.module('hillcresttooldieApp')
    .factory('Part', function ($resource) {
        return $resource('api/parts/:id', {}, {
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
