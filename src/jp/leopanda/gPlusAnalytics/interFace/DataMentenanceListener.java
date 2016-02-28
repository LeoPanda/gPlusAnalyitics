package jp.leopanda.gPlusAnalytics.interFace;

/**
 * データストアメンテナンスRPC呼び出し用インターフェース
 * 
 * @author LeoPanda
 *
 */
public interface DataMentenanceListener {
  // 値が返された時の処理
  public void onCallback(String result);

}
