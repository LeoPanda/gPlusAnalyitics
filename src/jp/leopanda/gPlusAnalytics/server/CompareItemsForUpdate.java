package jp.leopanda.gPlusAnalytics.server;

import java.util.List;
import java.util.ListIterator;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * ソースアイテムリストを最新のアイテムリストと比較しアップデートする抽象クラス
 * 
 * @param <I>
 */
abstract class CompareItemsForUpdate<I extends PlusItem> {
  List<I> update(List<I> newItems, List<I> sourceItems) throws Exception {
    for (ListIterator<I> newIterator = newItems.listIterator(); newIterator.hasNext();) {
      I newItem = newIterator.next();
      sourceItems = updateItems(newItem, sourceItems);
      // 処理の終わったアイテムは対象リストから除外
      newIterator.remove();
    }
    return sourceItems;
  }

  private List<I> updateItems(I newItem, List<I> sourceItems) throws Exception {

    for (ListIterator<I> sourceIterator = sourceItems.listIterator(); sourceIterator.hasNext();) {
      I sourceItem = sourceIterator.next();
      if (newItem.getId().equals(sourceItem.getId())) {
        I modifiedItem = setItemForUpdate(newItem, sourceItem);
        if (modifiedItem != null) {
          sourceIterator.set(modifiedItem);
        }
        return sourceItems;
      }
    }
    sourceItems.add(setItemForNewAdd(newItem));
    return sourceItems;
  }

  abstract I setItemForNewAdd(I newItem) throws Exception;

  abstract I setItemForUpdate(I newItem, I sourceItem) throws Exception;
}