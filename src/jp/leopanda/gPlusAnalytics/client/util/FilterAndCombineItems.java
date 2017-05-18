package jp.leopanda.gPlusAnalytics.client.util;

import java.util.ArrayList;
import java.util.List;

import jp.leopanda.gPlusAnalytics.client.panel.FilterLogCard;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * アイテムリストをフィルターし、かつ指定の論理結合を行う
 * 
 * @author LeoPanda
 *
 * @param <I>
 */
public abstract class FilterAndCombineItems<I extends PlusItem> {
  public List<I> doFilter(List<I> originalItems, List<I> sourceItems, FilterLogCard card) {
    List<I> filteredItems = new ArrayList<I>();
    switch (card.getBooleansValue()) {
    case AND:
      filteredItems = getFilterdItems(sourceItems, card);
      break;
    case OR:
      filteredItems = getFilterdItems(originalItems, card);
      filteredItems = logicalORItems(sourceItems, filteredItems);
      break;
    }
    return filteredItems;
  }

  /**
   * ２つのアイテムリストの論理和を返す
   * 
   * @param sourceItems
   * @param filteredItems
   * @return
   */
  List<I> logicalORItems(List<I> sourceItems, List<I> filteredItems) {
    List<I> pickUpedItems = new ArrayList<I>();
    for (I sourceItem : sourceItems) {
      boolean duplicated = false;
      for (I filteredItem : filteredItems) {
        if (filteredItem.equals(sourceItem)) {
          duplicated = true;
          break;
        }
      }
      if (duplicated) {
        duplicated = false;
      } else {
        pickUpedItems.add(sourceItem);
      }
    }

    for (I pickUpedItem : pickUpedItems) {
      filteredItems.add(pickUpedItem);
    }
    return filteredItems;
  }

  /**
   * アイテムのフィルターを実行する
   * 
   * @param sourceItems
   * @param card
   * @return
   */
  public abstract List<I> getFilterdItems(List<I> items, FilterLogCard card);
}