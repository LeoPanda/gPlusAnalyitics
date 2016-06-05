package jp.leopanda.gPlusAnalytics.client.panel;

import jp.leopanda.gPlusAnalytics.client.panel.abstracts.FilterableItemListPanel;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.ItemFilter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * ユーザ別＋１フィルター機能付きアクテビティ一覧
 * 
 * @author LeoPanda
 *
 */
public class FilterableActivityListPanel extends
    FilterableItemListPanel<PlusActivity, ActivityTable> {


  /**
   * コンストラクタ
   * 
   * @param items 一覧に表示するデータのリスト
   * @param titleName 一覧表のタイトル
   * @param pageSize １ページの表示行数
   * @param itemTable 一覧表示する表オブジェクト
   */
  public FilterableActivityListPanel(String titleName, int pageSize, ActivityTable itemTable) {
    super(titleName, pageSize, itemTable);
  }

  /**
   * +1erでフィルター
   * 
   * @param plusOnerId フィルターした+1ユーザーのID
   * @param displayName フィルターした+1ユーザーの表示名称
   */
  public void doPlusOnerFilter(String plusOnerId, String displayName) {
    doFilter(plusOnerId, displayName, new ItemFilter<PlusActivity, String>() {
      @Override
      public boolean compare(PlusActivity item, String comparator) {
        return item.getPlusOnerIds().contains(comparator);
      }
    });
  }

}
