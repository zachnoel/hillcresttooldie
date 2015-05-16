'use strict';

angular.module('hillcresttooldieApp')
    .factory('Po_part', function ($resource) {
        return $resource('api/po_parts/:id', {}, {
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
