var app = angular.module('bookings', []);

app.controller('HomeController', function($scope, $http) {

	$('#booking-daterange').datepicker({
	    autoclose: true,
 		calendarWeeks: true,
	    todayHighlight: true,
	    todayBtn: true,
	    startDate: new Date()
	});

 	$('#datepicker-from').datepicker().on('changeDate', function(e) {
 		updateIsSearchEnabled();
 		$('#datepicker-to').datepicker('show');
 	});

 	$('#datepicker-to').datepicker().on('changeDate', function(e) {
 		updateIsSearchEnabled();
 	});

	$scope.isSearchEnabled = null;

	$scope.searchRooms = function() {
		sessionStorage.setItem('startDate', getStartDate());
		sessionStorage.setItem('endDate', getEndDate());
		window.location.href = "rooms";
	};

	function updateIsSearchEnabled() {
		var startDate = getStartDate();
		var endDate = getEndDate();
		$scope.isSearchEnabled = startDate != null && !Array.isArray(startDate) && endDate != null && !Array.isArray(endDate);
	};

	function getStartDate() {
		return $('#datepicker-from').datepicker('getDate');
	}

	function getEndDate() {
		return $('#datepicker-to').datepicker('getDate');
	}
});