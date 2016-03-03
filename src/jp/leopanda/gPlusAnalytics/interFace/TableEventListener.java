package jp.leopanda.gPlusAnalytics.interFace;



/**
 * テーブルオブジェクトのイベントを通知する
 * 
 * @author LeoPanda
 *
 */
public interface TableEventListener {
  /**
   * フィルター機能が使用された
   * 
   * @param filterLog 表のフィルター履歴
   */
  public void onFilter(String filterLog);
}
