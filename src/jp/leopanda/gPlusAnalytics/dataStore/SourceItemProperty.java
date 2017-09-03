package jp.leopanda.gPlusAnalytics.dataStore;

/**
 * ソースアイテムエンティティ用プロパティ定義
 * @author LeoPanda
 *
 */
public class SourceItemProperty extends CommonProperty {

  /**
   * コンストラクタでKind名を設定
   */
  public SourceItemProperty(String kind) {
    super(kind);
  }
  public final String SEQUENCE = "sequence";
  public final String ITEMS = "items";  
  
}
