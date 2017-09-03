package jp.leopanda.gPlusAnalytics.dataStore;

/**
 * データストアエンティティ　共通プロパティ
 * @author LeoPanda
 *
 */
public abstract class CommonProperty {
  private String kind;
  public final String ACTOR_ID = "actorId";
  public final String PUBLISED = "published";
 
  public CommonProperty(String kind) {
    this.kind = kind;
  }
  
  /**
   * エンティティのkind名を取得する
   * @return
   */
  public String getKindName(){
    return kind;
  }

}
