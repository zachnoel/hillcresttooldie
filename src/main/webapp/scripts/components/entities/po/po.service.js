'use strict';

angular.module('hillcresttooldieApp')
    .factory('Po', function ($resource) {
        return $resource('api/pos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET' },
            'update': { method:'PUT' }
        });
    })
    .factory('PoFilterByDate', function ($resource) {
	    return $resource('api/filteredPos/:startDate/:endDate', {}, {
	        'query': { method: 'GET', isArray: true}
	    });
	});
