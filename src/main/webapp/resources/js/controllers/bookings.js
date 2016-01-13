var app = angular.module('bookings', []);

app.controller('BookingsController', function($scope, $http) {

	initialSearchValidation();

	function initialSearchValidation() {
		if (sessionStorage.getItem('startDate') == null || sessionStorage.getItem('endDate') == null) {
			window.location.href = "../";
		}
	}
	$('#booking-daterange').datepicker({
	    autoclose: true,
 		calendarWeeks: true,
	    todayHighlight: true,
	    todayBtn: true,
	    startDate: new Date()
	});

	$('#datepicker-from').datepicker('setDate', new Date(sessionStorage.getItem('startDate')));
	$('#datepicker-to').datepicker('setDate', new Date(sessionStorage.getItem('endDate')));

 	$('#datepicker-from').datepicker().on('changeDate', function(e) {
 		$('#datepicker-to').datepicker('show');
 		sessionStorage.setItem('startDate', e.date);
 		updateSearchEnabled();
 	});

 	$('#datepicker-to').datepicker().on('changeDate', function(e) {
 		sessionStorage.setItem('endDate', e.date);
 		updateSearchEnabled();
 	});

	$scope.rooms = [];
	$scope.selectedRoomTotalPrice = null;
	$scope.selectedRoomRating = null;
	$scope.selectedRoomReviews = [];
	$scope.isSearchEnabled = false;

	$scope.setSelectedRoom = function(index) {
		updatePageProperties(index);
	}

	$scope.getAvailableRooms = function() {
		updateSearchEnabled();
		if($scope.isSearchEnabled) {
			var startDate = getStartDate().getTime();
			var endDate = getEndDate().getTime();
			var url = '../../../api/v1/public/rooms/available/startdate/' + startDate + '/enddate/' + endDate;
			$http.get(url).success(function(data){
				$scope.rooms = data;
				updatePageProperties(0);
			});
		}
	}

	$scope.bookSelectedRoom = function() {
		var data = {
				'roomId': $scope.selectedRoom.roomId,
				'startDate': getStartDate(),
				'endDate': getEndDate()
		};

		$http.post('../../../api/v1/protected/user/book', data).success(function(data) {
			alert("Booking successfull!");
		});
	}

	$scope.getAvailableRooms();
	getUser();

	function loadRooms() {
		$http.get('../../../api/v1/public/rooms').success(function(data){
			$scope.rooms = data;
			updatePageProperties(0);
		});
	}

	function getUser() {
		$http.get('../../../api/v1/public/user/profile').success(function(data){
			$scope.user = data;
		}).error(function(data, status) {
			if (status == 404) {
				$http.post('../../../api/v1/protected/user/register').success(function(data){
					getUser();
				}).error(function(data, status) {
					alert('Unable to register user');
				});
			}
		});
	}

	function updatePageProperties(index) {
		$scope.selectedRoom = $scope.rooms[index];
		loadSelectedRoomTotalPrice($scope.selectedRoom.roomId);
		loadSelectedRoomReviews($scope.selectedRoom.roomId);
	}

	function loadSelectedRoomTotalPrice(roomId) {
		$http.get('../../../api/v1/public/payment/rooms/' + roomId + "/price").success(function(data){
			$scope.selectedRoomTotalPrice = data * getDaysBetween(getStartDate(), getEndDate());
		});
	}

	function getDaysBetween(firstDate, secondDate) {
	    return Math.round((secondDate - firstDate) / (1000 * 60 * 60 * 24));
	}

	function loadSelectedRoomReviews(roomId) {
		$scope.selectedRoomReviews.push({
			'rating': 3,
			'username': 'Yordan Pavlov',
			'comment': 'This product was great in terms of quality. I would definitely buy another!',
			'daysBefore': '1'
		});
		$scope.selectedRoomReviews.push({
			'rating': 4,
			'username': 'Desislava Rasheva',
			'comment': 'This product was great in terms of quality. I would definitely buy another!',
			'daysBefore': '2'
		});
		$scope.selectedRoomReviews.forEach(function(next){
			next.rating = createRatingArray(next.rating);
		});
		$scope.selectedRoomRating = createRatingArray(2);		
	}

	function createRatingArray(rating) {
		rating = (rating < 0) ? 0 : (rating > 5) ? 5 : rating;
		var ratingArray = [];
		for(var i = 1; i <= 5; i ++) {
			ratingArray.push(i <= rating);
		}
		return ratingArray;
	}

	function getStartDate() {
		return $('#datepicker-from').datepicker('getDate');
	}

	function getEndDate() {
		return $('#datepicker-to').datepicker('getDate');
	}

	function updateSearchEnabled() {
		$scope.isSearchEnabled = getStartDate() != null && getEndDate() != null;
	}
});