/*
*
*
* 'use strict';

angular.module('app.privateAccount', ['ngResource'])

	.factory('PrivateAccountFactory', function ($resource) {
		return $resource('', {}, {
			findByProfileEmail: {method: 'GET', url: 'api/flat/search/findByProfileEmailContains'}
		})
	});
	*/

// angular.module("Home",[])
    app.controller("HomeController", function($scope, $http) {
        getCurrentOrder();

        function getCurrentOrder () {
            $http({
                method: 'GET',
                url: '/getCurrentOrder'
            }).then(
                function(res) { // success
                    $scope.medicines = res.data;
                },
                function(res) { // error
                    console.log("Error: " + res.status + " : " + res.data);
                }
            );
        }
    });