'use strict';

angular.module('hillcresttooldieApp')
    .controller('Po_partDetailController', function ($scope, $stateParams, Po_part, Part, Po) {
        $scope.po_part = {};
        $scope.load = function (id) {
            Po_part.get({id: id}, function(result) {
              $scope.po_part = result;
            });
        };
        $scope.load($stateParams.id);
    });
