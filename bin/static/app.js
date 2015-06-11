(function(){
	var app = angular.module('WTDApp', []);

	app.controller('ListController',['$scope','$http', function($scope,$http){

		$scope.taskName = '';

		$http.get('http://localhost:8080/tasks').success(function(data){
			$scope.taskList = data;
		});

		$scope.addTask = function(){
			$http.post('http://localhost:8080/addTask/1/'+$scope.taskName).success(function(data){
				$scope.taskList = data;
			});
			$scope.taskName = '';
		}

		$scope.removeTask = function(taskId){
			$http.post('http://localhost:8080/removeTask/'+taskId).success(function(data){
				$scope.taskList = data;
			});
		};

	}]);

}());