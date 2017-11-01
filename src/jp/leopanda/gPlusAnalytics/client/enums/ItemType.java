package jp.leopanda.gPlusAnalytics.client.enums;

/**
 * 元データの種類
 * 
 * @author LeoPanda
 *
 */
public enum ItemType {
  ACTIVITIES("アクテビティ"), PLUSONERS("+1er");
  private String name;

  ItemType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
