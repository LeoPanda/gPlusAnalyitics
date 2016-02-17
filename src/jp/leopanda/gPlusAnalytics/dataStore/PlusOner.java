package jp.leopanda.gPlusAnalytics.dataStore;

/**
 * データストア PlusOner エンティティ定義
 * @author LeoPanda
 *
 */
public enum PlusOner {
	KIND("PlusOner"),
	actorId("actorId"),
	title("chartTitle"),
	id("id"),
	url("url"),
	displayName("displayName"),
	imageUrl("imageUrl"),
	numOfPlusOne("numOfPlusOne");
	
	public String val;
	
	PlusOner(String val){
		this.val = val;
	}
	
}
