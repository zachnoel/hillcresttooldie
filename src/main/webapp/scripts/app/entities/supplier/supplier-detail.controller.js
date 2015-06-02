'use strict';

angular.module('hillcresttooldieApp')
    .controller('SupplierDetailController', function ($scope, $stateParams, Supplier, Material) {
        $scope.supplier = {};
        $scope.load = function (id) {
            Supplier.get({id: id}, function(result) {
              $scope.supplier = result;
            });
        };
        $scope.load($stateParams.id);
    });
