package jp.leopanda.gPlusAnalytics.server;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.interFace.ItemForNewAdd;
import jp.leopanda.gPlusAnalytics.interFace.ItemChecker;

/**
 * ソースアイテムリストに新規アイテムを適用するための抽象クラス
 * 
 * @param <I>
 */
abstract class NewItemsApplyer<I extends PlusItem> {
  List<I> newItems;
  List<I> sourceItems;

  public NewItemsApplyer(List<I> newItems, List<I> sourceItems) {
    this.newItems = newItems;
    this.sourceItems = sourceItems;
  }

  /**
   * ソースアイテムリストをアップデートする
   * 
   * @param itemForNewAdd 新規追加時のアイテムを設定する
   * @param itemCheker 既存アイテムを新規アイテムで置き換えるかどうかの判定ロジックを設定する
   * @return
   * @throws Exception
   */
  List<I> apply(ItemForNewAdd<I> itemForNewAdd, ItemChecker<I> itemChecker) throws Exception {
    ListIterator<I> newIterator = newItems.listIterator();
    while (newIterator.hasNext()) {
      I newItem = newIterator.next();
      sourceItems = updateItems(newItem, itemForNewAdd, itemChecker);
      newIterator.remove();//CPU処理時間超過時の後続処理のため、完了した対象を逐次削除しておく。
    }
    return sourceItems;
  }

  /**
   * 最新アイテムがソースアイテムにあれば入れ替え、なければ新規に作成する
   * 
   * @param newItem 新規アイテム
   * @param itemForNewAdd 追加アイテムを設定する
   * @param itemCheker 既存アイテムを置き換えるかどうかの判定ロジックを設定する
   * @return
   * @throws Exception
   */
  private List<I> updateItems(I newItem, ItemForNewAdd<I> itemForNewAdd,
      ItemChecker<I> itemChecker) throws Exception {
    List<I> matchedItems = getMatchedItems(newItem);
    if (matchedItems.isEmpty()) {
      sourceItems.add(itemForNewAdd.setItem(newItem));
    } else if (isMatchedItemsNeedToReplace(newItem, itemChecker, matchedItems)) {
      sourceItems.removeAll(matchedItems);
      sourceItems.add(itemForNewAdd.setItem(newItem));
    }
    return sourceItems;
  }

  
  /**
   * 既存のアイテムが存在する場合はこれを抽出する
   * @param newItem
   * @return
   */
  private List<I> getMatchedItems(I newItem) {
    return sourceItems.stream()
        .filter(sourceItem -> sourceItem.getId().equals(newItem.getId()))
        .collect(Collectors.toList());
  }
  /**
   * 既存のアイテムに更新が必要かを判定する
   * @param newItem
   * @param itemChecker
   * @param matchedItems
   * @return
   */
  private boolean isMatchedItemsNeedToReplace(I newItem, ItemChecker<I> itemChecker,
      List<I> matchedItems) {
    return matchedItems.stream()
        .anyMatch(matchedItem -> itemChecker.replaceWhen(newItem, matchedItem));
  }

}