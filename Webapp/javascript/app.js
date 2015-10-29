/**
 * Created by dean on 20/10/15.
 */
/** javascript file **/

var app = angular.module('evaApp', []);

app.controller('MovieController', function($scope, $http) {
	var pendingTask;

	function fetch() {
		$http({
			method: 'POST',
			url: "http://groep6api.herokuapp.com/register",
			headers: {'Content-Type': 'application/x-www-form-urlencoded'},
			transformRequest: function (obj) {
				var str = [];
				for (var p in obj)
					str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
				return str.join("&");
			},
			data: {username: "username", password: "password"}
		}).success(function (result) {
			console.log(result);
		});
	}
});

$('#modal-id').show('toggle');
