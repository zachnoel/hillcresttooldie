'use strict';

angular.module('hillcresttooldieApp')
    .controller('CustomerController', function ($scope, Customer, ParseLinks) {
        $scope.customers = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Customer.query({page: $scope.page, per_page: 10000}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.customers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Customer.update($scope.customer,
                function () {
                    $scope.loadAll();
                    $('#saveCustomerModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Customer.get({id: id}, function(result) {
                $scope.customer = result;
                $('#saveCustomerModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Customer.get({id: id}, function(result) {
                $scope.customer = result;
                $('#deleteCustomerConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Customer.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCustomerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.customer = {customer_name: null, customer_address: null, customer_phone: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
