function formatDate(date) {
	var month = '' + (date.getMonth() + 1);
	var day = '' + date.getDate();
	var year = date.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [day, month, year].join('-');
}

var app = angular.module('bookings', ['ngRoute']);

app.config(function($routeProvider){
	$routeProvider.when('/', {
		controller: 'BookingsController',
		templateUrl: 'templates/rooms.html'
     }).when('/my-bookings', {
		controller: 'MyBookingsController',
		templateUrl: '../../protected/user/templates/bookings.html'
     }).when('/profile', {
		controller: 'ProfileController',
		templateUrl: '../../protected/user/templates/profile.html'
     });
}).service('userService', function () {
    var user = null;

    return {
        getUser: function () {
            return user;
        },
        setUser: function(value) {
            user = value;
        }
    };
}).controller('LoginController', function($scope, $http, userService) {
	getUser();

	function getUser() {
		$http.get('../../../api/v1/public/user/profile').success(function(data) {
			userService.setUser(data.userId);
			$scope.user = userService.getUser();
		}).error(function(data, status) {
			if (status == 404) {
				$http.post('../../../api/v1/protected/user/register').success(function(data) {
					getUser();
				}).error(function(data, status) {
					alert('Unable to register user');
				});
			}
		});
	}
}).controller('BookingsController', function($scope, $http, userService) {

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

 	$("#review-rating").on("rating.change", function(event, value) {
 		$scope.review.rating = value;
 	});

	$scope.rooms = [];
	$scope.selectedRoomTotalPrice = null;
	$scope.selectedRoomRating = null;
	$scope.selectedRoomReviews = [];
	$scope.isSearchEnabled = false;
	$scope.review = {
			'comment': null,
			'rating': 1
	};

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

		var message = "Do you want to book the selected room, " +
				"for the period from [" + formatDate(data.startDate) + "] to [" + formatDate(data.endDate) + "], " +
				"for a total price of $" + $scope.selectedRoomTotalPrice + "?";
		if(confirm(message)) {
			$http.post('../../../api/v1/protected/user/book', data).success(function(data) {
				alert("Booking successfull!");
			});
		}
	}

	$scope.submitReview = function() {
		$scope.review.roomId = $scope.selectedRoom.roomId;
		if(confirm("Do you really want to submit a review?")) {
			$http.post('../../../api/v1/protected/user/reviews', $scope.review).success(function(data) {
				alert("The review was successfully added!");
				loadRoomSpecificProperties($scope.selectedRoom.roomId);
				$scope.review.rating = 1;
				$scope.review.comment = null;
			});
		}
	}

	$scope.getAvailableRooms();

	function loadRooms() {
		$http.get('../../../api/v1/public/rooms').success(function(data) {
			$scope.rooms = data;
			updatePageProperties(0);
		});
	}

	function updatePageProperties(index) {
		$scope.selectedRoom = $scope.rooms[index];
		$scope.user = userService.getUser();
		loadRoomSpecificProperties($scope.selectedRoom.roomId);
	}

	function loadRoomSpecificProperties(roomId) {
		loadSelectedRoomTotalPrice(roomId);
		loadSelectedRoomReviews(roomId);
		loadCanWriteReview(roomId);
	}

	function loadSelectedRoomTotalPrice(roomId) {
		$http.get('../../../api/v1/public/payment/rooms/' + roomId + "/price").success(function(data) {
			$scope.selectedRoomTotalPrice = data * getDaysBetween(getStartDate(), getEndDate());
		});
	}

	function getDaysBetween(firstDate, secondDate) {
	    return Math.round((secondDate - firstDate) / (1000 * 60 * 60 * 24));
	}

	function loadSelectedRoomReviews(roomId) {
		$http.get('../../../api/v1/public/reviews/room/' + roomId).success(function(data) {
			$scope.selectedRoomReviews = data;
			var ratingSum = 0;
			$scope.selectedRoomReviews.forEach(function(next) {
				ratingSum += next.rating;
				next.rating = createRatingArray(next.rating);
			});
			$scope.selectedRoomRating = createRatingArray(Math.round((ratingSum/ $scope.selectedRoomReviews.length)));		
		});
	}

	function createRatingArray(rating) {
		rating = (rating < 0) ? 0 : (rating > 5) ? 5 : rating;
		var ratingArray = [];
		for(var i = 1; i <= 5; i ++) {
			ratingArray.push(i <= rating);
		}
		return ratingArray;
	}

	function loadCanWriteReview(roomId) {
		if ($scope.user) {
			$http.get('../../../api/v1/protected/user/reviews/room/' + roomId).success(function(data) {
				$scope.canWriteReview = !data;
			});
		}
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
}).controller('MyBookingsController', function($scope, $http) {

	$http.get('../../protected/user/resources/model-booking.json').success(function(data) {
		$scope.model = data;
	});

	$http.get('../../../api/v1/protected/user/bookings').success(function(data) {
		$scope.data = data;
	});
	
}).controller('ProfileController', function($scope, $http) {

	const API_PROFILE_MODEL = '../../protected/user/resources/model-profile.json';
	const API_PROFILE = '../../../api/v1/protected/user/profile';

	$scope.readonly = true;

	loadUserProfileModel();
	loadUserProfileData();

	$scope.setReadonly = function(value) {
		$scope.readonly = value;
	}

	$scope.saveChanges = function() {
		$http.put(API_PROFILE, $scope.data).success(function(data) {
			$scope.setReadonly(true);
		});
	}

	$scope.cancelChanges = function() {
		$scope.setReadonly(true);
		loadUserProfileData();
	}

	function loadUserProfileModel() {
		$http.get(API_PROFILE_MODEL).success(function(data) {
			$scope.model = data;
		});
	}

	function loadUserProfileData() {
		$http.get(API_PROFILE).success(function(data) {
			$scope.data = data;
		});
	}
}).directive('starRating', starRating);

function starRating() {
    return {
      restrict: 'EA',
      template:
    	  '<ul class="star-rating" ng-class="{readonly: readonly}">' +
    	  '  <li ng-repeat="star in stars" class="star" ng-class="{filled: star.filled}" ng-click="toggle($index)">' +
    	  '    <i class="fa fa-star"></i>' +
    	  '  </li>' +
    	  '</ul>',
      scope: {
    	  ratingValue: '=ngModel',
    	  max: '=?',
    	  onRatingSelect: '&?',
    	  readonly: '=?'
      },
      link: function(scope, element, attributes) {
    	  if (scope.max == undefined) {
    		  scope.max = 5;
    	  }

    	  function updateStars() {
    		  scope.stars = [];
    		  for (var i = 0; i < scope.max; i++) {
    			  scope.stars.push({
    				  filled: i < scope.ratingValue
    			  });
    		  }
    	  };

    	  scope.toggle = function(index) {
    		  if (scope.readonly == undefined || scope.readonly === false) {
    			  scope.ratingValue = index + 1;
    			  scope.onRatingSelect({
    				  rating: index + 1
    			  });
    		  }
    	  };

    	  scope.$watch('ratingValue', function(oldValue, newValue) {
    		  if (newValue) {
    			  updateStars();
    		  }
    	  });
      }
    };
}