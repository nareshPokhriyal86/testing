
function getChannelPerformanceData(channelName,eCPM,CHG,percentageCHG,impressionsDelivered,clicks,CTR,payout){

		this.channelName=channelName;
		this.eCPM=eCPM;
		this.CHG=CHG;
		this.percentageCHG=percentageCHG;
		this.impressionsDelivered=impressionsDelivered;
		this.clicks=clicks;
		this.CTR=CTR;
		this.payout=payout;

		this.getChannelName=function(){
		return this.channelName;
		};

		this.setChannelName=function(channelName){
		this.channelName=channelName;
		};

		this.ECPM=function(){
		return this.eCPM;
		};

		this.setECPM=function(eCPM){
		this.eCPM=eCPM;
		};

		this.getcHG=function(){
		return this.CHG;
		};
		this.setcHG=function(CHG){
		this.CHG=CHG;
		};

		this.getPercentageCHG=function(){
		return this.percentageCHG;
		};

		this.setPercentageCHG=function(percentageCHG){
		this.percentageCHG=percentageCHG;
		};

		this.getImpressionsDelivered=function(){
		return this.impressionsDelivered;
		};

		this.setImpressionsDelivered=function(impressionsDelivered){
		this.impressionsDelivered=impressionsDelivered;
		};

		this.getClicks=function(){
		return this.clicks;
		};

		this.setClicks=function(clicks){
		this.clicks=clicks;
		};

		this.getcTR=function(){
		return this.CTR;
		};

		this.setcTR=function(CTR){
		this.CTR=CTR;
		};
		
		this.getPayout=function(){
		return this.payout;
		};

		this.setPayout=function(payout){
		this.payout=payout;
		};

}
