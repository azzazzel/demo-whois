'use strict';


var whois = angular.module('demo.whois', ['ngResource']);

whois.controller('MainController', function MainController($scope, $http) {

	$scope.checkSite = function() {
		$scope.site = null;
		$scope.error = undefined
		$scope.loading = true
		$http.get('/rest/whois/' + $scope.domain).then(
		function(response) {
			$scope.loading = false
			if (response.data == "") {
				$scope.error = "We couldn't find whois information for " + $scope.domain
			} else {
				$scope.site = response.data;
			}
		}, 
		function(response) {
			$scope.loading = false
			$scope.error = response.statusText;
		});
	};
});

