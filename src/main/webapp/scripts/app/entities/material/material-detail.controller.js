'use strict';

angular.module('hillcresttooldieApp')
    .controller('MaterialDetailController', function ($scope, $stateParams, Material) {
        $scope.material = {};
        $scope.load = function (id) {
            Material.get({id: id}, function(result) {
              $scope.material = result;
            });
        };
        $scope.load($stateParams.id);
    });
