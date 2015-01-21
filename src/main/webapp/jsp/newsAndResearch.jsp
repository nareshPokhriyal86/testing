<!-- <div id="s4" style="display:none;"> -->
<div id="s4" >
						
						<!--news-->
                        
                        <div id="rssfeed" class="maintable_news" >
                        <div class="primary-content">
                        <div class="homenews">
					
					

<!-- start feedwind code -->

<div class="addoperation" id="a1" >
<h4>Lin Media News</h4>


<script type="text/javascript">

var loadNRflag = 0;
$(document).ready(function(){
	

	
$("#indus_new").click(getNews());
});

function getNews() { 	

	if (loadNRflag==0){
		loadNRflag = 1;
	
var wWidth =  $(window).width()-200;
if ($(window).width() >= 960){
wWidth = wWidth/3;
}else if($(window).width() <=767  && $(window).width() >480){

wWidth = wWidth/2;

}else{
wWidth = wWidth/2;

}

rssmikle_url="";
rssmikle_frame_width= wWidth;
rssmikle_frame_height="210";
rssmikle_target="_blank";
rssmikle_font="Arial, Helvetica, sans-serif";
rssmikle_font_size="12";
rssmikle_border="off";
rssmikle_css_url="";
rssmikle_title="off";
rssmikle_title_bgcolor="#0066FF";
rssmikle_title_color="#141111";
rssmikle_title_bgimage="http://";
rssmikle_item_bgcolor="";
rssmikle_item_bgimage="http://";
rssmikle_item_title_length="55";
rssmikle_item_title_color="#005ea6";
rssmikle_item_border_bottom="off";
rssmikle_item_description="on";
rssmikle_item_description_length="150";
rssmikle_item_description_color="#666666";
rssmikle_item_date="off";
rssmikle_item_description_tag="off";
rssmikle_item_podcast="icon";

$(function(){

var a = window;
a.rssmikle_frame_width = a.rssmikle_frame_width ? a.rssmikle_frame_width : 180;
a.rssmikle_frame_height = a.rssmikle_frame_height ? a.rssmikle_frame_height : 500;
a.rssmikle_item_description_tag = a.rssmikle_item_description_tag ? a.rssmikle_item_description_tag : '';

var twoDimensionalArray =[
['http://phx.corporate-ir.net/corporate.rss?c=131042&Rule=Cat=news~subcat=ALL','a1'],
['http://feeds.feedburner.com/ommalik','a2'],
['http://www.mobilecommercedaily.com/rss-feeds','a3'],
['http://madmobilenews.com','a4'],
['http://www.clickz.com/type/news','a5'],
['http://feeds.feedburner.com/dmnewshome','a6'],
['http://venturebeat.com/category/mobile','a7'],
['http://feeds.feedburner.com/MobileMarketingWatch','a8'],
['http://feeds.mediapost.com/mediadailynews','a9'],
['http://feeds.mediapost.com/online-media-daily','a10'],
['http://feeds2.feedburner.com/opinion/classic-guides','a11'],
['http://feeds2.feedburner.com/sectors/advertising-agencies','a12'],
['http://www.businessinsider.com','a13'],
['http://digg.com/search/?q=mobile%20marketing&format=rss','a14'],
['http://www.mobilenewscwp.co.uk/category/news/feed','a15'],
['http://www.clickz.com/category/analytics','a16'],
['http://www.clickz.com/category/media','a17'],
['http://techcrunch.com/rssfeeds/','a18'],
['http://techcrunch.com/rssfeeds/','a19'],
['http://feeds.adweek.com/adweek/all-news/','a20'],
['http://feeds.feedburner.com/AdOperationsOnline?format=xml','a21'],
['http://www.iab.net/rss','a22'],
['http://feeds.feedburner.com/AdOperationsOnline?format=xml','a23'],
['http://adage.com/rss-feed?section_id=350','a24'],
['http://www.digitaltrends.com/feed/','a25'],
['http://www.forbes.com/mobile/','a26'],
['http://www.digiday.com/feed/','a27'],
['http://feeds.mediapost.com/online-media-daily','a28'],
['http://feeds2.feedburner.com/news/ad-networks','a29'],
['http://www.digiday.com/feed/','a30'],
['http://www.businessweek.com/feeds/most-popular.rss','a31'],
['http://feeds2.feedburner.com/resources/case-studies','a32'],
['http://feeds2.feedburner.com/resources/case-studies','a33'],
['http://allthingsd.com/author/walt/feed/','a34'],
['http://www.clickz.com/category/marketing','a35'],
['http://www.clickz.com/category/marketing/data-driven-marketing','a36'],
['http://feeds.feedburner.com/ommalik','a37'],
];

for(var i = 0; i < twoDimensionalArray.length; i++) {
    var cube = twoDimensionalArray[i];
	var urlFinal = cube[0];
	var idFinal = cube[1];
    /*for(var j = 0; j < cube.length; j++) {
        //alert(( cube[j]));
    }*/

rssmikle_url = urlFinal;

var url = 'http://widget.feed.mikle.com/'
	+ '?rssmikle_url=' + (a.rssmikle_url ? encodeURIComponent(a.rssmikle_url) : '')
	+ '&rssmikle_type=' + (a.rssmikle_type ? a.rssmikle_type : '')
	+ '&rssmikle_frame_width=' + a.rssmikle_frame_width
	+ '&rssmikle_frame_height=' + a.rssmikle_frame_height
	+ '&rssmikle_frame_rico=' + (a.rssmikle_frame_rico ? a.rssmikle_frame_rico : '')
	+ '&rssmikle_target=' + (a.rssmikle_target ? a.rssmikle_target : '')
	+ '&rssmikle_font=' + (a.rssmikle_font ? a.rssmikle_font : '')
	+ '&rssmikle_font_size=' + (a.rssmikle_font_size ? a.rssmikle_font_size : '')
	+ '&rssmikle_border=' + (a.rssmikle_border ? a.rssmikle_border : '')
	+ '&rssmikle_css_url=' + (a.rssmikle_css_url ? encodeURIComponent(a.rssmikle_css_url) : '')
	+ '&rssmikle_title=' + (a.rssmikle_title ? a.rssmikle_title : '')
	+ '&rssmikle_title_bgcolor=' + (a.rssmikle_title_bgcolor ? encodeURIComponent(a.rssmikle_title_bgcolor) : '')
	+ '&rssmikle_title_color=' + (a.rssmikle_title_color ? encodeURIComponent(a.rssmikle_title_color) : '')
	+ '&rssmikle_title_bgimage=' + (a.rssmikle_title_bgimage ? encodeURIComponent(a.rssmikle_title_bgimage) : '')
	+ '&rssmikle_item_bgcolor=' + (a.rssmikle_item_bgcolor ? encodeURIComponent(a.rssmikle_item_bgcolor) : '')
	+ '&rssmikle_item_bgimage=' + (a.rssmikle_item_bgimage ? encodeURIComponent(a.rssmikle_item_bgimage) : '')
	+ '&rssmikle_item_title_length=' + (a.rssmikle_item_title_length ? a.rssmikle_item_title_length : '')
	+ '&rssmikle_item_title_color=' + (a.rssmikle_item_title_color ? encodeURIComponent(a.rssmikle_item_title_color) : '')
	+ '&rssmikle_item_border_bottom=' + (a.rssmikle_item_border_bottom ? a.rssmikle_item_border_bottom : '')
	+ '&rssmikle_item_description=' + (a.rssmikle_item_description ? a.rssmikle_item_description : '')
	+ '&rssmikle_item_description_length=' + (a.rssmikle_item_description_length ? a.rssmikle_item_description_length : '')
	+ '&rssmikle_item_description_color=' + (a.rssmikle_item_description_color ? encodeURIComponent(a.rssmikle_item_description_color) : '')
	+ '&rssmikle_item_description_tag=' + (a.rssmikle_item_description_tag ? a.rssmikle_item_description_tag : '')
        + '&rssmikle_item_date=' + (a.rssmikle_item_date ? a.rssmikle_item_date : '')
        + '&rssmikle_item_podcast=' + (a.rssmikle_item_podcast ? a.rssmikle_item_podcast : '');
//	+ '&rssmikle_ref=' + encodeURIComponent(document.URL);

if(a.rssmikle_border != 'off' && !a.rssmikle_css_url){
	a.rssmikle_frame_width = parseInt(a.rssmikle_frame_width) + 2;
	a.rssmikle_frame_height = parseInt(a.rssmikle_frame_height) + 2;
}

var scroll_flag = 'no';
if (a.rssmikle_item_description_tag == 'on_scrollbar'){
	scroll_flag = 'auto';
}

$("#"+idFinal).append('<iframe name="rssmikle_frame" width="' + a.rssmikle_frame_width + '" height="' + a.rssmikle_frame_height + '" frameborder="0" src="' + url + '" marginwidth="0" marginheight="0" vspace="0" hspace="0" scrolling="' + scroll_flag + '"></iframe>');


}

});
	
	}

}
</script>
</div>

