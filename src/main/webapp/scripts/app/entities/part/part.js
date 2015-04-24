'use strict';

angular.module('hillcresttooldieApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('part', {
                parent: 'entity',
                url: '/part',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'hillcresttooldieApp.part.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/part/parts.html',
                        controller: 'PartController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('part');
                        return $translate.refresh();
                    }]
                }
            })
            .state('partDetail', {
                parent: 'entity',
                url: '/part/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'hillcresttooldieApp.part.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/part/part-detail.html',
                        controller: 'PartDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('part');
                        return $translate.refresh();
                    }]
                }
            });
    });
