/**
 * Created by dean on 20/10/15.
 */
/** javascript file **/

var app = angular.module('evaApp', []);

app.controller('MainCtrl', [
	'$scope',
	function($scope){
		$scope.test = 'Hello world!';
	}]);

$('#modal-id').show('toggle');
