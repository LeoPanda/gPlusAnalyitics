package jp.leopanda.gPlusAnalytics.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * セルテーブルの表示データをフィルタリングするための抽象クラス
 * 
 * @author LeoPanda
 *
 */
public abstract class ItemFilter<I> {
  List<I> sourceItems;

  public ItemFilter(List<I> sourceItems) {
    this.sourceItems = sourceItems;
  }

  /**
   * ソースアイテムリストをフィルタして新しいインスタンスを返す
   * 
   * @param predicate フィルター条件
   * @return List<I> フィルター後のアイテムリスト
   */
  public List<I> doFilter(Predicate<I> predicate) {
    List<I> filterdItems = new ArrayList<I>();
    sourceItems.stream().filter(predicate).forEach(item -> filterdItems.add(item));
    return filterdItems;
  }

  /**
   * アイテムリストをソースアイテムリストにOR結合する
   * 
   * @param originalItems
   * @return
   */
  public List<I> combineOr(List<I> targetItems) {
    geFreshItems(targetItems).forEach(item -> sourceItems.add(item));
    return sourceItems;
  }

  /**
   * 対象リストからソースアイテムリストに存在しないアイテムを抽出する
   * 
   * @param comparsionItems 比較対象リスト
   * @return
   */
  private List<I> geFreshItems(List<I> comparsionItems) {
    List<I> fleshItems = new ArrayList<I>();
    comparsionItems.forEach(item -> {
      if (!sourceItems.contains(item)) {
        fleshItems.add(item);
      }
    });
    return fleshItems;
  }
}
