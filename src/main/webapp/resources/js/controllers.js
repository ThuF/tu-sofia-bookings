var bookignsApp = angular.module('bookings', []);

bookignsApp.controller('BookingsController', function($scope, $http) {

	$scope.rooms = [];
	$scope.selectedRoomPrice = null;
	$scope.selectedRoomRating = null;
	$scope.selectedRoomReviews = [];

	$scope.setSelectedRoom = function(index) {
		updatePageProperties(index);
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
});
