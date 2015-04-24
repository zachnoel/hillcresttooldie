'use strict';

angular.module('hillcresttooldieApp')
    .controller('PoController', function ($scope, Po, Part, Customer, ParseLinks) {
        $scope.pos = [];
        $scope.parts = Part.query();
        $scope.customers = Customer.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Po.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.pos = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Po.update($scope.po,
                function () {
                    $scope.loadAll();
                    $('#savePoModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Po.get({id: id}, function(result) {
                $scope.po = result;
                $('#savePoModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Po.get({id: id}, function(result) {
                $scope.po = result;
                $('#deletePoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Po.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.po = {po_number: null, sales_order_number: null, due_date: null, status: null, total_sale: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
