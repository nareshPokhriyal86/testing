'use strict';


$(window).load(function() {	
	
});

 var isChanged = false;
 var smartMediaPlanApp = angular.module('smartMediaPlanApp',[]);
 
 smartMediaPlanApp.controller('campaignHeaderCtrl', ['$scope','$filter', function($scope,$filter) {
	try{
		console.log("---header----");
		$scope.totalImpression=0;
		$scope.netRevenue=0;	
		$scope.servingFeeAmount='0.04';  //$0.04
		$scope.cost=0;	
		
	/*	
		$scope.campaignHeader={
				'budget' : '$8,400.00',
				'impression' : '2,645,669',
			    'eCPM' :'$3.18',
			    'cost' : '$6,614.18',
			    'servingFee' : '$105.82',
			    'netRevenue' : '$1,680.00'
		};*/
		
		$scope.campaignHeader= campaignJsonObj.header;
		
	
		$scope.placements=campaignJsonObj.placements;

	
	    //$scope.products=campaignJsonObj.products;
	
		$scope.smartMediaPlan={
				'header':$scope.campaignHeader,
				'placements':$scope.placements,
				/*'products':$scope.products*/
		};
	
	   $scope.productHashTable = new HashTable();
	   $scope.placementHashTable = new HashTable();
	
	   $scope.init=function(){
		  
		   $scope.placementHashTable=new HashTable();
		   $scope.productHashTable=new HashTable();
		   var placements=$scope.placements;
		   if($scope.campaignHeader ==null || $scope.campaignHeader==undefined){
			   $('#errorDiv').html(''+campaignJsonObj.error);
		   }
		   //$scope.productHashTable
		   angular.forEach(placements, function(placement, key) {	
			   var placementId=placement.id;
			   $scope.placementHashTable.setItem(placementId, placement);
			   
			   var productArray=placement.products;
			   angular.forEach(productArray, function(product, index) {	
				   var pId=product.id;
				   var hashKey=placementId+"_"+pId;
				   $scope.productHashTable.setItem(hashKey,product);
				   console.log('added..'+hashKey);
				   console.log(product);
			   });
		   });
		   console.log("init():hashTables: placemnts:"+$scope.placementHashTable.length+" and products:"+ $scope.productHashTable.length);		   
	   };
	   
	   $scope.init();
	   
	   $scope.changed=function(val) {
		   isChanged = val;
	   };
	   
	   	   
	   $scope.updateProducts=function(placementId,productId){
			 console.log('update products placementId:'+placementId+" and productId--"+productId);
			 var values = $scope.placements;
		     //console.log(values);
		     var allocatedImp=0;
		     var allProductsBudget=0;
		     console.log("total products..."+values.length);
		     var updateProduct=false;
		     var updateOverFlow=false;
		     var placementGoalImp=0;
		     var productIndex=0;
		     var overflowProductKey=0;
		     var totalImpWithOutOverflow=0;
		     var OverflowImp=0;
		     var placementBudget=0;
		     var doUpdate=false;
		     
		     angular.forEach(values, function(value, key) {		    	
		    	 var id=value.id;  //placementId
		    	 
		    	 console.log("placement key---"+key);
		    	 if(placementId == id){
		    		 console.log("You are modifying placemnt --"+id);
		    		 placementGoalImp=value.impression;
		    		 placementBudget=parseFloat(value.budget);
		    		 var prodcutArray=value.products;
		    		 
		    		 angular.forEach(prodcutArray, function(product, productKey) {
		    			 
		    			 var pId=product.id;
		    			 var productImpStr=product.allocatedImp+'';
		    			 var alloImp=parseInt(productImpStr.replaceAll(',',''));
		    			 if(pId==0 || pId=='0'){
		    				 OverflowImp=alloImp;
		    				 overflowProductKey=productKey;
		    			 }else{
		    				 totalImpWithOutOverflow=parseInt(totalImpWithOutOverflow) + alloImp;
		    			 }		    			 
		    			
				    	 allocatedImp=allocatedImp+ alloImp;	//for all products for a placement
				    	 
				    	 var budget=parseFloat(product.budget);
				    	
				    	 allProductsBudget= parseFloat(budget)+parseFloat(allProductsBudget); //for all products for a placement
				    	 
				    	 console.log('cumulative budget:'+allProductsBudget+" and cumulative Imp :"+allocatedImp);
				    	 var notANumber=isNaN(alloImp);
				    	 
				    	 if(pId==productId && (pId != 0 || pId !='0')){	
				    		
					    	 if( (notANumber || alloImp <=0) && 
					    			 (product.partner_id !=null && product.partner_id.indexOf('_')<0)){// for new partner
					    		 doUpdate=false;
					    		 toastr.error("Please provide a valid allocation for impressions > 0");
					    	 }else{
					    		 doUpdate=true;

					    		 productIndex=productKey;
					    		 console.log("You have modified product --"+pId+" with productIndex:"+productIndex);
					    		 var availableImpStr=product.availableImp+'';
					    		 var availableImp=parseInt(availableImpStr.replaceAll(',',''));
					    		 
					    		 if( (parseInt(alloImp) > availableImp )
					    				 && (product.partner_id !=null && product.partner_id.indexOf('_')<0)){
					    			 doUpdate=false;
					    			 console.log("Can not allocate more than available...");
					    			 //toastr.error("You can not allocate impressions beyond available impressions for this product.");
					    			 updateProduct=false;
					    			 updateOverFlow=false;
					    		 }else{		
					    			 console.log("Update both.....");
					    			 updateProduct=true;
					    			 updateOverFlow=true;
					    		 }
					    	 }
					    	 
				    	 }else if((pId==productId) && (pId == 0 || pId =='0')){
				    		 if(notANumber ){
					    		 doUpdate=false;
					    		 //toastr.error("Please provide a valid allocated impression.");
					    		 return false;
					    	 }else{
					    		 doUpdate=true;
					    		 productIndex=productKey;
					    		 console.log("2...You have modified overflow product --"+pId+" with productIndex:"+productIndex);
					    		 updateOverFlow=true;
					    		 updateProduct=false;
					    	 }				    					    		 
				    	 }
				    	
				    	 
		    		 });
		    		 var product=prodcutArray[productIndex];
		    		 
		    		 if(doUpdate && updateProduct && updateOverFlow){
		    			 //update product and overflow both..
		    			 
		    			 var newOverFlowImp=placementGoalImp-totalImpWithOutOverflow;
		    			 console.log('update product and overflow both..placementGoalImp:'+
		    					 placementGoalImp+" and totalImpWithOutOverflow:"+totalImpWithOutOverflow
		    					 +" and newOverFlowImp:"+newOverFlowImp);
		    			 
		    			 var productImpStr=product.allocatedImp+'';
	    				 var productImp=productImpStr.replaceAll(',','');
	    				 var rate= parseFloat(product.rate);		
			    		 var budget = parseFloat( (parseInt(productImp)* parseFloat(rate) )/1000 ).toFixed(3);
			    		 if(parseFloat(budget) < 0.01){
			    			 budget=0.00;
			    		 }else{
			    			 budget =parseFloat( (parseInt(productImp)* parseFloat(rate) )/1000 ).toFixed(2);
			    		 }
			    		 product.budget=budget;	 
			    		 var revenueShare=product.revenueSharingPercent;
			    		 var payout= parseFloat(parseFloat(revenueShare) * budget/100).toFixed(3);
			    		 if(parseFloat(payout) < 0.01){
			    			 payout=0.00;
			    		 }else{
			    			 parseFloat(parseFloat(revenueShare) * budget/100).toFixed(2);
			    		 }
			    		 
			    		 if(productImp ==0){
			    			 //product.cpm=0;
			    		 }else{
			    			 var netRate= parseFloat( (parseFloat(payout)/parseInt(productImp))*1000).toFixed(2);
			    			 if(netRate==0){
			    				 netRate=parseFloat(rate*parseFloat(revenueShare)/100).toFixed(2);
			    			 }
				    		 product.cpm=netRate;
			    		 }
			    		 product.payout=formatNumber(parseFloat(payout));
			    		 var hashKey=placementId+"_"+product.id;
			    		 $scope.productHashTable.setItem(hashKey,product);
			    		 
			    		 //update overflow product
			    		 var overflowProduct=prodcutArray[overflowProductKey];
			    		 overflowProduct.allocatedImp=newOverFlowImp;
	    				 rate= parseFloat(overflowProduct.rate);		
			    		
			    		 budget = parseFloat( (parseInt(newOverFlowImp)* rate )/1000 ).toFixed(3);
			    		 if(parseFloat(budget) < 0.01){
			    			 budget=0.00;
			    		 }else{
			    			 budget = parseFloat( (parseInt(newOverFlowImp)* rate )/1000 ).toFixed(2);
			    		 }
			    		 overflowProduct.budget=budget;	    		 
			    		 revenueShare=overflowProduct.revenueSharingPercent;
			    		 payout= parseFloat(parseFloat(revenueShare) * budget/100).toFixed(3);
			    		 if(parseFloat(payout) < 0.01){
			    			 payout=0.00;
			    		 }else{
			    			 payout=parseFloat(parseFloat(revenueShare) * budget/100).toFixed(2); 
			    		 }
			    		 
			    		 if(newOverFlowImp == 0){
			    			 netRate=0;
			    		 }else{
			    			 netRate= parseFloat( (parseFloat(payout)/parseInt(newOverFlowImp))*1000).toFixed(2);
			    		 }				    		
			    		 overflowProduct.cpm=netRate;
			    		 overflowProduct.payout=formatNumber(parseFloat(payout));
			    		 var hashKey2=placementId+"_"+overflowProduct.id;
			    		 $scope.productHashTable.setItem(hashKey2,overflowProduct);
			    		 
			    		 
		    		 }else if(doUpdate && updateOverFlow){
		    			 
		    			 var total=OverflowImp+totalImpWithOutOverflow;
		    			 console.log('update overflow only..OverflowImp:'+OverflowImp+" and totalImpWithOutOverflow:"
		    					 +totalImpWithOutOverflow+ " : total:"+total);
		    			 if(total > placementGoalImp){
		    				 //toastr.error('You can not allocate more impressions than corresponding placement goal');
		    			 }else{
		    				console.log("update overflow...");
		    				 var overflowProduct=prodcutArray[overflowProductKey];
		    				 var overFlowproductImpStr=overflowProduct.allocatedImp+'';
				    		 var overFlowproductImp=overFlowproductImpStr.replaceAll(',','');
		    				 rate= (overflowProduct.rate+'').replace('$','');		
				    		
				    		 var budget = parseFloat( (parseInt(overFlowproductImp)* parseFloat(rate) )/1000 ).toFixed(3);
				    		 if(parseFloat(budget) < 0.01){
				    			 budget=0.00;
				    		 }else{
				    			 budget = parseFloat( (parseInt(overFlowproductImp)* parseFloat(rate) )/1000 ).toFixed(2);
				    		 }
				    		 overflowProduct.budget=budget;	    		 
				    		 var revenueShare=overflowProduct.revenueSharingPercent;
				    		 var payout= parseFloat(parseFloat(revenueShare) * budget/100).toFixed(3);				    		 
				    		 if(parseFloat(payout) < 0.01){
				    			 payout=0.00;
				    		 }else{
				    			 payout= parseFloat(parseFloat(revenueShare) * budget/100).toFixed(2); 
				    		 }
				    		 var netRate= parseFloat( (parseFloat(payout)/parseInt(overFlowproductImp))*1000).toFixed(3);
				    		 if(parseFloat(netRate) < 0.01){
				    			 netRate=0.00;
				    		 }else{
				    			 netRate= parseFloat( (parseFloat(payout)/parseInt(overFlowproductImp))*1000).toFixed(2); 
				    		 }
				    		 overflowProduct.cpm=netRate;
				    		 overflowProduct.payout=formatNumber(parseFloat(payout));
				    		
				    		 var hashKey=placementId+"_"+overflowProduct.id;
				    		 $scope.productHashTable.setItem(hashKey,overflowProduct);
		    			 }
		    			 
		    		 }
		    		 //Now update placement also....
		    		 
		    	 }	    	
		    	
		     });
		     $scope.updateCampaignHeader();
		     //$scope.updatePlacement(placementId,allocatedImp,allProductsBudget);		     
	};
		
	$scope.updateCampaignHeader=function(){
		console.log("Update campaign header.....................");
		 var values = $scope.placements; 
	     //console.log(values);
	     var totalImp=0;
	     var totalBudget=0.0;
	     var totalPayout=0.0;
	     
	     angular.forEach(values, function(value, key) {
	    	 var imp=value.impression+'';
	    	 imp=imp.replaceAll(',','');
	    	 totalImp=totalImp+ parseInt(imp);
	    	 
	    	 var budget= (value.budget+'').replace('$','');
	    	 budget=parseFloat(budget.replaceAll(',','')).toFixed(2);
	    	 totalBudget= parseFloat(totalBudget)+parseFloat(budget);
	     });
	     console.log("1...totalImp:"+totalImp+", totalBudget:"+totalBudget+" and totalPayout:"+totalPayout);
	     $scope.campaignHeader.impression=parseInt(totalImp);
	     $scope.campaignHeader.budget=totalBudget;
	     var eCPM= parseFloat( (totalBudget*1000)/totalImp ).toFixed(2);
	     
	     $scope.campaignHeader.eCPM=eCPM;
	     var totalServingFee= parseFloat( (totalImp * (parseFloat($scope.servingFeeAmount).toFixed(2) ) ) /1000 ).toFixed(2);
	     $scope.campaignHeader.servingFee=totalServingFee;
	     
	     //$scope.campaignHeader.cost='$'+formatNumber(totalPayout);
	     $scope.updatePayoutAndCost(totalServingFee,totalBudget);
	    	     
	     
	};
	
	$scope.updatePayoutAndCost=function(totalServingFee,totalBudget){
		 //var values = $scope.products; 
		 var totalPayout=0.0;
		 console.log("$scope.productHashTable :"+$scope.productHashTable.length);
		 $scope.productHashTable.each(function(key, value) {
			 var payout= (value.payout+'').replace('$','');
	    	 payout=parseFloat(payout.replaceAll(',',''));
	    	 totalPayout=parseFloat(totalPayout)+parseFloat(payout);
	    	 console.log('2...totalPayout...'+totalPayout+" and payout--"+payout);		 
		 });
	      
	     $scope.campaignHeader.cost=totalPayout;
	     $scope.cost=totalPayout;
	     var netCost=parseFloat(totalPayout)+parseFloat(totalServingFee);
	     console.log('3...netCost:'+netCost+" and totalBudget:"+totalBudget);
	     var netRevenue= parseFloat(parseFloat(totalBudget)-parseFloat(netCost));
	     $scope.campaignHeader.netRevenue=netRevenue;
	};
	
	$scope.deleteItem = function (index, placementId) {
		try{
			var partner='';
			console.log('index...'+index+" and placementId:"+placementId);
			var placementsArray=$scope.placements;
			angular.forEach(placementsArray, function(placement, key) {
				if(placement.id == placementId){
					console.log('splice a product from placement....');
					var productArray=placement.products;
					var product=productArray[index];
					console.log(product);
					var productId=product.id;
					partner=product.partner;
					console.log("Befor delete :"+productArray.length);
					productArray.splice(index,1);
					console.log("After delete -- "+productArray.length);
					var hashkey=placementId+"_"+productId;
					$scope.productHashTable.removeItem(hashkey);				
				}
			});
			
			console.log($scope.placements);		
			//$scope.products.splice(index, 1);
			$scope.updatePlacementAfterDeletingProduct(placementId);
			
			partnersJSObjArray=partnerHashTable.getItem(placementId);
 			for(var i=0;i<partnersJSObjArray.length;i++){
				 var partnerObj=partnersJSObjArray[i];
				 if(partner == partnerObj.getPartnerName()){
					 console.log("deleted --true, setSelected to: false ");
					 partnerObj.setSelected(false);
					 partnerName=partnerObj.getPartnerName();
					 break;
				 }	
				 //console.log(partnerObj);
			 }
 			 partnerHashTable.setItem(placementId,partnersJSObjArray);
 			 
			
		}catch(error){
			console.log("Error--"+error);
		}
		
	};
		

	$scope.updatePlacementAfterDeletingProduct=function(placementId){
		 console.log('update placement after deleting product........placementId:'+placementId);
		 var values = $scope.placements;
	     //console.log(values);
	     var allocatedImp=0;
	     var allProductsBudget=0;
	     console.log("total products..."+values.length);
	     var updateProduct=false;
	     var updateOverFlow=false;
	     var placementGoalImp=0;
	     var productIndex=0;
	     var overflowProductKey=0;
	     var totalImpWithOutOverflow=0;
	     var OverflowImp=0;
	     
	     angular.forEach(values, function(value, key) {		    	
	    	 var id=value.id;  //placementId
	    	 
	    	 console.log("placement key---"+key);
	    	 if(placementId == id){
	    		 console.log("You are modifying placemnt --"+id);
	    		 placementGoalImp=value.impression;
	    		 var prodcutArray=value.products;
	    		 
	    		 angular.forEach(prodcutArray, function(product, productKey) {	    			 
	    			 var pId=product.id;
	    			
	    			 if(pId==0 || pId=='0'){
	    				 OverflowImp=parseInt(product.allocatedImp);
	    				 overflowProductKey=productKey;
	    			 }else{
	    				 totalImpWithOutOverflow=parseInt(totalImpWithOutOverflow) + parseInt(product.allocatedImp);
	    			 }
	    			 
	    			 var alloImp=product.allocatedImp+'';  //only for a product		    			 
			    	 alloImp=alloImp.replaceAll(',','');
			    	 
			    	 allocatedImp=allocatedImp+ parseInt(alloImp);	//for all products for a placement
			    	 
			    	 var budget=(product.budget+'').replace('$','');  //only for a product
			    	 budget=budget.replaceAll(',','');
			    	 
			    	 allProductsBudget= parseFloat(budget)+parseFloat(allProductsBudget); //for all products for a placement
			    	 
			    	 console.log('cumulative budget:'+allProductsBudget+" and cumulative Imp :"+allocatedImp);
			    	 
			    	 if( pId == 0 || pId =='0' ){
			    		 productIndex=productKey;
			    		 console.log("2...You have modified overflow product --"+pId+" with productIndex:"+productIndex);
			    		 updateOverFlow=true;
			    		 updateProduct=false;				    		 
			    	 }
			    	 
	    		 });
	    		//var product=prodcutArray[productIndex];
    			 
    		     if(updateOverFlow){	    			 
					 var total=OverflowImp+totalImpWithOutOverflow;
					 console.log('update overflow only..OverflowImp:'+OverflowImp+" and totalImpWithOutOverflow:"
							 +totalImpWithOutOverflow+ "total:"+total);
					 if(total > placementGoalImp){
						 //toastr.error('You can not allocate more than placement goal...2');
					 }else{
						 console.log('update overflow....');
						 var overflowProduct=prodcutArray[overflowProductKey];
						 //console.log(overflowProduct);
						 var netOverFlowImp=placementGoalImp-totalImpWithOutOverflow;
						 overflowProduct.allocatedImp=netOverFlowImp;
						 
						 var overFlowproductImp=overflowProduct.allocatedImp;			    		
			    		 			    		 
						 var rate= (overflowProduct.rate+'').replace('$','');		
			    		
			    		 var budget = parseFloat( (parseInt(overFlowproductImp)* parseFloat(rate) )/1000 ).toFixed(2);
			    		 overflowProduct.budget=budget;	    		 
			    		 var revenueShare=overflowProduct.revenueSharingPercent;
			    		 var payout= parseFloat(parseFloat(revenueShare) * budget/100).toFixed(2);
			    		 var netRate= parseFloat( (parseFloat(payout)/parseInt(overFlowproductImp))*1000).toFixed(2);
			    		 overflowProduct.cpm=netRate;
			    		 overflowProduct.payout=formatNumber(parseFloat(payout));
			    		 //console.log(overflowProduct);
			    		 var hashKey=placementId+"_"+overflowProduct.id;
			    		 $scope.productHashTable.setItem(hashKey,overflowProduct);
					 }
    		   }
	    			    		
	    	 }	    	
	    	
	     });
	     $scope.updateCampaignHeader();
	     
   };
	
$scope.modifyProductsByBudget=function(placementId,productId){			 
			 console.log('modifyProductsByBudget.......placementId:'+placementId+" and productId--"+productId);
			 var values = $scope.placements;
		     console.log("total products..."+values.length);
		     var doUpdate=false;
		  
		     var productHashKey="";
		     var productIndex=0;
		     
		     angular.forEach(values, function(value, key) {		    	
		    	 var id=value.id;  //placementId
		    	 
		    	 console.log("placement key---"+key);
		    	 if(placementId == id){
		    		 console.log("You are modifying placemnt --"+id);
		    		 var productArray=value.products;
		    		 var placementBudget=parseFloat(value.budget);
		    		 var placementGoal=parseInt(value.impression);
		    		 var cumulativeProductsBudget=0.0;
		    		 var cumulativeImp=0;
		    		 
		    		 angular.forEach(productArray, function(product, prodIndex) {
		    			 var pId=product.id;
		    			 var allocatedBudgetStr=product.budget+'';
		    			 var allocatedBudget=parseFloat(allocatedBudgetStr.replaceAll(',',''));
		    			 
		    			 var notANumber=isNaN(allocatedBudget);
	    				 if(notANumber || allocatedBudget <0){
	    					// toastr.error('Please enter a valid numeric value.');
	    					 doUpdate=false;
	    					 allocatedBudget=0;
	    				 }else{
	    					 doUpdate=true;
	    					 if(pId==productId){		    				
			    				 productHashKey=placementId+"_"+pId;
			    				 productIndex=prodIndex;	    				
			    				 		    				 
			    			 }else{
			    				 cumulativeImp= parseInt(cumulativeImp)+parseInt(product.allocatedImp);
			    			 }
			    			 cumulativeProductsBudget=parseFloat(cumulativeProductsBudget)+parseFloat(allocatedBudget);			    			 
	    				 }
		    			 
		    		 });
		    		 
		    		 //console.log('cumulativeProductsBudget:'+cumulativeProductsBudget+' and placementBudget:'+placementBudget);
		    		 if(cumulativeProductsBudget > placementBudget){
		    			 var diff=parseFloat(cumulativeProductsBudget)-parseFloat(placementBudget);
		    			 diff=parseFloat(diff).toFixed(4);
		    			 console.log("Invalid budget, all product budget should not be more than placement budget..diff:"+diff);
		    			 if(parseFloat(diff) < 0.5){		    				 
			    			 doUpdate=true;
		    			 }else{
		    				 //toastr.error('Allocated budget value of products exceeds the goal of placement');
			    			 doUpdate=false;
		    			 }
		    		
		    		 }
		    		 
		    		 if(doUpdate){
		    			 var product=productArray[productIndex];
		    		 
		    			 var allocatedBudget=parseFloat(product.budget);
		    			 var rate=parseFloat(product.rate);
		    			 var avlImp=parseInt(product.availableImp);
		    			 var allocatedImpressions= Math.round((allocatedBudget*1000.0)/rate);
		    			 
		    			 cumulativeImp=cumulativeImp+allocatedImpressions;
		    			
		    			 if(allocatedImpressions > avlImp && (product.id != '0' || product.id != 0)){
		    				 doUpdate=false;
		    				 //toastr.error("Invalid budget, due to this allocatedImpression would exceed available impression.");
		    			 }else if(placementGoal < cumulativeImp){
		    				 var impDiff=cumulativeImp-placementGoal;
		    				 if(impDiff==1){
		    					 console.log('Round off error...impDiff:'+impDiff);
		    					 doUpdate=true;
		    					 
			    				 product.allocatedImp=parseInt(allocatedImpressions-1);		    				 
			    				 var revenueShare=product.revenueSharingPercent;
					    		 var payout= parseFloat(parseFloat(revenueShare) * allocatedBudget/100).toFixed(2);
					    		 var netRate=0;
					    		 if(parseInt(allocatedImpressions) >0){
					    			 netRate= parseFloat( (parseFloat(payout)/parseInt(allocatedImpressions))*1000).toFixed(2);
					    		 }
					    		
					    		 product.cpm=netRate;
					    		 product.payout=payout;
					    		 //console.log(product);	
					    		 $scope.productHashTable.setItem(productHashKey,product);
		    				 }else{
		    					 doUpdate=false;
			    				 //toastr.error("Invalid budget, due to this all products allocated impressions are exceeding placement goal impressions.");			    				  
		    				 }
		    				//console.log('placementGoal :'+placementGoal+" and cumulativeImp:"+cumulativeImp);
		    			 }else if(placementGoal > cumulativeImp){
		    				 var impDiff=placementGoal-cumulativeImp;
		    				 if(impDiff==1){
		    					 console.log('Round off error : impDiff:'+impDiff);
		    					 doUpdate=true;
		    					 if(id == '0' || id == 0){
		    						 product.allocatedImp=parseInt(allocatedImpressions+1);	
		    					 }else if( parseInt(product.availableImp) > allocatedImpressions){
		    						 product.allocatedImp=parseInt(allocatedImpressions+1);	
		    					 }else{
		    						 product.allocatedImp=parseInt(allocatedImpressions);	
		    					 }
			    				 	    				 
			    				 var revenueShare=product.revenueSharingPercent;
					    		 var payout= parseFloat(parseFloat(revenueShare) * allocatedBudget/100).toFixed(2);
					    		 var netRate=0;
					    		 if(parseInt(allocatedImpressions) >0){
					    			 netRate= parseFloat( (parseFloat(payout)/parseInt(allocatedImpressions))*1000).toFixed(2);
					    		 }else{
					    			 netRate= parseFloat( rate * parseFloat(revenueShare)/100).toFixed(2);
					    		 }	
					    		 
					    		 product.cpm=netRate;
					    		 product.payout=payout;
					    		 //console.log(product);	
					    		 $scope.productHashTable.setItem(productHashKey,product);
		    				 }else{
		    					 doUpdate=false;
			    				 //toastr.error("Invalid budget, due to this all products allocated impressions are exceeding placement goal impressions.");			    				  
		    				 }
		    				console.log('placementGoal :'+placementGoal+" and cumulativeImp:"+cumulativeImp);
		    			 }
		    			 else{
		    				 console.log('updating product.....');
		    				 doUpdate=true;
		    				 product.allocatedImp=allocatedImpressions;		    				 
		    				 var revenueShare=product.revenueSharingPercent;
				    		 var payout= parseFloat(parseFloat(revenueShare) * allocatedBudget/100).toFixed(2);
				    		 
				    		 var netRate=0;
				    		 if(parseInt(allocatedImpressions) >0){
				    			 netRate= parseFloat( (parseFloat(payout)/parseInt(allocatedImpressions))*1000).toFixed(2);
				    		 }else{
				    			 netRate= parseFloat( rate * parseFloat(revenueShare)/100).toFixed(2);
				    		 }			    		 
				    		 
				    		 product.cpm=netRate;
				    		 product.payout=payout;
				    		 //console.log(product);	
				    		 $scope.productHashTable.setItem(productHashKey,product);
		    			 }
		    		 }
		    		 
		    	 }	    	
		    	
		     });
		     
		     if(doUpdate){
		    	 $scope.updateCampaignHeader();
		     }
		     
	};
	
	$scope.modifyProductsByRate=function(placementId,productId){			 
		 console.log('modifyProductsByRate.......placementId:'+placementId+" and productId--"+productId);
		 var values = $scope.placements;
	     var doUpdate=false;
	  
	     var productHashKey="";
	     var productIndex=0;
	     
	     angular.forEach(values, function(value, key) {		    	
	    	 var id=value.id;  //placementId
	    	 var placementRate=parseFloat(value.rate);
	    	 if(placementId == id){	    		 
	    		 var productArray=value.products;
	    		 var placementBudget=parseFloat(value.budget);
	    		 var placementGoal=parseInt(value.impression);
	    		 var cumulativeProductsBudget=0;
	    		 var cumulativeImp=0;
	    		 
	    		 angular.forEach(productArray, function(product, prodIndex) {
	    			 var pId=product.id;
	    			 var rate=parseFloat(product.rate);
	    			 var allocatedImp=parseInt(product.allocatedImp);
	    			 
	    			 var notANumber=isNaN(rate);
	   				 if(notANumber || rate <0){
	   					 toastr.error('Please enter a valid numeric value.');
	   					 doUpdate=false;
	   					 rate=0;
	   				 }else if(rate > placementRate){
	   					 //toastr.error('Product rate can not be greater than placement rate.');
	   					 doUpdate=false;
	   				 }else{
	   					 doUpdate=true;
	   					 if(pId==productId){		    				
			    				 productHashKey=placementId+"_"+pId;
			    				 productIndex=prodIndex;	 
		    			 }
	   					 cumulativeImp= parseInt(cumulativeImp+allocatedImp);
	   					 var allocatedBudget=parseFloat(allocatedImp*rate/1000.0).toFixed(2);
	   					 //console.log("allocatedBudget:"+allocatedBudget+" and rate:"+rate+" and allocatedImp:"+allocatedImp);
			    		 cumulativeProductsBudget=parseFloat(cumulativeProductsBudget)+ parseFloat(allocatedBudget);
	   				 }
		    			 
		    	 });
			    		 
	    		 //console.log('cumulativeProductsBudget:'+cumulativeProductsBudget+' and placementBudget:'+placementBudget);
	    		 if(cumulativeProductsBudget > placementBudget){
	    			 console.log("Invalid rate, due to this all products budget would exceed placement budget.");
	    			 //toastr.error('Invalid rate, due to this all products budget would exceed placement budget.');
	    			 doUpdate=false;
	    		 }else if(doUpdate){
	    			 var product=productArray[productIndex];
	    			 
	    			 var allocatedImp=parseInt(product.allocatedImp);
	    			 var rate=parseFloat(product.rate);
	    			 var allocatedBudget=parseFloat(allocatedImp*rate/1000.0).toFixed(2);
    				 doUpdate=true;
    				 product.budget=allocatedBudget;		    				 
    				 var revenueShare=product.revenueSharingPercent;
		    		 var payout= parseFloat(parseFloat(revenueShare) * allocatedBudget/100).toFixed(2);
		    		 var netRate=0;
		    		 if(allocatedImp >0){
		    			 netRate= parseFloat( (parseFloat(payout)/parseInt(allocatedImp))*1000).toFixed(2);
		    		 }else{
		    			 netRate= parseFloat( rate * parseFloat(revenueShare)/100).toFixed(2);
		    		 }
		    		 
		    		 product.cpm=netRate;
		    		 product.payout=payout;
		    		 console.log(product);	
		    		 $scope.productHashTable.setItem(productHashKey,product);
	    			
	    		 }
	    		 
	    	 }	    	
	    	
	     });
	     
	     if(doUpdate){
	    	 $scope.updateCampaignHeader();
	     }
	     
    };
	
	$scope.modifyProductsByRevenuePercent=function(placementId,productId){	
			 console.log('modifyProductsByRevenuePercent.......placementId:'+placementId+" and productId--"+productId);
			 var values = $scope.placements;
		     var doUpdate=false;
		     angular.forEach(values, function(value, key) {		    	
		    	 var id=value.id;  //placementId
		    	 if(placementId == id){
		    		 
		    		 var productArray=value.products;
		    		 
		    		 angular.forEach(productArray, function(product, productKey) {
		    			 var pId=product.id;
		    			 
		    			 if(pId==productId){		    				
		    				 var hashKey=placementId+"_"+pId;
		    				 var oldProduct=$scope.productHashTable.getItem(hashKey);
		    				 var rate=parseFloat(product.rate);
		    				 var oldRevenuePercent=parseInt(oldProduct.revenueSharePercent);		    				 
		    				 var revenueSharePercent=parseInt(product.revenueSharingPercent);
		    				 if(oldRevenuePercent == revenueSharePercent){
		    					 console.log("No chnage in value...");
		    					 doUpdate=false;
		    				 }else{
		    					 var notANumber=isNaN(revenueSharePercent);
			    				 if(notANumber || revenueSharePercent >100){
			    					// toastr.error('Please enter a valid numeric less than 100');
			    					 doUpdate=false;
			    				 }else{
			    					 doUpdate=true;
			    					 var allocatedBudget=parseFloat(product.budget);
				    				 var allocatedImp=parseInt(product.allocatedImp);
				    				 var payout= parseFloat(allocatedBudget*revenueSharePercent/100.0).toFixed(2);
				    				 var netRate=0;
				    				 if(allocatedImp >0){
				    					 netRate=  parseFloat( (payout/allocatedImp)*1000.0 ).toFixed(2);
				    				 }else{
						    			 netRate= parseFloat( rate * parseFloat(revenueSharePercent)/100).toFixed(2);
						    		 }
				    				
				    				 product.revenueSharingPercent=revenueSharePercent;
				    				 product.cpm=parseFloat(netRate);
				    				 product.payout=parseFloat(payout);
				    				 $scope.productHashTable.setItem(hashKey,product);
				    				 //console.log(product);
			    				 }
		    				 }	
		    			 }
		    		 });
		    		 
		    	 }	    	
		    	
		     });
		     
		     if(doUpdate){
		    	 $scope.updateCampaignHeader();
		     }
		     
	};
	
	$scope.modifyProductsByPayout=function(placementId,productId){		
		 console.log('modifyProductsByPayout.......placementId:'+placementId+" and productId--"+productId);
		 var values = $scope.placements;
	     var doUpdate=false;
	     
	     angular.forEach(values, function(value, key) {		    	
	    	 var id=value.id;  //placementId
	    	 var placementName=value.name;	    	 
	    	 if(placementId == id){
	    		 var productArray=value.products;
	    		 
	    		 angular.forEach(productArray, function(product, productKey) {
	    			 var pId=product.id;
	    			 
	    			 if(pId==productId){
	    				 //console.log("You are modifying product --"+productId);
	    				 var hashKey=placementId+"_"+productId;
	    				 var paoutStr=product.payout+'';
	    				 var payout=parseFloat(paoutStr.replaceAll(',',''));	  
	    				 var allocatedBudget=parseFloat(product.budget);	
	    				 var rate=parseFloat(product.rate);
	    				 var notANumber=isNaN(payout);
	    				 if(notANumber || payout < 0 || payout > allocatedBudget ){
	    					 doUpdate=false;
	    					 //toastr.error("Payout must not be greater than allocated budget, see placement :"+ placementName+" and proudct:"+product.name);
	    					 
	    				 }else{
	    					 doUpdate=true;
	    					 var revenueSharePercent=70;
	    					 if(allocatedBudget > 0.0){	    					
	    						 revenueSharePercent=Math.round( (payout*100.0)/allocatedBudget);
	    						 if(revenueSharePercent <=0 || revenueSharePercent==null || revenueSharePercent==undefined){
	    							 revenueSharePercent=70; 
	    						 }
	    					 }
		    				 
		    				 var allocatedImp=parseInt(product.allocatedImp);	
		    				 var netRate=0;
		    				 if(allocatedImp >0){
		    					 netRate=  parseFloat( (payout/allocatedImp)*1000.0 ).toFixed(2);
		    				 }else{
				    			 netRate= parseFloat( rate * parseFloat(revenueSharePercent)/100).toFixed(2);
				    		 }
		    				 
		    				 product.cpm=parseFloat(netRate);
		    				 product.revenueSharingPercent=revenueSharePercent;
		    				 product.payout=payout;
		    				 $scope.productHashTable.setItem(hashKey,product);
		    				 
	    				 }   				 
	    			 }
	    		 });
	    		 
	    	 }	    	
	    	
	     });
	     if(doUpdate){
	    	 $scope.updateCampaignHeader();
	     }    
    };

    $scope.modifyProductsByNetRate=function(placementId,productId){		
		 console.log('modifyProductsByNetRate..placementId:'+placementId+" and productId--"+productId);
		 var values = $scope.placements;
	     var doUpdate=false;
	     angular.forEach(values, function(value, key) {		    	
	    	 var id=value.id;  //placementId
	    	 
	    	 //console.log("placement key---"+key);
	    	 if(placementId == id){
	    		 //console.log("You are modifying placemnt --"+id);
	    		 
	    		 var productArray=value.products;
	    		 
	    		 angular.forEach(productArray, function(product, productKey) {
	    			 var pId=product.id;
	    			 
	    			 if(pId==productId){
	    				// console.log("You are modifying product --"+productId);
	    				 var hashKey=placementId+"_"+productId;
	    				 //var oldProduct=$scope.productHashTable.getItem(hashKey);
	    				 //var oldRate=parseFloat(oldProduct.cpm);
	    				 var netRate=parseFloat(product.cpm);	
	    				 
	    				 
	    				 var notANumber=isNaN(netRate);
	    				 if(notANumber){
	    					 doUpdate=false;
	    					 //toastr.error('Please enter a valid rate.');
	    				 }else{	    					
	    					 var allocatedImp=parseInt(product.allocatedImp);
		    				 var allocatedBudget=parseFloat(product.budget);
		    				 var payout= parseFloat(netRate *allocatedImp /1000.0).toFixed(2);
		    				 //console.log("payout - "+payout+", allocatedBudget - "+allocatedBudget);
		    				 if(payout > allocatedBudget){
		    					 console.log('Please enter a valid rate as payout  '+payout+' is exceeding allocatedBudget..'+allocatedBudget);
		    					 //toastr.error('Please enter a valid rate as payout  '+payout+' is exceeding budget..');
		    					 doUpdate=false;
		    					 product.cpm=parseFloat(product.rate*product.revenueSharingPercent/100).toFixed(2);
		    				 }else{
		    					 doUpdate=true;
		    					 var revenueSharePercent=70;
		    					 if(allocatedBudget !=null && (allocatedBudget !=0 || allocatedBudget !=0.0)){
		    						 revenueSharePercent=Math.round(payout*100.0/allocatedBudget);
		    					 }			    				 
			    				 product.cpm=netRate;
			    				 product.payout=parseFloat(payout);
			    				 product.revenueSharingPercent=revenueSharePercent;
			    				 $scope.productHashTable.setItem(hashKey,product);
			    				 //console.log(product);
		    				 }
	    					
	    				 }
	    			 }
	    		 });
	    		 
	    	 }	    	
	    	
	     });
	     if(doUpdate){
	    	 $scope.updateCampaignHeader();
	     }     
   };
   
   
   
   $scope.validatePage=function(){			 
		 console.log("validatePage.....");
		 var values = $scope.placements;
	     console.log("total placements..."+values.length);
	     var invalidImpressionAllocation=false;
	     var invalidBudgetAllocation=false;
	     var invalidPayoutAllocation=false;
	     var doUpdate=true;
	   	     
	     angular.forEach(values, function(value, key) {	
	    	 
	    	 if(doUpdate){
	    		 var placementId=value.id;  
		    	 
		    	 console.log("checking placementId---"+placementId);
		    	
	    		 var productArray=value.products;
	    		 var placementBudget=parseFloat(value.budget);
	    		 var placementGoal=parseInt(value.impression);
	    		 var placementName=value.name;
	    		 var cumulativeProductsBudget=0;
	    		 var cumulativeImp=0;
	    		 var cumulativePayout=0;
	    		 var placementPayout=parseFloat($scope.campaignHeader.cost);
	    		 angular.forEach(productArray, function(product, prodIndex) {
	    			 if(doUpdate){
	    				 var rate=parseFloat(product.rate);
	    				 var pId=product.id;
		    			 var allocatedImp=parseInt( (product.allocatedImp+'').replaceAll(',',''));
		    			 var availableImp=parseInt(product.availableImp);
		    			 if(allocatedImp > availableImp && (pId !=0 || pId !='0')
		    					 && (product.partner_id !=null && product.partner_id.indexOf('_')<0)){
		    				 doUpdate=false;
		    				 toastr.error('Allocated Impressions are exceeding available impressions for a particular product.');
		    			 }
		    			 cumulativeImp= parseInt(cumulativeImp+allocatedImp);
						 var allocatedBudget=parseFloat(product.budget);			     
			    		 cumulativeProductsBudget=parseFloat(cumulativeProductsBudget)+ parseFloat(allocatedBudget);
			    		 console.log("cumulativeProductsBudget:"+cumulativeProductsBudget+" and cumulativeImp:"+cumulativeImp);
			    		
			    		 var revenuePervcentShare=parseInt(product.revenueSharingPercent);
			    		 if(revenuePervcentShare >100){
			    			 doUpdate=false;
			    			 toastr.error("RevenueShare(%) must not be greater than 100, see placement :"+placementName+" and proudct:"+product.name);
			    		 }
			    		 var payout=parseFloat(product.payout);
			    		 cumulativePayout=parseFloat(cumulativePayout+payout);
			    		 if(payout > allocatedBudget && (pId !=0 || pId !='0')){
			    			 doUpdate=false;
			    			 toastr.error("Payout is exceeding than allocated budget, see placement :"+placementName+" and proudct:"+product.name);
			    		 }
			    		 if(pId ==0 || pId =='0'){
			    			 if(allocatedImp <0 ){
			    				 doUpdate=false;
			    				 toastr.error("Allocated Impressions to products are exceeding available impressions for placement -"+placementName);
			    			 }else if(allocatedBudget <0){
			    				 doUpdate=false;			    				
			    				 toastr.error("Allocated budget to products are exceeding available budget for placement -"+placementName);
			    			 }
			    		 }
	    			 }
	    			 
		    	 });
			    
	    		 if(doUpdate){
	    			 console.log('cumulativeImp:'+cumulativeImp+' and placementGoal:'+placementGoal);
		    		 /*if(cumulativeImp > placementGoal){
		    			 var diffImp=parseInt(cumulativeImp)-parseInt(placementGoal);
		    			 console.log("cumulativeImp is greater than placementGoal--diffImp:"+diffImp);
		    			 if(diffImp <=0){
		        			 doUpdate=true;
		         			 invalidImpressionAllocation=false;
		         			 saveMediaPlan=true; 
		    			 }else{
		        			 doUpdate=false;
		         			// toastr.error("Allocated impression value of products exceeds the goal of placement");
		         			 invalidImpressionAllocation=true;
		         			 saveMediaPlan=false; 
		    			 }

		    		 }else if(cumulativeImp < placementGoal){
		    			 var diffImp=parseInt(placementGoal)-parseInt(cumulativeImp);
		    			 console.log("cumulativeImp is less than placementGoal-- diffImp :"+diffImp);
		    			 if(diffImp<=0){
		        			 doUpdate=true;
		        			 invalidImpressionAllocation=false;
		        			 saveMediaPlan=true; 
		    			 }else{
		        			 doUpdate=false;
		        			 invalidImpressionAllocation=true;
		        			 //toastr.error("Allocated impression value of products is less than the goal of placement");
		        			 saveMediaPlan=false; 
		    			 }

		    		 }*/ 
	    			 var diffImp=parseInt(cumulativeImp)-parseInt(placementGoal);
	    			 if(diffImp >= 0) {
	    				 console.log("cumulativeImp is greater than placementGoal--diffImp:"+diffImp);
	    				 doUpdate=true;
	         			 invalidImpressionAllocation=false;
	         			 saveMediaPlan=true;
	    			 }else {
	    				 console.log("cumulativeImp is less than placementGoal-- diffImp :"+diffImp);
	    				 doUpdate=false;
	         			 invalidImpressionAllocation=true;
	         			 saveMediaPlan=false;
	    			 }
	    		 }
	    		 	 
	    		 
	    		 if(doUpdate){
	    			 console.log('cumulativeProductsBudget:'+cumulativeProductsBudget+' and placementBudget:'+placementBudget);
	    			 if(cumulativeProductsBudget > placementBudget){
		    			 var budgetDiff=parseFloat(cumulativeProductsBudget)-parseFloat(placementBudget);
		    			 budgetDiff=parseFloat(budgetDiff).toFixed(4);
		    			 console.log("cumulativeProductsBudget is greater than placementBudget-- budgetDiff:"+budgetDiff);
		    			 if(parseFloat(budgetDiff) > 0.5){
		    				 doUpdate=false;
		    	    	     // toastr.error("Allocated budget value of products exceeds the budget of placement");
		    	    		 invalidBudgetAllocation=true;
		    	    		 saveMediaPlan=false; 
		    			 }else{
		    				doUpdate=true;
		    	    		// toastr.error("Allocated budget value of products exceeds the budget of placement");
		    	    		invalidBudgetAllocation=false;
		    	    		saveMediaPlan=true; 
		    			 }
		    			 
		    		 }else if(cumulativeProductsBudget < placementBudget){
		    			 var budgetDiff=parseFloat(placementBudget)-parseFloat(cumulativeProductsBudget);
		    			 budgetDiff=parseFloat(budgetDiff).toFixed(4);
		    			 console.log("cumulativeProductsBudget is less than placementBudget--budgetDiff:"+budgetDiff);
		    			 if(parseFloat(budgetDiff) < 0.5){
		    				 doUpdate=true;
		    	    		// toastr.error("Allocated budget value of products is less than the budget of placement");
		    	    		 invalidBudgetAllocation=false;
		    	    		 saveMediaPlan=true;  
		    			 }else{
		    				 doUpdate=false;
		    	    		// toastr.error("Allocated budget value of products is less than the budget of placement");
		    	    		 invalidBudgetAllocation=true;
		    	    		 saveMediaPlan=false; 
		    			 }
		    			
		    		 }
	    		 }
	    		 
	    		 if(doUpdate){
	    			 console.log('cumulativePayout:'+cumulativePayout+' and placementPayout:'+placementPayout);
	    			 var diffPayout=parseInt(cumulativePayout)-parseInt(placementPayout);
	    			 if(diffPayout <= 0.5 && diffPayout >= -0.5) {
	    				 console.log("cumulativePayout is greater than placementpayout--diffPayout:"+diffPayout);
	    				 doUpdate=true;
	    				 invalidPayoutAllocation=false;
	         			 saveMediaPlan=true;
	    			 }else {
	    				 console.log("cumulativePayout is less than placementpayout-- diffPayout :"+diffPayout);
	    				 doUpdate=false;
	    				 invalidPayoutAllocation=true;
	         			 saveMediaPlan=false;
	    			 }
	    		 }
	    	
	    	 }
	    	 
    		 
	    	
	     });
	     
	     if(doUpdate){
	    	console.log("campaign is oky, save it..........");
	    	saveMediaPlan=true;
	     }else{
	    	 console.log("campaign is not oky, correct it first..");
		     saveMediaPlan=false;
		     if(invalidImpressionAllocation){
		    	 toastr.error("Allocated impression value of products should match the goal of placements");
		     }else if(invalidBudgetAllocation){
		    	 toastr.error("Allocated budget value of products should match the budget of placements");
		     }else if(invalidPayoutAllocation){
		    	 toastr.error("Allocated payout value of products should match the payout of placements");
		     }
	     }
	     
     };
     
  
     $scope.hasChanged=false;
     $scope.hasModified=false;
     
     $scope.changeProduct=function(placementId,productId){
    	 $scope.hasChanged=true;
    	 console.log("Changed......"+$scope.hasChanged);
     };
     
     
     $scope.addProductWithNewPartner = function (placementId,partnerId,partnerName) {
    		console.log("placementId:"+placementId);
  			var placementsArray=$scope.placements;
  			angular.forEach(placementsArray, function(placement, key) {
  				if(placement.id == placementId){
  					console.log('add a product in placement....'+placementId);
  					var productArray=placement.products;
  					var rate=placement.rate;
  					var revenuePercentage=70;
  					var eCPM=parseFloat(parseFloat(rate)*revenuePercentage/100).toFixed(2);
  					var product= { "id":productArray.length,
 			 					   "partner":partnerName,
 			 					   "name":partnerName,
 			 					   "availableImp":0,
 			 					   "allocatedImp":0,
 			 					   "rate":rate,
 			 					   "budget":0,
 			 					   "revenueSharingPercent":revenuePercentage,
 			 					   "payout":0,
 			 					   "cpm":eCPM,
 			 					   "placementId":placementId,
 			 					   "partner_adserver_id":"0",
 			 					   "partner_id":"0_"+partnerId,
 			 					   "partner_logo":""
 			 					  };
  					productArray.push(product); 					
  					var productId=product.id;
  					var hashkey=placementId+"_"+productId;
  					$scope.productHashTable.setItem(hashkey,product);				
  				}
  			});
 		
 	};
 	
 	$scope.addPartner=function( placementId ){
 		
 		 var $modal = $('#partnerModel').modal({
 								show:false }).css({'width': '65%' });
 		 //$("#myModalLabel").html("chartTitle");
 		 $modal.modal('show');
 		 
 		 
 		 console.log("add partner to placement : "+placementId);
 		 //var scope = angular.element($("#campaignCtrlDiv")).scope();
 		 //scope.addProductWithNewPartner(placementId);
 		 
 		 $scope.createPartnerDropDown(placementId);
 		 
 	 }
 	 
 	$scope.createPartnerDropDown= function (placementId){
 		
 		var placementsArray=$scope.placements;
 		var placementWiseProductPartners=new HashTable();
 		angular.forEach(placementsArray, function(placement, key) {
				if(placement.id == placementId){
					var productArray=placement.products;
					$.each(productArray, function (index, product) {
						placementWiseProductPartners.setItem(product.name,product);
						console.log("add product with partner name as key in hashtable -- "+product.name);
	 			  });			
				}
		});
	 		
 		 if(!partnerHashTable.hasItem(placementId)){
 			$('#partnerName').empty().append('<option value="-1">Select a partner</option>');
 			  partnersJSObjArray=[];
 			  $.each(campaignJsonObj.partners, function (index, item) {
 				  
 				  if( !placementWiseProductPartners.hasItem(item.name) ){
 					$('#partnerName').append($('<option>', { 
  				        value: placementId+'_'+item.id,
  				        text : item.name 
  				    }));
 					partnersJSObjArray[partnersJSObjArray.length]=new partnersJSObject(item.id,item.name,placementId);
 				  } 	
 				    
 			  });
 			  partnerHashTable.setItem(placementId,partnersJSObjArray);
 			  console.log("added in hash table partnersJSObjArray: "+partnersJSObjArray.length+" and placementId:"+placementId);
 		 }else{	 
 			
 	 		
 			 $('#partnerName').empty().append('<option value="-1">Select a partner</option>');
 			 partnersJSObjArray=partnerHashTable.getItem(placementId);
 			 console.log("From hashtable partnersJSObjArray: "+partnersJSObjArray.length+" and placementId:"+placementId);
 			 
 			 for(var i=0;i<partnersJSObjArray.length;i++){
 				 var partnerObj=partnersJSObjArray[i];
 				 //console.log(partnerObj);
 				 if(!partnerObj.getSelected() &&  ( !placementWiseProductPartners.hasItem(partnerObj.getPartnerName())) ){
 					 $('#partnerName').append($('<option>', { 
 					        value: placementId+'_'+partnerObj.getId(),
 					        text : partnerObj.getPartnerName() 
 					    }));
 				 }
 			 }
 			 
 		 }
 	    console.log('partner dropdown ends for placementId--'+placementId);
 	 }
 	 
 	 $scope.addProductCart=function(){
 		var selectedValue=$('#partnerName').val();
 		//console.log("selectedValue : "+selectedValue);
 		if(selectedValue != '-1'){
 			var arr=selectedValue.split("_");
 			var placementId=arr[0];
 			var partnerId=arr[1];
 			var partnerName='';
 			partnersJSObjArray=partnerHashTable.getItem(placementId);
 			for(var i=0;i<partnersJSObjArray.length;i++){
				 var partnerObj=partnersJSObjArray[i];
				 if(partnerId == partnerObj.getId()){
					 //console.log("selected -- "+true);
					 partnerObj.setSelected(true);
					 partnerName=partnerObj.getPartnerName();
					 break;
				 }	
				 console.log(partnerObj);
			 }
 			 partnerHashTable.setItem(placementId,partnersJSObjArray);
 			 $scope.addProductWithNewPartner(placementId,partnerId,partnerName);
 			 
 			var $modal = $('#partnerModel').modal({
					show:false }).css({'width': '65%' });
 			$modal.modal('hide');
 		}
 	 }
 	 
	}catch(err){
	  console.log("Error -- "+err);
	}
}
 
 ]); 


