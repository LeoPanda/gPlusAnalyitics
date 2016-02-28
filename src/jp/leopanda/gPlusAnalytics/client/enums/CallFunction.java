package jp.leopanda.gPlusAnalytics.client.enums;

/**
 * GoogleGateを通してアクセスするGoogle+ APIの種類
 * 
 * @author LeoPanda
 *
 */
public enum CallFunction {
  GET_ITEM("データをロードします。"), INITIAL_LOAD("データストアを初期設定します。"), UPDATE("データストアを更新します。"), CLEAR(
      "データストアを消去します。");
  public String msg;

  private CallFunction(String msg) {
    this.msg = msg;
  }
}
