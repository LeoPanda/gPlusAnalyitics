package jp.leopanda.gPlusAnalytics.dataStore;

/**
 * データストア Activity エンティティ定義
 * @author LeoPanda
 *
 */
public enum Activity {
	KIND("Activity"),
	id("id"),
	actorId("actorId"),
	published("published"),
	numOfPlusOners("numOfPlusOners"),
	activiyItem("activityItem"),
	plusOnerItems("plusOnerItems");
	
	public String val;
	
	Activity(String val){
		this.val = val;
	}
}
