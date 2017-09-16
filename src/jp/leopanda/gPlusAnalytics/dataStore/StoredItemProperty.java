package jp.leopanda.gPlusAnalytics.dataStore;

/**
 * データストアエンティティ用プロパティ定義
 * 
 * @author LeoPanda
 *
 */
public class StoredItemProperty {

  private String kind;
  public final String ACTOR_ID = "actorId";
  public final String PUBLISED = "published";

  public final String SEQUENCE = "sequence";
  public final String ITEMS = "items";

  /**
   * コンストラクタでKind名を設定
   */
  public StoredItemProperty(String kind) {
    this.kind = kind;
  }

  public String getKindName() {
    return kind;
  }

}
