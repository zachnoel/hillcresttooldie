'use strict';

angular.module('hillcresttooldieApp')
    .controller('PoDetailController', function ($scope, $stateParams, Po, Part, Customer) {
        $scope.po = {};
        $scope.load = function (id) {
            Po.get({id: id}, function(result) {
              $scope.po = result;
            });
        };
        $scope.load($stateParams.id);
    });
