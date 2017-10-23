package jp.leopanda.gPlusAnalytics.server;

import java.util.List;
import java.util.ListIterator;

import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * ソース+1erリストの処理クラス
 * 
 * @author LeoPanda
 *
 */
public class PlusOnersProceccer {
  LoggerWidhConter logger;

  /**
   * 
   */
  public PlusOnersProceccer(LoggerWidhConter logger) {
    this.logger = logger;
  }

  /**
   * ソース+1erアイテムリストの+1数を更新する
   * ソースアクテビティに存在しなくなった+1erはアイテムリストから削除する
   * 
   * @param summrizer
   * @param sourceItems
   * @return
   */
  public List<PlusPeople> updatePlusOners(SummrizerByPlusOners summrizer,
      List<PlusPeople> sourceItems) {
    ListIterator<PlusPeople> iterator = sourceItems.listIterator();
    while (iterator.hasNext()) {
      PlusPeople plusOner = iterator.next();
      if (!summrizer.containsKey(plusOner.getId())) {// アクテビティに存在しなくなった+1erは削除
        logger.plusOnerDeleted();
        iterator.remove();
      } else {
        int numOfPlusOne = summrizer.get(plusOner.getId());
        if (numOfPlusOne == 0) {// +1数が0になった+1erは削除
          logger.plusOnerDeleted();
          iterator.remove();
        } else {
          plusOner.setNumOfPlusOne(numOfPlusOne);
          iterator.set(plusOner);
        }
      }
    }
    return sourceItems;
  }

  /**
   * 新規のPlusOnerをソースアイテムリストへ追加する
   * 
   * @throws Exception
   */
  public List<PlusPeople> addNewPlusOners(List<PlusPeople> newItems, List<PlusPeople> sourceItems)
      throws Exception {
    return new NewItemsApplyer<PlusPeople>(newItems, sourceItems,logger) {}
        .apply(newItem -> newItem, null);
  }

}
