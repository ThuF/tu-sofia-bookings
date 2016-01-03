var app = angular.module('bookings', []);

app.controller('BookingsController', function($scope, $http) {

	$('#booking-daterange').datepicker({
	    autoclose: true,
 		calendarWeeks: true,
	    todayHighlight: true,
	    todayBtn: true,
	    startDate: new Date()
	});

 	$('#datepicker-from').datepicker().on('changeDate', function(e) {
 		$('#datepicker-to').datepicker('show');
 		updateSearchEnabled();
 	});

 	$('#datepicker-to').datepicker().on('changeDate', function(e) {
 		updateSearchEnabled();
 	});

	$scope.rooms = [];
	$scope.selectedRoomPrice = null;
	$scope.selectedRoomRating = null;
	$scope.selectedRoomReviews = [];
	$scope.isSearchEnabled = false;

	$scope.setSelectedRoom = function(index) {
		updatePageProperties(index);
	}

	$scope.getAvailableRooms = function() {
		if($scope.isSearchEnabled) {
			var startDate = getStartDate().getTime();
			var endDate = getEndDate().getTime();
			var url = '../../api/v1/public/rooms/available/startdate/' + startDate + '/enddate/' + endDate;
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

		$http.post('../../api/v1/protected/user/book', data).success(function(data) {
			alert("Booking successfull!");
		});
	}

	loadRooms();

	function loadRooms() {
		$http.get('../../api/v1/public/rooms').success(function(data){
			$scope.rooms = data;
			updatePageProperties(0);
		});
	}

	function updatePageProperties(index) {
		$scope.selectedRoom = $scope.rooms[index];
		loadSelectedRoomPrice($scope.selectedRoom.roomId);
		loadSelectedRoomReviews($scope.selectedRoom.roomId);
	}

	function loadSelectedRoomPrice(roomId) {
		$http.get('../../api/v1/public/payment/rooms/' + roomId + "/price").success(function(data){
			$scope.selectedRoomPrice = data;
		});
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