//focus directive
 smartMediaPlanApp.directive('myFocus', function () {
	
     return {
         restrict: 'A',
         link: function (scope, element, attr) {
        	 scope.hasChanged=false;
             scope.$watch(attr.myFocus, function (n, o) {
                 if (n != 0 && n) {
                     element[0].focus();
                 }
             });
         }
     };
 });
 //blur directive
 smartMediaPlanApp.directive('myBlur', function () {
	    return {
	        restrict: 'A',
	        link: function (scope, element, attr) {
	            element.bind('blur', function () {	                
	                scope.$apply(attr.myBlur);
	                //return scope value for focusing to false
	                scope.$eval(attr.myFocus + '=false');
	                console.log("On blur value changed status......"+scope.hasChanged);
	            });
	        }
	    };
 });


smartMediaPlanApp.directive('currencyInput', function($filter, $browser) {
    return {
        require: 'ngModel',
        link: function($scope, $element, $attrs, ngModelCtrl) {
            var listener = function() {
                var value = $element.val().replace(/,/g, '')
                $element.val($filter('number')(value, false))
            }
            
            // This runs when we update the text field
            ngModelCtrl.$parsers.push(function(viewValue) {
                return viewValue.replace(/,/g, '');
            })
            
            // This runs when the model gets updated on the scope directly and keeps our view in sync
            ngModelCtrl.$render = function() {
                $element.val($filter('number')(ngModelCtrl.$viewValue, false))
            }
            
            $element.bind('change', listener)
            $element.bind('keydown', function(event) {
                var key = event.keyCode
                // If the keys include the CTRL, SHIFT, ALT, or META keys, or the arrow keys, do nothing.
                // This lets us support copy and paste too
                if (key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40)) 
                    return 
                $browser.defer(listener) // Have to do this or changes don't get picked up properly
            })
            
            $element.bind('paste cut', function() {
                $browser.defer(listener)  
            })
        }
        
    }
});


