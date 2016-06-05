package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.panel.abstracts.FilterableItemListPanel;
import jp.leopanda.gPlusAnalytics.client.panel.abstracts.ItemFilter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * アクティビティ別+1フィルター機能付き＋１er一覧
 * @author LeoPanda
 *
 */
public class FilterablePlusOnerListPanel extends FilterableItemListPanel<PlusPeople, PlusOnersTable> {

  /**
   * コンストラクタ
   * @param items 一覧に表示するデータのリスト
   * @param titleName 一覧表のタイトル
   * @param pageSize １ページの表示行数
   * @param itemTable 一覧表示する表オブジェクト
   */
  public FilterablePlusOnerListPanel(String titleName, int pageSize,
      PlusOnersTable itemTable) {
    super(titleName, pageSize, itemTable);
  }

  /**
   * アクティビティに＋１した+1erでフィルター
   * @param plusOnerIds
   * @param displayName
   */
  public void doActivityFilter(List<String> plusOnerIds,String displayName){
    doFilter(plusOnerIds, displayName, new ItemFilter<PlusPeople, List<String>>() {
      @Override
      public boolean compare(PlusPeople item, List<String> comparator) {
        return comparator.contains(item.getId());
      }
  });
  
}
}
