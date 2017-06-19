(function () {

    'use strict';

    angular.module('onms.ui.header', [])
        .controller("HeaderController", ['$scope', 'UserService', 'NoticeService', 'NavbarService', function($scope, UserService, NoticeService, NavbarService) {
            $scope.currentDate = new Date();
            $scope.quiet = false;

            UserService.whoami().then(function(result) {
                $scope.user = result;
                console.log($scope.user);
            });

            NoticeService.getNoticeStatus().then(function(result) {
                $scope.noticeStatus = result;
                console.log($scope.noticeStatus);
            });

            NavbarService.getNavigation().then(function(result) {
                $scope.menu = result.data;
                console.log($scope.menu);
            });
        }])
        .filter('legacyUrl', ['NavigationMappingService', function(NavigationMappingService) {
            return function(input) {
                var mapped = NavigationMappingService.getMappingByLegacyUrl(input);
                return "#legacy/" + mapped;
            };
        }])
        .factory('UserService', ['$q', function($q) {
            // TODO MVR retrieve data from opennms
            return {
                whoami: function() {
                    var deferred = $q.defer();
                    var user = {
                        name: 'admin',
                        roles: ["ROLE_ADMIN", "ROLE_PROVISION"],

                        isAdmin: function() {
                            return this.isRole("ROLE_ADMIN");
                        },
                        isProvision: function() {
                            return this.isRole("ROLE_PROVISION");
                        },
                        isRole: function(role) {
                            return this.roles.indexOf(role) >= 0;
                        }
                    };
                    deferred.resolve(user);
                    return deferred.promise;
                }
            }
        }])
        .factory("NavbarService", ['$http', function($http) {
            return {
                getNavigation: function() {
                    return $http({
                        method: 'GET',
                        url: 'navigation.htm'
                    });
                }
            };
        }])
        .factory("NavigationMappingService", [ function() {
            var mapping = {
                'navigation_search': "element/index.jsp",
                'navigation_report': "report/index.jsp",
                'navigation_dashboard': "dashboards.htm",
                'navigation_maps': "maps.htm",
                'home': 'home.jsp',
                'dashboard': 'dashboard.jsp',
                'account_selfservice': 'account/selfService/index.jsp',
                'support': 'support/index.htm',
            };

            return {
                getMappingByLegacyUrl: function(inputUrl) {
                    var key;
                    for (key in mapping) {
                        if (mapping[key] == inputUrl) {
                            return key;
                        }
                    }
                    return inputUrl;
                },
                getMappingByRoute: function(input) {
                    if (mapping[input]) {
                        return mapping[input];
                    }
                    return input;
                }
            }
        }])
        .factory("NoticeService", ['$q', function($q) {
            // TODO MVR retrieve data from opennms
            return {
                getNoticeStatus: function() {
                    var deferred = $q.defer();
                    deferred.resolve("Unknown")
                    return deferred.promise;
                }
            }
        }]);
}());
