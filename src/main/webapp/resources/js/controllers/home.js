var app = angular.module('bookings', []);

app.controller('HomeController', function($scope, $http) {

	$scope.searchRooms = function() {
		sessionStorage.setItem('startDate', getStartDate());
		sessionStorage.setItem('endDate', getEndDate());
		window.location.href = "rooms"
	}

	function getStartDate() {
		return $('#datepicker-from').datepicker('getDate');
	}

	function getEndDate() {
		return $('#datepicker-to').datepicker('getDate');
	}
});