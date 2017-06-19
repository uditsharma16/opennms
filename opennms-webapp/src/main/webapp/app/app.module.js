(function () {

    'use strict';

    angular.module('onms.ui', [
        'onms.ui.header',
        'ngRoute',
        'ui.bootstrap',
    ])
        .directive('iframeSetDimensionsOnload', [function() {
            return {
                restrict: 'A',
                link: function(scope, element, attrs){
                    element.on('load', function(){
                        /* Set the dimensions here,
                         I think that you were trying to do something like this: */
                        var iFrameHeight = element[0].contentWindow.document.body.scrollHeight + 'px';
                        var iFrameWidth = '100%';
                        element.css('width', iFrameWidth);
                        element.css('height', iFrameHeight);
                    })
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
        .controller('LegacyController', ['$scope', '$routeParams', '$log', 'NavigationMappingService', function ($scope, $routeParams, $log, NavigationMappingService) {
            var type = NavigationMappingService.getMappingByRoute($routeParams.type);
            if (type !== $routeParams.type) {
                $scope.legacyUrl = type;
            }
            if (!$scope.legacyUrl) { // if not defined, simply use the parameter itself
                $scope.legacyUrl = $routeParams.type;
            }
        }]);

}());
