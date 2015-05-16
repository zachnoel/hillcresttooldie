'use strict';

angular.module('hillcresttooldieApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('po_part', {
                parent: 'entity',
                url: '/po_part',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'hillcresttooldieApp.po_part.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/po_part/po_parts.html',
                        controller: 'Po_partController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('po_part');
                        return $translate.refresh();
                    }]
                }
            })
            .state('po_partDetail', {
                parent: 'entity',
                url: '/po_part/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'hillcresttooldieApp.po_part.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/po_part/po_part-detail.html',
                        controller: 'Po_partDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('po_part');
                        return $translate.refresh();
                    }]
                }
            });
    });