<div class="addoperation" id="a2">
<h4>Technology News</h4>


</div>

<div class="addoperation" id="a3">
<h4>Mobile Commerce Daily</h4>






</div>

<div class="addoperation" id="a4">
<h4>Mad Mobile News</h4>



</div>

<div class="addoperation" id="a5">
<h4>Marketing News</h4>

</div>

<div class="addoperation" id="a6">
<h4>Direct Marketing News</h4>

</div>

<div class="addoperation" id="a7">
<h4>Venture Beat Technology News</h4>


</div>

<div class="addoperation" id="a8">
<h4>Mobile Marketing Watch</h4>

</div>


<div class="addoperation" id="a9">
<h4>MediaPost Media</h4>

</div>

<div class="addoperation" id="a10">
<h4>MediaPost Marketing</h4>

</div>

<div class="addoperation" id="a11">
<h4>Mobile Marketer Opinion</h4>

</div>

<div class="addoperation" id="a12">
<h4>Mobile Marketer Sectors</h4>


</div>

<div class="addoperation" id="a13">
<h4>Business Insider General Business News</h4>


</div>

<div class="addoperation" id="a14">
<h4>Digg.com News</h4>


</div>

<div class="addoperation" id="a15">
<h4>Mobile General News</h4>




</div>

<div class="addoperation" id="a16">
<h4>ClickZ Analytics</h4>




