(function () {

    'use strict';

    angular.module('onms.ui', [
        'onms.ui.header',
        'onms.ui.admin',
        'ngRoute',
        'ui.bootstrap',
    ])
        .directive('iframeSetDimensionsOnload', [function() {
            return {
                restrict: 'A',
                link: function(scope, element, attrs) {
                    var setMaxHeight = function(document) {
                        var iFrameHeight = document.body.scrollHeight + 'px';
                        var iFrameWidth = '100%';
                        element.css('width', iFrameWidth);
                        element.css('height', iFrameHeight);
                    };

                    var recursivelySetMaxHeight = function(document) {
                        setTimeout(function() {
                            var moreElements = document.getElementsByTagName("iframe").length >= 1;
                            if (moreElements === true) {
                                recursivelySetMaxHeight(document.getElementsByTagName("iframe")[0]);
                            }
                            setMaxHeight(document);
                        }, 500)
                    };

                    element.on('load', function() {
                        var currentDocument = element[0].currentWindow.document;
                        recursivelySetMaxHeight(currentDocument);
                    });
                }
            }
        }])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider
                .when('/', {
                    redirectTo: '/legacy/home'
                })
                .when('/home', {
                    templateUrl: 'app/components/home/home.html',
                })
                .when('/legacy/:type', {
                    templateUrl: 'app/components/legacy/legacy.html',
                    controller: 'LegacyController'
                });
                // .otherwise({
                //     redirectTo: '/legacy/home'
                // });
        }])
        .controller("MainController", ['$scope', function ($scope) {


        }])
        .controller('LegacyController', ['$scope', '$routeParams', '$log', 'LegacyNavigationMappingService', function ($scope, $routeParams, $log, LegacyNavigationMappingService) {
            var type = LegacyNavigationMappingService.getMappingByRoute($routeParams.type);
            if (type !== $routeParams.type) {
                $scope.legacyUrl = type;
            }
            if (!$scope.legacyUrl) { // if not defined, simply use the parameter itself
                $scope.legacyUrl = $routeParams.type;
            }
        }])
        .run(['$rootScope', function($rootScope) {
            $rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
                if (current.$$route.title) {
                    $rootScope.title = current.$$route.title + " | OpenNMS Web Console";
                } else {
                    $rootScope.title = "OpenNMS Web Console";
                }
            })
        }]);

}());
