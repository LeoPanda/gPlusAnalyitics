package jp.leopanda.gPlusAnalytics.dataStore;

/**
 * データストア Activity エンティティ定義
 * @author LeoPanda
 *
 */
public enum Activity {
	KIND("Activity"),
	title("title"),
	id("id"),
	actorId("actorId"),
	url("url"),
	published("published"),
	updated("updated"),
	itemObjectContent("itemObjectContent"),
	attachmentImageUrls("attachmentImageUrls"),
	numOfPlusOners("numOfPlusOners"),
	plusOnerIds("plusOnerIds"),
	firstLookers("firstLookers"),
	lowMiddleLookers("lowMiddleLookers"),
	highMiddleLookers("highMiddleLookers"),
	highLookers("highLookers"),
	accessDescription("accessDescription");
	
	public String val;
	
	Activity(String val){
		this.val = val;
	}
}
