'use strict';

angular.module('hillcresttooldieApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('material', {
                parent: 'entity',
                url: '/material',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'hillcresttooldieApp.material.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/material/materials.html',
                        controller: 'MaterialController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('material');
                        return $translate.refresh();
                    }]
                }
            })
            .state('materialDetail', {
                parent: 'entity',
                url: '/material/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'hillcresttooldieApp.material.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/material/material-detail.html',
                        controller: 'MaterialDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('material');
                        return $translate.refresh();
                    }]
                }
            });
    });
