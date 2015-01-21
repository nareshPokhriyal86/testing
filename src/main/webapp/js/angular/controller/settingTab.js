var app = angular.module('settingApp',[]);
	app.controller('settingCtrl', function($scope) {
		
		$scope.campaigns=[
	         {valueinclicks:'1000', time:'1',timeduration:'days'}
	        ];
		$scope.addCampaign = function() {
			 this.campaigns.push( {valueinclicks:'1000', time:'1',timeduration:'days'});
		
		};
		$scope.removeCampaign = function(camp) {
	         var index = this.campaigns.indexOf(camp);
	        this.campaigns.splice(index, 1); 
		};
		
		$scope.addValue = function (){
			$scope. obj = { value: null,text:null,kpi:null,kpival:null};
			this.obj.value = $("#kpival").val() + "_" + $("#kpi").val();
			this.obj.text = $("#kpival").val() + " " + $("#kpi option:selected").text();
			this.obj.kpi = $("#kpi").val();
			this.obj.kpival = $("#kpival").val();
			$('#benchmark').tagsinput('add',this.obj );
		};
		
		$scope.showValue = function (){
				console.log($('#benchmark').tagsinput('items'));
		};
	});