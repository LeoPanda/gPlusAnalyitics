package jp.leopanda.gPlusAnalytics.interFace;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * イベント発生時のアイテムを通知する
 * 
 * @author LeoPanda
 *
 */
public interface ItemEventListener<I extends PlusItem> {
  /**
   *　イベントが発生した
   * @param item イベント発生時の対象アイテム
   */
  public void onEvent(I item);
}
