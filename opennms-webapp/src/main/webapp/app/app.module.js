(function () {

    'use strict';

    angular.module('onms.ui', [
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
                })
                .otherwise({
                    redirectTo: '/'
                });
        }])
        .controller("MainController", ['$scope', function ($scope) {
            $scope.currentDate = new Date();
            $scope.remoteUser = 'admin';
            $scope.noticeStatus = 'Off';
            $scope.quiet = false;

        }])
        .controller('LegacyController', ['$scope', '$routeParams', '$log', function ($scope, $routeParams, $log) {
            var legacyMapping = {
                'home': 'home.jsp',
                'node' : '',
                'alarm' : '',
                'event' : ''
            };

            $scope.legacyUrl = legacyMapping[$routeParams.type];
            console.log($scope.legacyUrl);
        }]);

}());