smartMediaPlanApp.directive('ngConfirmClick', [
function(){
	return {
		link: function (scope, element, attr) {
			var msg = attr.ngConfirmClick || "Are you sure?";
			var clickAction = attr.confirmedClick;
			element.bind('click',function (event) {
				if ( window.confirm(msg) ) {
					scope.$apply(clickAction);
				}
			});
		}
	};
}]);
  

String.prototype.splice = function(idx, rem, s) {
    return (this.slice(0, idx) + s + this.slice(idx + Math.abs(rem)));
};

smartMediaPlanApp.directive('myDirective', function() {
    return {
        restrict: 'A',
        require: '?ngModel',
        scope: {},
        link: function (scope, element, attrs, controller) {
            scope.$watch(function(){return controller.$viewValue}, function(newVal,oldVal) {
                console.log("Changed to " + newVal +" : oldValue: "+oldVal);
                oldVal=(oldVal+'').replaceAll(',','');
                newVal=(newVal+'').replaceAll(',','');
                var oldValNum=parseInt(oldVal);
                var newValNum=parseInt(newVal);
                console.log("oldValNum : "+oldValNum+" and newValNum:"+newValNum);
                
                if(newVal == oldVal){
                	scope.hasModified=true;
                }else{
                	scope.hasModified=false;
                }
                
            });
        }
    }
});


