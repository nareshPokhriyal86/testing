<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script src="../js/include/jquery.min.js?v=<s:property value='deploymentVersion'/>"></script>
<link rel="stylesheet" type="text/css" media="all" href="../css/daterangepicker.css?v=<s:property value='deploymentVersion'/>" /> 
<script type="text/javascript" src="../js/moment.js?v=<s:property value='deploymentVersion'/>"></script>
<script type="text/javascript" src="../js/daterangepicker.js?v=<s:property value='deploymentVersion'/>"></script>





	  <script type="text/javascript">
	  if (startDate == undefined){
		  var startDate ;
		  var endDate;
		  var compareStartDate;
		  var compareEndDate ;
		  var timePeriod;
		  var publisherViewPage = '<s:property value="publisherViewPageName"/>';
		  var advertiserViewPage = '<s:property value="advertiserViewPageName"/>';
		  var operationalViewPage = '<s:property value="operationalViewPageName"/>';
		  var pageName = '<s:property value="#session.sessionDTO.pageName"/>';
		  
		  }
	  
               $(document).ready(function() {
            	      //defaultGetDate = getCookie("username")
            	       //defaultGetDateSplit = defaultGetDate.split("~");
            	       //calenderStartDate = defaultGetDateSplit[0]
            	       //calenderEndDate = defaultGetDateSplit[1]
            	       //alert(calenderStartDate)
            	       //alert(calenderEndDate)
            	       //alert(moment().subtract('days', 6))
            	       //format = calenderStartDate.format('ddd MMM D YYYY h:mm:ss Z')
					   //alert(moment().startOf('year').add('month', 3));
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
						//alert("range_start_lastquater"+ moment().startOf('year').add('month', 3));
						//alert("range_end_lastquater"+range_end_lastquater);
                  $('#reportrange').daterangepicker(
						
                     {
                        ranges: {
                           //'Today': [new Date(), new Date()],
                           'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
                           'Last 7 Days': [moment().subtract('days', 6), new Date()],
                           'Last 30 Days': [moment().subtract('days', 29), new Date()],
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
                        startDate: moment().subtract('days', 6),
                        endDate: new Date(),
                        minDate: '01/01/2012',
                        maxDate: new Date(),
                        locale: {
                            applyLabel: 'Apply',
                            fromLabel: 'From',
                            toLabel: 'To',
                            customRangeLabel: 'Custom Range',
                            daysOfWeek: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr','Sa'],
                            monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
                            firstDay: 1
                        },
                        showWeekNumbers: true,
                        buttonClasses: [''],
                        dateLimit: false
                     },
					 
                     function(start, end) {
                    	 try{
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
       								setCookie("username",username,1);
       							 $('#reportrange div').html(user_startdate + ' - ' +user_enddate +'</br>');
       							 $('#reportrange p').html('Compared to:' + formated_todaytoyesterdaystart + ' - ' + formated_todaytoyesterdayend);
       							 	
       							 	compareStartDate= todaytoyesterday.format('YYYY-MM-DD');
       								compareEndDate=enddates.format('YYYY-MM-DD');
       								timePeriod = user_startdate + ' - ' +user_enddate ;

       								if(pageName == advertiserViewPage){
       									if((ordername!=null && ordername!="") || (lineItemArr!=null && lineItemArr!="")){
       										isTrendDefault = false;
       									}
       									loadAllDataAdvertiser();
       								}else if (pageName == publisherViewPage){
       									loadAllDataPublisher();
       								}else if (pageName == operationalViewPage){
       									getAllReconciliationData();
       								}

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
       				           	   	  //alert("dsdaasd"+username)
       								setCookie("username",username,1);
       								
       								$('#reportrange div').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY') +'</br>');
       								$('#reportrange p').html('Compared to:' + formated_todaytoyesterdaystart + ' - ' + formated_todaytoyesterdayend);
       								//alert("start" + start.format('MMMM D, YYYY'));
       								//alert("end" + end.format('MMMM D, YYYY'));
       								//alert("formated_todaytoyesterdaystart" +formated_todaytoyesterdaystart);
       								//alert("formated_todaytoyesterdayend" +formated_todaytoyesterdayend);
       								
       								startDate = start.format('YYYY-MM-DD');
       								
       								endDate=end.format('YYYY-MM-DD');
       								
       								compareStartDate= todaytoyesterday.format('YYYY-MM-DD');
       								
       								compareEndDate=enddates.format('YYYY-MM-DD');
       								timePeriod = date_timePeriod;
                                       if(pageName == advertiserViewPage) {
       									if((ordername!=null && ordername!="") || (lineItemArr!=null && lineItemArr!="")){
       										isTrendDefault = false;
       									}
       									loadAllDataAdvertiser();
       								}else if(pageName == publisherViewPage){
       									loadAllDataPublisher();
       								}else if(pageName == operationalViewPage){
       									getAllReconciliationData();
       								}
       							}
       						}else{
       				
       		                    $('#reportrange div').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY') +'</br>');
       							$('#reportrange p').html('');

       						}
                            }catch(error){
                            	//alert(error);
                            }
                    	 }
                    	 
                  );
				  
                  //Set the initial state of the picker label
                  
                  function getCookie(c_name)
					{
					var c_value = document.cookie;
					var c_start = c_value.indexOf(" " + c_name + "=");
					if (c_start == -1)
					{
					c_start = c_value.indexOf(c_name + "=");
					}
					if (c_start == -1)
					{
					c_value = null;
					}
					else
					{
					c_start = c_value.indexOf("=", c_start) + 1;
					var c_end = c_value.indexOf(";", c_start);
					if (c_end == -1)
					{
					c_end = c_value.length;
					}
					c_value = unescape(c_value.substring(c_start,c_end));
					}
					return c_value;
					}

		    	   function setCookie(c_name,value,exdays)
					{
						var exdate=new Date();
						exdate.setDate(exdate.getDate() + exdays);
						var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
						document.cookie=c_name + "=" + c_value;
					}
					
					
			 		//alert(startDate); 
	           		var username=getCookie("username");
	           	  	
	           		if (username!=null && username!="")
		           	  {
	           			
		           	  
		           	 splitedUserDate= username.split("~")
		           	 //alert("final"+username)
		           	 startDate1 = splitedUserDate[0];
		           	 //alert("startDate1"+startDate1);
		           	 startDate2 = splitedUserDate[1];
		           	 compareDate1 = splitedUserDate[2];
		           	 compareDate2 = splitedUserDate[3];
		           	var today = new Date(startDate1)
		           	var end = new Date(startDate2)
		           	var compareStart = new Date(compareDate1);
		           	var compareEnd = new Date(compareDate2);
		           	var startMonth = ("0" + (today.getMonth()+1)).slice(-2);
		           	var endMonth = ("0" + (end.getMonth()+1)).slice(-2);
		           	var startDay = ("0" + (today.getDate())).slice(-2);
		           	var endDay =  ("0" + (end.getDate())).slice(-2);	
		           	var compareStartMonth = ("0" + (compareStart.getMonth()+1)).slice(-2);
		           	var compareEndMonth = ("0" + (compareEnd.getMonth()+1)).slice(-2);
		           	var compareStartDay= ("0" + (compareStart.getDate())).slice(-2);
		           	var compareEndDay = ("0" + (compareEnd.getDate())).slice(-2);
		           	startDate = today.getFullYear()+"-"+startMonth+"-"+startDay 
		           	endDate = end.getFullYear()+"-"+endMonth+"-"+endDay
		           compareStartDate = compareStart.getFullYear()+"-"+compareStartMonth+"-"+compareStartDay 
		           	compareEndDate = compareEnd.getFullYear()+"-"+compareEndMonth+"-"+compareEndDay
		           	 //startDate = startDate1.format('YYYY-MM-DD');
		           	 //endDate = startDate2.format('YYYY-MM-DD');
		           	  //var startDate1= 
		           		$('#reportrange div').html(startDate1 + ' - ' + startDate2);
                  		$('#reportrange p').html('Compared to:' + compareDate1 + ' - ' + compareDate2);
		           	  }
		           	else 
		           	  {
		           		
		           	  		//alert(moment().subtract('days', 6).format('MMMM D, YYYY'))
		           			startDateCookie = moment().subtract('days', 6).format('MMMM D, YYYY')
							endDateCookie = moment().format('MMMM D, YYYY')	;	
							compareStartDateCookie = moment().subtract('days', 13).format('MMMM D, YYYY');
							compareEndDateCookie = moment().subtract('days', 7).format('YYYY-MM-DD');
							
							startDate = moment().subtract('days', 6).format('YYYY-MM-DD')
							
							endDate =  moment().format('YYYY-MM-DD')
							$('#reportrange div').html(moment().subtract('days', 6).format('MMMM D, YYYY') + ' - ' + moment().format('MMMM D, YYYY'));
		                  	$('#reportrange p').html('Compared to:' + moment().subtract('days', 13).format('MMMM D, YYYY') + ' - ' + moment().subtract('days', 7).format('MMMM D, YYYY'));
							username= startDateCookie +"~"+ endDateCookie +"~"+ compareStartDateCookie +"~"+ compareEndDateCookie;
		           	   	  
							setCookie("username",username,1);
		           	   	  	
		           	    
		           	  }
	           		
	           
                  
               });
               </script>
               
               <!-- <div class="well" style="float:right;margin-top: 10px;"> -->
 				<div class="well" style="float:right;margin-top: 3px;">
                <div id="reportrange" class="pull-right" style="background: #fff; cursor: pointer; padding: 0px 3px; border: 1px solid #ccc">
                  <i class="icon-calendar icon-large" style="float:left;"></i>
                  <div style="font-size:15px;font-weight:bold; margin-left: 27px;margin-right: 12px;margin-bottom: 6px;"></div><p style="float:left;font-size:14px;margin:0px;"></p> <b class="caret" style="margin-top: -15px;float:right;margin-left:5px;"></b>
               </div>
            </div>