package jp.leopanda.gPlusAnalytics.interFace;

import jp.leopanda.gPlusAnalytics.client.enums.FilterType;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * アイテムのクリックイベントを通知する
 * 
 * @author LeoPanda
 *
 */
public interface ItemClickListener<I extends PlusItem> {
  /**
   *　クリックイベントが発生した
   * @param item イベント発生時の対象アイテム
   */
  public void onClick(I item,FilterType filterType);
}
