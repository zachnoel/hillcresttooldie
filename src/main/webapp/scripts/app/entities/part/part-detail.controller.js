'use strict';

angular.module('hillcresttooldieApp')
    .controller('PartDetailController', function ($scope, $stateParams, Part, Material) {
        $scope.part = {};
        $scope.load = function (id) {
            Part.get({id: id}, function(result) {
              $scope.part = result;
            });
        };
        $scope.load($stateParams.id);
    });
