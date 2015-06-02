'use strict';

angular.module('hillcresttooldieApp')
    .factory('Po', function ($resource) {
        return $resource('api/pos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    var due_dateFrom = data.due_date.split("-");
                    data.due_date = new Date(new Date(due_dateFrom[0], due_dateFrom[1] - 1, due_dateFrom[2]));
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
    .factory('PoFilterByDate', function ($resource) {
	    return $resource('api/filteredPos/:startDate/:endDate', {}, {
	        'query': { method: 'GET', isArray: true}
	    });
	});
