package jp.leopanda.gPlusAnalytics.server;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.interFace.NewItemSetter;
import jp.leopanda.gPlusAnalytics.interFace.ItemReplaceChecker;

/**
 * ソースアイテムリストに新規アイテムを適用するための抽象クラス
 * 
 * @param <I>
 */
abstract class NewItemsApplyer<I extends PlusItem> {
  List<I> newItems;
  List<I> sourceItems;
  LoggerWidhConter logger;

  public NewItemsApplyer(List<I> newItems, List<I> sourceItems, LoggerWidhConter logger) {
    this.newItems = newItems;
    this.sourceItems = sourceItems;
    this.logger = logger;
  }

  /**
   * ソースアイテムリストをアップデートする
   * 
   * @param itemForNewAdd 新規追加時のアイテムを設定する
   * @param itemCheker 既存アイテムを新規アイテムで置き換えるかどうかの判定ロジックを設定する nullの場合は新規追加のみ
   * @return
   * @throws Exception
   */
  List<I> apply(NewItemSetter<I> itemForNewAdd, ItemReplaceChecker<I> itemReplaceChecker)
      throws Exception {
    ListIterator<I> newIterator = newItems.listIterator();
    while (newIterator.hasNext()) {
      I newItem = newIterator.next();
      sourceItems = updateItems(newItem, itemForNewAdd, itemReplaceChecker);
      newIterator.remove();// CPU処理時間超過時の後続処理のため、完了した対象を逐次削除しておく。
    }
    return sourceItems;
  }

  /**
   * 最新アイテムがソースアイテムにあれば入れ替え、なければ新規に作成する
   * 
   * @param newItem 新規アイテム
   * @param newItemSetter 追加アイテムの内容をセットする
   * @param itemCheker 既存アイテムを置き換えるかどうかの判定ロジックを設定する
   * @return
   * @throws Exception
   */
  private List<I> updateItems(I newItem, NewItemSetter<I> newItemSetter,
      ItemReplaceChecker<I> itemReplaceChecker) throws Exception {
    //新規アイテムがある場合はソースに追加
    List<I> matchedItems = getMatchedItems(newItem);
    if (matchedItems.isEmpty()) {
      addNewItem(matchedItems, newItemSetter.setItem(newItem));
      return sourceItems;
    }
    //更新が必要ない場合は処理終了
    if (!Optional.ofNullable(itemReplaceChecker).isPresent()) return sourceItems;
    //更新処理
    if (isMatchedItemsNeedToReplace(matchedItems, newItem, itemReplaceChecker))
      replaceItem(matchedItems, newItemSetter.setItem(newItem));
    
    return sourceItems;
  }

  /**
   * ソースアイテムリストに新規アイテムがすでに存在しているなら、これを取り出す
   * 
   * @param newItem
   * @return
   */
  private List<I> getMatchedItems(I newItem) {
    return sourceItems.stream()
        .filter(sourceItem -> sourceItem.getId().equals(newItem.getId()))
        .collect(Collectors.toList());
  }

  /**
   * ソースアイテムリストに新規アイテムを追加する。
   * 
   * @param modfiedNewItem
   * @param matchedItems
   * @throws Exception
   */
  private void addNewItem(List<I> matchedItems, I modfiedNewItem) throws Exception {
    sourceItems.add(modfiedNewItem);
    logger.itemAdded(modfiedNewItem);
  }

  /**
   * 既存のアイテムに更新が必要かを判定する
   * 
   * @param newItem
   * @param itemChecker
   * @param matchedItems
   * @return
   */
  private boolean isMatchedItemsNeedToReplace(List<I> matchedItems, I newItem,
      ItemReplaceChecker<I> itemChecker) {
    return matchedItems.stream()
        .anyMatch(matchedItem -> itemChecker.replaceWhen(newItem, matchedItem));
  }

  /**
   * ソースアイテムリストの一致アイテムを新規アイテムで置き換える
   * 
   * @param newItem
   * @param itemForNewAdd
   * @param matchedItems
   * @throws Exception
   */
  private void replaceItem(List<I> matchedItems, I modfiedNewItem) throws Exception {
    sourceItems.removeAll(matchedItems);
    sourceItems.add(modfiedNewItem);
    logger.itemUpdated(modfiedNewItem);
  }

}