'use strict';

angular.module('hillcresttooldieApp')
    .factory('Supplier', function ($resource) {
        return $resource('api/suppliers/:id', {}, {
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
