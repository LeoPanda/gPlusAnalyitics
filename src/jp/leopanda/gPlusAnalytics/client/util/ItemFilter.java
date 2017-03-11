package jp.leopanda.gPlusAnalytics.client.util;

import java.util.ArrayList;
import java.util.List;

/**
 * セルテーブルの表示データをフィルタリングするための抽象クラス
 * 
 * @author LeoPanda
 *
 */
public abstract class ItemFilter<I, C> {
  /**
   * リストデータをフィルタリングする
   * 
   * @param comparator C
   *          フィルタ比較オブジェクト
   * @param sourceItems List<I>
   *          表示データを変更したいアイテムリスト
   * @return List<I> フィルター後のアイテムリスト
   */
  public List<I> doFilter(List<I> sourceItems,C comparator) {
    if(comparator == null){
      return sourceItems;
    }
    List<I> filterdItems = new ArrayList<I>();
    for (I sourceItem : sourceItems) {
      if (compare(sourceItem, comparator)) {
        filterdItems.add(sourceItem);
      }
    }
    return filterdItems;
  }

  /**
   * フィルタリング方法を記述する
   * 
   * @param sourceItem
   *          フィルタ対象のアイテムオブジェクト
   * @param comparator
   *          比較フィールドオブジェクト
   * @return 比較結果
   */
  public abstract boolean compare(I sourceItem, C comparator);
}
