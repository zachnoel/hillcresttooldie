'use strict';

angular.module('hillcresttooldieApp')
    .controller('MaterialController', function ($scope, Material, ParseLinks) {
        $scope.materials = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Material.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.materials = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Material.update($scope.material,
                function () {
                    $scope.loadAll();
                    $('#saveMaterialModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Material.get({id: id}, function(result) {
                $scope.material = result;
                $('#saveMaterialModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Material.get({id: id}, function(result) {
                $scope.material = result;
                $('#deleteMaterialConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Material.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMaterialConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.material = {material_number: null, material_thickness: null, material_size: null, lb_per_sheet: null, dollar_per_lb: null, inventory_count: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
