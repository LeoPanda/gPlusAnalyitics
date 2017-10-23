package jp.leopanda.gPlusAnalytics.client.enums;

/**
 * GoogleGateを通してアクセスするGoogle+ APIの種類
 * 
 * @author LeoPanda
 *
 */
public enum CallFunction {
  GET_STOREDITEMS("データをロードします。"), UPDATE＿ITEMSTORE("データストアを更新します。"),
  CLEAR_SERVERMEMORY("サーバーのキャッシュメモリーをクリアします。");
  public String msg;

  private CallFunction(String msg) {
    this.msg = msg;
  }
}
