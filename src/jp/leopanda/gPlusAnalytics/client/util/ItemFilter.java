package jp.leopanda.gPlusAnalytics.client.util;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * セルテーブルの表示データをフィルタリングするための抽象クラス
 * 
 * @author LeoPanda
 *
 */
public class ItemFilter<I> {
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
    return sourceItems.stream().filter(predicate).collect(Collectors.toList());
  }

  /**
   * アイテムリストをソースアイテムリストにOR結合する
   * 
   * @param originalItems
   * @return
   */
  public List<I> combineOr(List<I> targetItems) {
    sourceItems.addAll(getFreshItems(targetItems));
    return sourceItems;
  }

  /**
   * 対象リストからソースアイテムリストに存在しないアイテムを抽出する
   * 
   * @param comparsionItems 比較対象リスト
   * @return
   */
  private List<I> getFreshItems(List<I> comparsionItems) {
    return comparsionItems.stream().filter(item -> !sourceItems.contains(item))
        .collect(Collectors.toList());
  }

  /**
   * +1数でフィルタ
   * @param keyword
   * @param getter
   * @return
   */
  protected List<I> doFilterByNumOfPlusOne(NumOfPlusOneFilterKeyword keyword,
      Function<I, Integer> getter) {
    return doFilter(item -> {
      boolean checker = false;
      switch (keyword.getComparator()) {
        case EQ:
          checker = keyword.getNumOfPlusOne() == getter.apply(item);
          break;
        case LT:
          checker = keyword.getNumOfPlusOne() > getter.apply(item);
          break;
        case GT:
          checker = keyword.getNumOfPlusOne() < getter.apply(item);
          break;
      }
      return checker;
    });
  }

}