</div>

<div class="addoperation" id="a17">
<h4>ClickZ Media</h4>



</div>

<div class="addoperation" id="a18">
<h4>TechCrunch Mobile</h4>

</div>


</div>





</div>


                         <div class="secondary-content" >
                        <div class="homenews">
					
                    
                          <div class="addoperation" id="a20">
      <h4>AdWeek : All News</h4>
                 
     
                      
                      </div>
                          
                          <div class="addoperation" id="a21">
                          <h4>eMarketer Articles</h4>					
      
     
      
      </div>
      
    <div class="addoperation" id="a22">
      <h4>IAB News</h4>					
      
     
      </div>
      
   <div class="addoperation" id="a23">
                          <h4>Ad Operations Online</h4> 
                             
      
      </div>
      
  <div class="addoperation" id="a24">
                          <h4>General Advertising News</h4>
                         
      
                          </div>
                          
  <div class="addoperation" id="a25">
                          <h4>Mobile Digital Trends</h4>
      
      
                          </div>
                          
                     
 <div class="addoperation" id="a26">
                                              <h4>Forbes Mobile News</h4>
   
                          
                                              </div>
                                               
   <div class="addoperation" id="a27">
                                                                  <h4>Digiday General News</h4>
                         
      
                                                                  </div>                                                               
     
     <div class="addoperation" id="a28">
                                                                                              <h4>MediaPost Online</h4>
                       
                                                                                              </div> 
                                                                                                                                                           
    <div class="addoperation" id="a29">
                                                              <h4>Mobile Marketer News</h4>
  
  
                                                              </div> 
                                                              
        <div class="addoperation" id="a30">
                                                            <h4>Bloomberg BusinessWeek </h4>

                                                            </div>   
                                                            
   <div class="addoperation" id="a31">
                                                            <h4>Bloomberg BusinessWeek </h4>


                                                            </div>  
															<div class="addoperation" id="a32">
                                                            <h4>Mobile Marketer Resources</h4>


                                                            </div>                      
                                                                
<div class="addoperation" id="a33">
                                                            <h4>Mobile Marketer Resources</h4>



                                                            </div>     
              
 <div class="addoperation" id="a34">
  <h4>All Things D General News</h4>


 </div>  
 
 <div class="addoperation" id="a35">
  <h4>ClickZ Marketing</h4>




 </div>
 
 <div class="addoperation" id="a36">
  <h4>ClickZ Agency</h4>

 </div>
                                                               
  <div class="addoperation" id="a37">
  <h4>GigaOM Technology News</h4>


 </div>                                                            
                                                            
                                                                                    
				</div>
         </div>
                 
                        
                       
                         
                          <div class="theard-content">
                          
                          <!-------------------------------video call-->
                           <div style="width:300px!important; margin-left:8%;">
                          
                    <iframe  scrolling="no" width="300px!important" height="300"  frameborder="0" src="http://dev.lininteractive.com/flowplayer/d/index-new.php"></iframe>

