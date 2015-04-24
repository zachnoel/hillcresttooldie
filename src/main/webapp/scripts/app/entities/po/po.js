'use strict';

angular.module('hillcresttooldieApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('po', {
                parent: 'entity',
                url: '/po',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'hillcresttooldieApp.po.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/po/pos.html',
                        controller: 'PoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('po');
                        return $translate.refresh();
                    }]
                }
            })
            .state('poDetail', {
                parent: 'entity',
                url: '/po/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'hillcresttooldieApp.po.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/po/po-detail.html',
                        controller: 'PoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('po');
                        return $translate.refresh();
                    }]
                }
            });
    });
