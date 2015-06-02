'use strict';

angular.module('hillcresttooldieApp')
    .controller('PartController', function ($scope, Part, Material, ParseLinks) {
        $scope.parts = [];
        $scope.materials = Material.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Part.query({page: $scope.page, per_page: 10000}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.parts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Part.update($scope.part,
                function () {
                    $scope.loadAll();
                    $('#savePartModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Part.get({id: id}, function(result) {
                $scope.part = result;
                $('#savePartModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Part.get({id: id}, function(result) {
                $scope.part = result;
                $('#deletePartConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Part.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePartConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.part = {part_number: null, plasma_hrs_per_part: null, grind_hrs_per_part: null, mill_hrs_per_part: null, brakepress_hrs_per_part: null, lb_per_part: null, inventory_count: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
