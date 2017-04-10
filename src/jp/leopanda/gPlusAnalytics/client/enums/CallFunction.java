package jp.leopanda.gPlusAnalytics.client.enums;

/**
 * GoogleGateを通してアクセスするGoogle+ APIの種類
 * 
 * @author LeoPanda
 *
 */
public enum CallFunction {
  GET_STOREDITEMS("データをロードします。"), INITIAL_ITEMSTORE("データストアを初期設定します。"),
  UPDATE＿ITEMSTORE("データストアを更新します。"), UPDATE_ACTIVITIES("アクテビティを再設定します。"),CLEAR＿ITEMSTORE(
      "データストアを消去します。");
  public String msg;

  private CallFunction(String msg) {
    this.msg = msg;
  }
}
