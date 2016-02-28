package jp.leopanda.gPlusAnalytics.client.enums;

/**
 * 　アクテビティの＋１ユーザー分布情報
 * 
 * @author LeoPanda
 *
 */
public enum Distribution {
  HIGH_LOOKER("30以上", 30), HIGH_MIDDLE_LOOKER("10-29", 10), LOW_MIDDLE_LOOKER("2-9", 2),
  FIRST_LOOKER("1", 1);

  public String name;
  public int threshold;

  /**
   * コンストラクタ
   */
  private Distribution(String name, int threshold) {
    this.name = name;
    this.threshold = threshold;
  }
}
