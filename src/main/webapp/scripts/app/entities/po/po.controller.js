'use strict';

angular.module('hillcresttooldieApp')
    .controller('PoController', function ($scope, $http, $q, Po, Part, Po_part, PoParts, PoFilterByDate, Customer, ParseLinks, $filter) {
        $scope.pos = [];
        $scope.pagerNavShow = true;
        $scope.po_part = [];
        $scope.po_part = {id: null, part_quantity: null};
        $scope.parts = Part.query();
        $scope.customers = Customer.query();
        $scope.po_part_list = [];
        $scope.page = 1;
        
      
        
        $scope.loadAll = function() {
            Po.query({page: $scope.page, per_page: 100000}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.pos = result;
                $scope.poLength = $scope.pos.length;
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
        
        $scope.update = function (id) {
            Po.get({id: id}, function(result) {
                $scope.po = result;
                $('#savePoModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Po.get({id: id}, function(result) {
                $scope.po = result;
                $('#deletePoPartsConfirmation').modal('show');
            });
        };
        
        

        $scope.confirmPartsDelete = function (id) {
        	angular.forEach($scope.pos, function(po){
        		po.expanded = false;
        	});
        	PoParts.query({poId: id}, function(result) {
        		
                $scope.po_part_list = result;
                
                	angular.forEach($scope.po_part_list, function(po_part_list){
	            		Po_part.delete({id: po_part_list.id});
	            	});
                	$('#deletePoPartsConfirmation').modal('hide');
                	$('#deletePoConfirmation').modal('show');
	            });
        };
        
        $scope.confirmPoDelete = function (id) {
        	Po.delete({id: id},
    	              function () {
    	                  $scope.loadAll();
    	                  $('#deletePoConfirmation').modal('hide');
    	                 $scope.clear();
    	     });
        };
        
        function deletePo(id){
        	alert('deleting now' + id);
        	
        }
        
        $scope.deletePoPart = function(poPartId, poId){
        	Po_part.delete({id: poPartId}, function(){
        		PoParts.query({poId: poId}, function(result) {
                    $scope.po_part_list = result;
                    $scope.po_part = {po: {id: poId}};
                });
        	});
        };

        $scope.clear = function () {
            $scope.po = {po_number: null, sales_order_number: null, due_date: null, status: null, total_sale: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
            
        };
        
        //Adds a PO Part 
        $scope.createPoPart	= function (){
    		var poId = $scope.po_part.po.id;
    		var index = $scope.$index;
    		Po_part.update($scope.po_part, function () {
    				 $scope.po_part = {id: null, part_quantity: null, po: {id: poId}};
    				 PoParts.query({poId: poId}, function(result) {
    		                $scope.po_part_list = result;
    		         });
	        });
    		
    	
        };
        
        //on each row expand click 
        $scope.expand = function (po, poId) {
        	angular.forEach($scope.pos, function(po){
        		po.expanded = false;
        	});
        	po.expanded = true;
        	PoParts.query({poId: poId}, function(result) {
                $scope.po_part_list = result;
                $scope.po_part = {po: {id: poId}};
            });
        };
        
      //Date Filter for Purchase Orders
        $scope.filterPoByDate = function(){
        	//format the date from form to string format for the repository to understand
        	var startDate = $filter('date')($scope.dateFilter.startDate, "yyyy-MM-dd");
        	var endDate = $filter('date')($scope.dateFilter.endDate, "yyyy-MM-dd");
        	
        	//submit the date filters to javascript service
        	PoFilterByDate.query({startDate:startDate, endDate:endDate}, function(result){
        		$scope.pagerNavShow = false;
        		$scope.pos = result;
        		$scope.poLength = $scope.pos.length;
        	})
        };
        //This clears the date filter applied on Purchase orders and unhides the pager
        $scope.clearFilter = function(){
        	$scope.dateFilter = null;
        	$scope.pagerNavShow = true;
        	$scope.page = '1';
        	$scope.loadAll();
        };
        
        //Generates shop orders for all PO's currently in the model
        $scope.generateShopOrders = function(){
        	
        	angular.forEach($scope.pos, function(po){
        		$http.put('api/generateShopOrder/' + po.id,{});
        	});
        	
        	
        };        
});