</div>

 <!-----------------------------end--video call-->
 <div class="social">
 <div containersrc="clean.ascx" id="dnn_TopPane" style="width:100%; float:left;"><h3>Follow LIN MOBILE</h3>
<div id="dnn__ctl7_ContentPane">
<p></p><div style="padding: 10px"><a href="http://www.linkd.in/TNORSB" class="fm_button fm_linkedin" target="_blank"><span style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='http://static.hubspot.com/img/follow/icon_linkedin.png', sizingMethod='scale');  width: 32px; height: 32px; display:inline-block;cursor:pointer;  background-image:url('http://static.hubspot.com/img/follow/icon_linkedin.png'); _bac\kground-image:none;" title="Follow us on Linkedin"></span></a>

<a href="http://www.facebook.com/mobile.linmedia" class="fm_button fm_facebook" target="_blank"><span style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='http://static.hubspot.com/img/follow/icon_facebook.png', sizingMethod='scale');  width: 32px; height: 32px; display:inline-block;cursor:pointer;  background-image:url('http://static.hubspot.com/img/follow/icon_facebook.png'); _bac\kground-image:none;" title="Follow us on Facebook"></span></a>

<a href="http://www.twitter.com/linmobile" class="fm_button fm_twitter" target="_blank"><span style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='http://static.hubspot.com/img/follow/icon_twitter.png', sizingMethod='scale');  width: 32px; height: 32px; display:inline-block;cursor:pointer;  background-image:url('http://static.hubspot.com/img/follow/icon_twitter.png'); _bac\kground-image:none;" title="Follow us on Twitter"></span></a></div><p></p></div>
</div>
 
 
 </div>
 
 <div class="twitter">
 <h3> What's happening? </h3>
 <div class="twitter_box">
<a class="twitter-timeline" href="https://twitter.com/LIN_Mobile" data-widget-id="347313579973828610">Tweets by @LIN_Mobile</a>
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>





 
 </div>
 
 </div>
 
 
 <!--<div class="social">
 <div containersrc="clean.ascx" id="dnn_TopPane" style="width:100%; float:left;"><h3>Follow LIN MOBILE</h3>
<div id="dnn__ctl7_ContentPane">
<p></p><div style="padding: 10px"><a href="http://www.linkd.in/TNORSB" class="fm_button fm_linkedin" target="_blank"><span style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='http://static.hubspot.com/img/follow/icon_linkedin.png', sizingMethod='scale');  width: 32px; height: 32px; display:inline-block;cursor:pointer;  background-image:url('http://static.hubspot.com/img/follow/icon_linkedin.png'); _bac\kground-image:none;" title="Follow us on Linkedin"></span></a>

<a href="http://www.facebook.com/mobile.linmedia" class="fm_button fm_facebook" target="_blank"><span style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='http://static.hubspot.com/img/follow/icon_facebook.png', sizingMethod='scale');  width: 32px; height: 32px; display:inline-block;cursor:pointer;  background-image:url('http://static.hubspot.com/img/follow/icon_facebook.png'); _bac\kground-image:none;" title="Follow us on Facebook"></span></a>

<a href="http://www.twitter.com/linmobile" class="fm_button fm_twitter" target="_blank" style="width:100%;"><span style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='http://static.hubspot.com/img/follow/icon_twitter.png', sizingMethod='scale');  width: 32px; height: 32px; display:inline-block;cursor:pointer;  background-image:url('http://static.hubspot.com/img/follow/icon_twitter.png'); _bac\kground-image:none;" title="Follow us on Twitter"></span></a></div><p></p></div>
</div>
 
 
 </div>-->
                          
                          </div>
                


</div>






				</div>
				
				<script type="text/javascript">
var checkTwitterResize = 0;
function resizeTwitterWidget() {
if ($('#twitter-widget-0').length > 0) {
checkTwitterResize++;
if ($('#twitter-widget-0').attr('width') != '100%') checkTwitterResize = 0;
$('#twitter-widget-0').attr('width', '100%');
// Ensures it's checked at least 10 times (script runs after initial resize)
if (checkTwitterResize < 10) setTimeout('resizeTwitterWidget()', 50);
} else setTimeout('resizeTwitterWidget()', 1000);
}
resizeTwitterWidget();
</script>
				