'use strict';

angular.module('hillcresttooldieApp')
    .controller('Po_partController', function ($scope, Po_part, Part, Po) {
        $scope.po_parts = [];
        $scope.parts = Part.query();
        $scope.pos = Po.query();
        $scope.loadAll = function() {
            Po_part.query(function(result) {
               $scope.po_parts = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Po_part.update($scope.po_part,
                function () {
            		console.log($scope.po_part);
                    $scope.loadAll();
                    $('#savePo_partModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Po_part.get({id: id}, function(result) {
                $scope.po_part = result;
                $('#savePo_partModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Po_part.get({id: id}, function(result) {
                $scope.po_part = result;
                $('#deletePo_partConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Po_part.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePo_partConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.po_part = {part_quantity: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
           
        };
    });
