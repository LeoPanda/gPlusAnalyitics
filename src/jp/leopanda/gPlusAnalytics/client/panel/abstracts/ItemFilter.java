package jp.leopanda.gPlusAnalytics.client.panel.abstracts;

import java.util.ArrayList;
import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * セルテーブルの表示データをフィルタリングするための抽象クラス
 * 
 * @author LeoPanda
 *
 */
public abstract class ItemFilter<I extends PlusItem, C> {
  List<I> originalItems; // フィルター実行前のオリジナルデータ

  /**
   * フィルタを実行してセルテーブルの表示データを変更する
   * 
   * @param comparator 　フィルタ比較オブジェクト
   * @param itemTable 表示データを変更したいセルテーブル
   */
  public void doFilter(C comparator, SimpleCellTable<I> itemTable) {
    List<I> displayList = itemTable.getDisplayList();
    this.originalItems = new ArrayList<I>(displayList);
    displayList.clear();
    for (I item : originalItems) {
      if (comparator == null) {
        displayList.add(item);
      } else if (compare(item, comparator)) {
        displayList.add(item);
      }
    }
    itemTable.setPageStart(0);
    this.originalItems.clear();
  }

  /**
   * フィルタリング方法を記述する
   * 
   * @param item フィルタ対象のアイテムオブジェクト
   * @param comparator 比較フィールドオブジェクト
   * @return 比較結果
   */
  public abstract boolean compare(I item, C comparator);
}
