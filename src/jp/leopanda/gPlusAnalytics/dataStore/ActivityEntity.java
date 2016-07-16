package jp.leopanda.gPlusAnalytics.dataStore;

/**
 * データストア Activity エンティティ定義
 * 
 * @author LeoPanda
 *
 */
public enum ActivityEntity {
  KIND("Activity"), ID("id"), ACTOR_ID("actorId"), PUBLISHED("published"), UPDATED("updated"),
  NUM_OF_PLUSONERS("numOfPlusOners"), ACTIVITY_ITEM("activityItem"),
  PLUSONER_ITEMS("plusOnerItems");

  public String val;

  ActivityEntity(String val) {
    this.val = val;
  }
}
