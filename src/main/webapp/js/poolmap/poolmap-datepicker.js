

if (startDate == undefined){
		  var startDate ;
		  var endDate;
		  var compareStartDate;
		  var compareEndDate ;
		  var timePeriod;
}

$(document).ready(function() { 
	
	
	
    if(Math.floor(moment().month() / 3) + 1 == '1'){
		var range_start_quater = moment().startOf('year');
		var range_end_quater = moment().startOf('year').add('month', 2).add('days',30);
		var range_start_lastquater = moment().startOf('year').subtract('month',3);
		var range_end_lastquater = moment().endOf('year').subtract('month', 12);
	}
    
    if(Math.floor(moment().month() / 3) + 1 == '2'){
    	var range_start_quater = moment().startOf('year').add('month', 3);
    	var range_end_quater = moment().startOf('year').add('month', 5).add('days',29);
    	var range_start_lastquater = moment().startOf('year');
    	var range_end_lastquater = moment().startOf('year').add('month', 2).add('days',30);
    }
    if(Math.floor(moment().month() / 3) + 1 == '3'){
    	var range_start_quater = moment().startOf('year').add('month', 6);
    	var range_end_quater = moment().startOf('year').add('month', 8).add('days',29);
    	var range_start_lastquater = moment().startOf('year').add('month', 3);
    	var range_end_lastquater = moment().startOf('year').add('month', 5).add('days',29);
    }
    if(Math.floor(moment().month() / 3) + 1 == '4'){
    	var range_start_quater = moment().startOf('year').add('month', 9);
    	var range_end_quater = moment().endOf('year');
    	var range_start_lastquater = moment().startOf('year').add('month', 6);
    	var range_end_lastquater = moment().startOf('year').add('month', 8).add('days',29);
    }
					
    $('#daterange').daterangepicker({
        ranges: {
           //'Today': [new Date(), new Date()],
           //'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
           //'Last 7 Days': [moment().subtract('days', 6), new Date()],
           //'Last 30 Days': [moment().subtract('days', 29), new Date()],
           'This Month': [moment().startOf('month'), moment().endOf('month')],
           'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')],
 		   'This Quater':[range_start_quater,range_end_quater],
 		   'Last Quater':[range_start_lastquater,range_end_lastquater],
 		   'This Year':[moment().startOf('year'), moment().endOf('year')],
 		   'Last Year':[moment().startOf('year').subtract('month', 12), moment().endOf('year').subtract('month', 12)],
 						   
 						   
        },
        
        opens: 'left',
        format: 'MM/DD/YYYY',
        separator: ' to ',
        startDate: moment().add('days', 1), //new Date(),
        endDate:   moment().add('days', 1).add('month', 0).endOf('month'), //moment().add('days', 30),
        minDate:  new Date(), //'01/01/2012',
        //maxDate: new Date(),
        locale: {
            applyLabel: 'Apply',
            fromLabel: 'From',
            toLabel: 'To',
            customRangeLabel: 'Custom Range',
            daysOfWeek: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr','Sa'],
            monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
            firstDay: 1
        },
        showWeekNumbers: false,
        buttonClasses: [''],
        dateLimit: false
     },
 					 
     function(start, end) {
    	    if(Math.floor(moment().month() / 3) + 1 == '1'){
 			var range_start_quater = moment().startOf('year');
 			var range_end_quater = moment().startOf('year').add('month', 2).add('days',30);
 			var range_start_lastquater = moment().startOf('year').subtract('month',3);
 			var range_end_lastquater = moment().endOf('year').subtract('month', 12);
   	    } 
 		if(Math.floor(moment().month() / 3) + 1 == '2'){
 			var range_start_quater = moment().startOf('year').add('month', 3);
 			var range_end_quater = moment().startOf('year').add('month', 5).add('days',29);
 			var range_start_lastquater = moment().startOf('year');
 			var range_end_lastquater = moment().startOf('year').add('month', 2).add('days',30);
 		}
 		if(Math.floor(moment().month() / 3) + 1 == '3'){
 			var range_start_quater = moment().startOf('year').add('month', 6);
 			var range_end_quater = moment().startOf('year').add('month', 8).add('days',29);
 			var range_start_lastquater = moment().startOf('year').add('month', 3);
 			var range_end_lastquater = moment().startOf('year').add('month', 5).add('days',29);
 		}
 		if(Math.floor(moment().month() / 3) + 1 == '4'){
 			var range_start_quater = moment().startOf('year').add('month', 9);
 			var range_end_quater = moment().endOf('year');
 			var range_start_lastquater = moment().startOf('year').add('month', 6);
 			var range_end_lastquater = moment().startOf('year').add('month', 8).add('days',29);
 		}

         if($("#compare_checbox").prop('checked') == true){
 			if ($('.ranges .active').text() == 'Today') {
 				var todaytoyesterday= moment().subtract('days', 1);
 				var enddates =  moment().subtract('days', 1);
 				var formated_todaytoyesterdaystart = todaytoyesterday.format('MMMM D, YYYY') ;
 				var formated_todaytoyesterdayend = todaytoyesterday.format('MMMM D, YYYY') ;
 				var date_timePeriod = 'Today';
 			}
 			if ($('.ranges .active').text() == 'Yesterday') {
 				var todaytoyesterday= moment().subtract('days', 2);
 				var enddates =  moment().subtract('days', 2);
 				var formated_todaytoyesterdaystart = todaytoyesterday.format('MMMM D, YYYY') ;
 				var formated_todaytoyesterdayend = todaytoyesterday.format('MMMM D, YYYY') ;
 				var date_timePeriod = 'Yesterday';
 			}
 			if ($('.ranges .active').text() == 'Last 7 Days') {
 				var todaytoyesterday= moment().subtract('days', 13);
 				var enddates = moment().subtract('days', 7);
 				var formated_todaytoyesterdaystart = todaytoyesterday.format('MMMM D, YYYY') ;
 				var formated_todaytoyesterdayend = enddates.format('MMMM D, YYYY') ;
 				var date_timePeriod = 'Last 7 Days';
 			}
 			if ($('.ranges .active').text() == 'Last 30 Days') {
 				var todaytoyesterday= moment().subtract('days', 59);
 				var enddates = moment().subtract('days', 30);
 				var formated_todaytoyesterdaystart = todaytoyesterday.format('MMMM D, YYYY') ;
 				var formated_todaytoyesterdayend = enddates.format('MMMM D, YYYY') ;
 				var date_timePeriod = 'Last 30 Days';
 			}
 			if ($('.ranges .active').text() == 'This Month') {
 				var todaytoyesterday= moment().subtract('month', 1).startOf('month');
 				var enddates = moment().subtract('month', 1).endOf('month');
 				var formated_todaytoyesterdaystart = todaytoyesterday.format('MMMM D, YYYY') ;
 				var formated_todaytoyesterdayend = enddates.format('MMMM D, YYYY') ;
 				var date_timePeriod = 'This Month';
 			}
 			if ($('.ranges .active').text() == 'Last Month') {
 				var todaytoyesterday= moment().subtract('month', 2).startOf('month');
 				var enddates = moment().subtract('month', 2).endOf('month');
 				var formated_todaytoyesterdaystart = todaytoyesterday.format('MMMM D, YYYY') ;
 				var formated_todaytoyesterdayend = enddates.format('MMMM D, YYYY') ;
 				var date_timePeriod = 'Last Month';
 			}
 			if ($('.ranges .active').text() == 'Custom Range') {
 				var user_startdate= start.format('MMMM D, YYYY');
 				var user_enddate =end.format('MMMM D, YYYY');
 				
 				startDate = start.format('YYYY-MM-DD');
 				endDate=end.format('YYYY-MM-DD');
 				var start_date_custom = moment([start.year(), start.month(), start.date()]);
 				var end_date_custom = moment([end.year(), end.month(), end.date()]);
 				
 				var diffrence = end_date_custom.diff(start_date_custom, 'days') +1;
 				
 				var todaytoyesterday= start.subtract('days', diffrence);
 				var enddates = end.subtract('days', diffrence);
 				var formated_todaytoyesterdaystart = todaytoyesterday.format('MMMM D, YYYY') ;
 				var formated_todaytoyesterdayend = enddates.format('MMMM D, YYYY') ;
 				
 				username= user_startdate +"~"+ user_enddate +"~"+ formated_todaytoyesterdaystart +"~"+ formated_todaytoyesterdayend;
 										  //alert("dsdaasd"+username)
 				//setCookie("username",username,1);
 			   
 				$('#daterange div').html(user_startdate + ' - ' +user_enddate +'</br>');
 			    //$('#daterange p').html('Compared to:' + formated_todaytoyesterdaystart + ' - ' + formated_todaytoyesterdayend);
 				
 				compareStartDate= todaytoyesterday.format('YYYY-MM-DD');
 				compareEndDate=enddates.format('YYYY-MM-DD');
 				timePeriod = user_startdate + ' - ' +user_enddate ;

 			}
 			if ($('.ranges .active').text() == 'This Year') {
 				var todaytoyesterday= moment().startOf('year').subtract('month', 12);
 				var enddates = moment().endOf('year').subtract('month', 12);
 				var formated_todaytoyesterdaystart = todaytoyesterday.format('MMMM D, YYYY') ;
 				var formated_todaytoyesterdayend = enddates.format('MMMM D, YYYY') ;
 				var date_timePeriod = 'This Year';
 			
 			}
 			if ($('.ranges .active').text() == 'Last Year') {
 				var todaytoyesterday= moment().startOf('year').subtract('month', 24);
 				var enddates = moment().endOf('year').subtract('month', 24);
 				var formated_todaytoyesterdaystart = todaytoyesterday.format('MMMM D, YYYY') ;
 				var formated_todaytoyesterdayend = enddates.format('MMMM D, YYYY') ;
 				var date_timePeriod = 'Last Year';
 			
 			}
 			if ($('.ranges .active').text() == 'This Quater') {
 				var todaytoyesterday= range_start_lastquater;
 				var enddates = range_end_lastquater;
 				var formated_todaytoyesterdaystart = todaytoyesterday.format('MMMM D, YYYY') ;
 				var formated_todaytoyesterdayend = enddates.format('MMMM D, YYYY') ;
 				var date_timePeriod = 'This Quater';
 			
 			}
 			if ($('.ranges .active').text() == 'Last Quater') {
 				var todaytoyesterday= range_start_lastquater.subtract('month', 3);
 				var enddates = range_end_lastquater.subtract('month', 3);
 				var formated_todaytoyesterdaystart = todaytoyesterday.format('MMMM D, YYYY') ;
 				var formated_todaytoyesterdayend = enddates.format('MMMM D, YYYY') ;
 				var date_timePeriod = 'Last Quater';
 			
 			}
 			
 			if($('.ranges .active').text() != 'Custom Range'){
 				username= start.format('MMMM D, YYYY') +"~"+ end.format('MMMM D, YYYY') +"~"+ formated_todaytoyesterdaystart +"~"+ formated_todaytoyesterdayend;
 				
 				$('#daterange div').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY') +'</br>');
 				
 				startDate = start.format('YYYY-MM-DD'); 				
 				endDate=end.format('YYYY-MM-DD');
 				
 				compareStartDate= todaytoyesterday.format('YYYY-MM-DD');
 				
 				compareEndDate=enddates.format('YYYY-MM-DD');
 				timePeriod = date_timePeriod; 				
 			}
 		}else{
 				
 		   $('#daterange div').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY') +'</br>');
 		   $('#daterange p').html('');

 		}
     }  
		
  ); 				
    
    $('#custom_compare_div').hide();	
 
    var momentStartDate=moment().add('days', 1);
    var momentEndDate=moment(momentStartDate).add('month', 0).endOf('month');
		
	$('#daterange div').html(momentStartDate.format('MMMM D, YYYY') + ' - ' +  momentEndDate.format('MMMM D, YYYY'));
	
	$('#dateRangeStartDateText').hide();
	$('#dateRangeEndDateText').hide();
	
	startDate=momentStartDate.format('YYYY-MM-DD');    	 
	endDate=momentEndDate.format('YYYY-MM-DD');
	
 });