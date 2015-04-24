'use strict';

angular.module('hillcresttooldieApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('customer', {
                parent: 'entity',
                url: '/customer',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'hillcresttooldieApp.customer.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customer/customers.html',
                        controller: 'CustomerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customer');
                        return $translate.refresh();
                    }]
                }
            })
            .state('customerDetail', {
                parent: 'entity',
                url: '/customer/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'hillcresttooldieApp.customer.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customer/customer-detail.html',
                        controller: 'CustomerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customer');
                        return $translate.refresh();
                    }]
                }
            });
    });
