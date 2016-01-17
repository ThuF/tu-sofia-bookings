var bookignsApp = angular.module('bookings', ['ngRoute']);

bookignsApp.config(function($routeProvider){
	$routeProvider.when('/bookings', {
		controller: 'BookingsAdminController',
		templateUrl: 'templates/bookings.html'
	}).when('/rooms', {
		controller: 'RoomsAdminController',
		templateUrl: 'templates/rooms.html'
     }).when('/users', {
		controller: 'UsersAdminController',
		templateUrl: 'templates/users.html'
     }).otherwise({
         redirectTo: '/bookings'
     });
}).controller('RoomsAdminController', function($scope, $http) {
	var API_ROOMS = '../../../api/v1/protected/admin/rooms';

	$http.get('resources/model-room.json').success(function(data) {
		$scope.model = data;
	});

	$http.get(API_ROOMS).success(function(data){
		$scope.data = data;
	});

	$scope.selectedEntry;
    $scope.operation = 'show';
    $scope.newEntry = createEmptyEntry(); 
    $scope.errorMessage = null;
	
	$scope.showInfoForEntry = function(entry) {
		if ($scope.operation === 'show') {
			if($scope.selectedEntry === entry){
				$scope.showEntry = false;
				$scope.selectedEntry = null;
				entry._selected_ = false;
			} else {
				for(var i = 0 ; i < $scope.data.length; i ++){
					$scope.data[i]._selected_ = false;
				}
				entry._selected_ = true;
				$scope.showEntry = true;
				$scope.selectedEntry = entry;
			}
		}
	};
			
	$scope.setOperation = function(operation) {
		switch (operation) {
			case 'new':
				if ($scope.operation !== 'new') {
					$scope.operation = 'new';
				} else {
					$scope.operation = 'show';
				}
				break;
			case 'update':
				if ($scope.operation !== 'update') {
					if ($scope.selectedEntry) {
						$scope.operation = 'update';
					} else {
						alert("Please first select entry for updated");
						$scope.operation = 'show';
					}
				} else {
					$scope.operation = 'show';
				}
				break;
			case 'delete':
				if ($scope.operation !== 'delete') {
					$scope.operation = 'delete';
				} else {
					$scope.operation = 'show';
				}
				break;
			default:
				$scope.operation = 'show';
				break;
		}
    };
           
    $scope.confirmAction = function() {
        switch ($scope.operation) {
            case 'show':
                break;
            case 'new':
                newEntry(transformEntry($scope.newEntry));
                break;
            case 'update':
                updateEntry(transformEntry($scope.selectedEntry));
                break;
        }
    };

    $scope.cancelAction = function() {
        refreshData();
    };

   $scope.delete = function() {
	   if ($scope.selectedEntry) {
         	var confirmed = confirm('Do you realy want to delete the selected entry?');
           	if (confirmed) {
               	delete $scope.selectedEntry._selected_;
                   deleteEntry($scope.selectedEntry);
                   $scope.operation = 'show';
           	}                    	
       } else {
           alert('Please select row from the table.');
       }
   };
            
	function newEntry(entry) {
		delete $scope.newEntry._selected_;
		$http.post(API_ROOMS, entry).success(function() {
			refreshData();
			$scope.selectedEntry = null;
            $scope.operation = 'show';
            $scope.newEntry = createEmptyEntry();
            $scope.errorMessage = null;
		}).error(function(response) {
			$scope.errorMessage = response.message;
		});
	}
	
	function updateEntry(entry) {
		delete $scope.selectedEntry._selected_;
		$http.put(API_ROOMS + '/' + entry.roomId, entry).success(function() {
			refreshData();
            $scope.operation = 'show';
            $scope.errorMessage = null;
		}).error(function(response) {
			$scope.errorMessage = response.message;
		});
	}

	function deleteEntry(entry) {
		var deleteUrl = API_ROOMS + "/" + entry["roomId"];
		$http.delete(deleteUrl).success(function(){
			refreshData();
            $scope.selectedEntry = null;
			$scope.errorMessage = null;
		}).error(function(response){
			$scope.errorMessage = response.message;
		});
	}
            
	function refreshData() {
		$http.get(API_ROOMS).success(function(data){
			$scope.data = data;
        	$scope.newEntry = createEmptyEntry();
            $scope.selectedEntry = null;
            $scope.operation = 'show';
            $scope.errorMessage = null;
		}).error(function(response){
			$scope.errorMessage = response.message;
		});
	}

    function createEmptyEntry() {
    	return {
    		'roomId': null,
    		'roomType': null,
    		'roomView': null,
    		'bedType': null,
    		'defaultPricePerNight': 0,
    		'imagesUrl': [],
    		'description': null
    	}
    }

    function transformEntry(entry) {
    	var entryCopy = entry;
		var images = entryCopy.imagesUrl;
		if (images != null && !Array.isArray(images)) {
			entryCopy.imagesUrl = images.split(',');
		}
		return entryCopy;
	}
}).controller('MenuController', function($scope, $http) {
	$http.get('resources/menu.json').success(function(data) {
		$scope.menus = data;
    });
}).controller('BookingsAdminController', function($scope, $http){

	var API_BOOKINGS = '../../../api/v1/protected/admin/bookings';

	$http.get('resources/model-booking.json').success(function(data) {
		$scope.model = data;
	});

	$http.get(API_BOOKINGS).success(function(data){
		$scope.data = data;
	});

	$scope.selectedEntry;
    $scope.operation = 'show';
    $scope.errorMessage = null;
	
	$scope.showInfoForEntry = function(entry) {
		if ($scope.operation === 'show') {
			if($scope.selectedEntry === entry){
				$scope.showEntry = false;
				$scope.selectedEntry = null;
				entry._selected_ = false;
			} else {
				for(var i = 0 ; i < $scope.data.length; i ++){
					$scope.data[i]._selected_ = false;
				}
				entry._selected_ = true;
				$scope.showEntry = true;
				$scope.selectedEntry = entry;
			}
		}
	};
			
	$scope.setOperation = function(operation) {
		switch (operation) {
			case 'updateBookingStatus':
				if ($scope.operation !== 'updateBookingStatus') {
					if ($scope.selectedEntry) {
						$scope.operation = 'updateBookingStatus';
					} else {
						alert("Please first select entry for updated");
						$scope.operation = 'show';
					}
				} else {
					$scope.operation = 'show';
				}
				break;
			case 'updatePaymentStatus':
				if ($scope.operation !== 'updatePaymentStatus') {
					if ($scope.selectedEntry) {
						$scope.operation = 'updatePaymentStatus';
					} else {
						alert("Please first select entry for updated");
						$scope.operation = 'show';
					}
				} else {
					$scope.operation = 'show';
				}
				break;
			case 'delete':
				if ($scope.operation !== 'delete') {
					$scope.operation = 'delete';
				} else {
					$scope.operation = 'show';
				}
				break;
			default:
				$scope.operation = 'show';
				break;
		}
    };
           
    $scope.confirmAction = function() {
        switch ($scope.operation) {
            case 'show':
                break;
            case 'updateBookingStatus':
                updateEntryBookingStatus($scope.selectedEntry);
                break;
            case 'updatePaymentStatus':
            	updateEntryPaymentStatus($scope.selectedEntry);
            	break;
        }
    };

    $scope.cancelAction = function() {
        refreshData();
    };

   $scope.delete = function() {
	   if ($scope.selectedEntry) {
         	var confirmed = confirm('Do you realy want to delete the selected entry?');
           	if (confirmed) {
               	delete $scope.selectedEntry._selected_;
                   deleteEntry($scope.selectedEntry);
                   $scope.operation = 'show';
           	}                    	
       } else {
           alert('Please select row from the table.');
       }
   };
            
	function updateEntryBookingStatus(entry) {
		delete $scope.selectedEntry._selected_;
		$http.put(API_BOOKINGS + '/' + entry.bookingId + '/booking/status', entry.bookingStatus).success(function() {
			refreshData();
            $scope.operation = 'show';
            $scope.errorMessage = null;
		}).error(function(response) {
			$scope.errorMessage = response.message;
		});
	}

	function updateEntryPaymentStatus(entry) {
		delete $scope.selectedEntry._selected_;
		$http.put(API_BOOKINGS + '/' + entry.bookingId + '/payment/status', entry.paymentStatus).success(function() {
			refreshData();
            $scope.operation = 'show';
            $scope.errorMessage = null;
		}).error(function(response) {
			$scope.errorMessage = response.message;
		});
	}

	function deleteEntry(entry) {
		var deleteUrl = API_BOOKINGS + "/" + entry["bookingId"];
		$http.delete(deleteUrl).success(function(){
			refreshData();
            $scope.selectedEntry = null;
			$scope.errorMessage = null;
		}).error(function(response){
			$scope.errorMessage = response.message;
		});
	}
            
	function refreshData() {
		$http.get(API_BOOKINGS).success(function(data){
			$scope.data = data;
            $scope.selectedEntry = null;
            $scope.operation = 'show';
            $scope.errorMessage = null;
		}).error(function(response){
			$scope.errorMessage = response.message;
		});
	}
}).controller('UsersAdminController', function($scope, $http){

	$http.get('resources/model-user.json').success(function(data) {
		$scope.model = data;
	});

	$http.get('../../../api/v1/protected/admin/users').success(function(data) {
		$scope.data = data;
	});
});