/*smartMediaPlanApp.directive('currencyInput', function() {
    return {
        restrict: 'A',
        scope: {
            field: '='
        },
        replace: true,
        template: '<span><input type="text" ng-model="field"></input>{{field}}</span>',
        link: function(scope, element, attrs) {

            $(element).bind('keyup', function(e) {
                var input = element.find('input');
                var inputVal = input.val();

                //clearing left side zeros
                while (scope.field.charAt(0) == '0') {
                    scope.field = scope.field.substr(1);
                }

                scope.field = scope.field.replace(/[^\d.\',']/g, '');

                var point = scope.field.indexOf(".");
                if (point >= 0) {
                    scope.field = scope.field.slice(0, point + 3);
                }

                var decimalSplit = scope.field.split(".");
                var intPart = decimalSplit[0];
                var decPart = decimalSplit[1];

                intPart = intPart.replace(/[^\d]/g, '');
                if (intPart.length > 3) {
                    var intDiv = Math.floor(intPart.length / 3);
                    while (intDiv > 0) {
                        var lastComma = intPart.indexOf(",");
                        if (lastComma < 0) {
                            lastComma = intPart.length;
                        }

                        if (lastComma - 3 > 0) {
                            intPart = intPart.splice(lastComma - 3, 0, ",");
                        }
                        intDiv--;
                    }
                }

                if (decPart === undefined) {
                    decPart = "";
                }
                else {
                    decPart = "." + decPart;
                }
                var res = intPart + decPart;

                scope.$apply(function() {scope.field = res});

            });

        }
    };
});*/


function formatNumber(number){
    number = number.toFixed(2) + '';
    var x = number.split('.');
    var x1 = x[0];	      
    var x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    if(x2 == '.00' ){
  	  return x1;
    }else{
  	  return x1 + x2;
    }
 }

function deletePlacement(productId) {
	try{	  
	   /*  $.ajax({
	      type : "POST",
	      url : "/deleteProduct.lin?productId="+productId+"&adServerId="+adServerId,
	      cache: false,	    
	      dataType: 'json',
	      error : function(err) {
			console.log("delete error : " + err);
			toastr.error('Delete Failed');
		  },
		  success : function(data) {
			$('#tr_'+productId).remove();
			console.log("delete : success");
			toastr.success('Deleted Successfully');
		  }
	   });    */
	  }catch(error){
		 
	  }
}


String.prototype.replaceAll = function(stringToFind, stringToReplace) {
     if (stringToFind == stringToReplace) return this;
     var temp = this;
     var index = temp.indexOf(stringToFind);
     while (index != -1) {
         temp = temp.replace(stringToFind, stringToReplace);
         index = temp.indexOf(stringToFind);
     }
     return temp;
 };