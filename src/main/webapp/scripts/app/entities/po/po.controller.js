'use strict';

angular.module('hillcresttooldieApp')
    .controller('PoController', function ($scope, Po, Part, Customer, ParseLinks) {
        $scope.pos = [];
        $scope.parts = Part.query();
        $scope.customers = Customer.query();
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

        $scope.expand = function (id) {
            Po.get({id: id}, function(result) {
                $scope.po = result;
                
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

        
        
        $scope.setFiles = function(element) {
        $scope.$apply(function(scope) {
            console.log('files:', element.files);
            // Turn the FileList object into an Array
                scope.files = []
                for (var i = 0; i < element.files.length; i++) {
                    scope.files.push(element.files[i])
                }
            scope.progressVisible = false
            });
        };

        $scope.uploadFile = function() {
        var fd = new FormData()
        for (var i in $scope.files) {
            fd.append("uploadedFile", $scope.files[i])
        }
        var xhr = new XMLHttpRequest()
        xhr.upload.addEventListener("progress", uploadProgress, false)
        xhr.addEventListener("load", uploadComplete, false)
        xhr.addEventListener("error", uploadFailed, false)
        xhr.addEventListener("abort", uploadCanceled, false)
        xhr.open("POST", "/api/fileupload/po")
        $scope.progressVisible = true
        
        xhr.send(fd)
    }

    function uploadProgress(evt) {
        $scope.$apply(function(){
            if (evt.lengthComputable) {
                $scope.progress = Math.round(evt.loaded * 100 / evt.total)
            } else {
                $scope.progress = 'unable to compute'
            }
        })
    }

    function uploadComplete(evt) {
        /* This event is raised when the server send back a response */
        alert(evt.target.responseText)
    }

    function uploadFailed(evt) {
        alert("There was an error attempting to upload the file.")
    }

    function uploadCanceled(evt) {
        $scope.$apply(function(){
            $scope.progressVisible = false
        })
        alert("The upload has been canceled by the user or the browser dropped the connection.")
    }




});
