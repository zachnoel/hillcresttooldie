'use strict';

angular.module('hillcresttooldieApp')
    .controller('PoController', function ($scope, Po, Part, Po_part, PoParts, Customer, ParseLinks) {
        $scope.pos = [];
        $scope.po_part = [];
        $scope.po_part = {id: null, part_quantity: null};
        $scope.parts = Part.query();
        $scope.po_part_list = [];
        $scope.page = 1;
        
        
        
        $scope.loadAll = function() {
            Po.query({page: $scope.page, per_page: 40}, function(result, headers) {
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
        		Po.update($scope.po, function () {
                    $scope.loadAll();
                    $('#savePoModal').modal('hide');
                    $scope.clear();
                });
        };
        
        $scope.createPoPart	= function (){
        		var poId = $scope.po_part.po.id;
        		var index = $scope.$index;
        		//alert(index);
        		Po_part.update($scope.po_part, function () {
        				 $scope.po_part = {id: null, part_quantity: null, po: {id: poId}};
        				 PoParts.query({poId: poId}, function(result) {
        		                $scope.po_part_list = result;
        		         });
		        });
        		$scope.loadAll();
        	
        };

        $scope.update = function (id) {
            Po.get({id: id}, function(result) {
                $scope.po = result;
                $('#savePoModal').modal('show');
            });
        };

        $scope.expand = function (poId) {
        	console.log(this.$index);
        	PoParts.query({poId: poId}, function(result) {
                $scope.po_part_list = result;
                $scope.po_part = {po: {id: poId}};
                
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
