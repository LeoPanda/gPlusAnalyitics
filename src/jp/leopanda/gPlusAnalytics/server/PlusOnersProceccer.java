package jp.leopanda.gPlusAnalytics.server;

import java.util.List;
import java.util.ListIterator;

import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * ソースアイテムの+1erリストを操作する
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
   * ソース+1erアイテムリストの+1数を更新する ソースアクテビティに存在しなくなった+1erはアイテムリストから削除する
   * 
   * @param counter
   * @param sourceItems
   * @return
   */
  public List<PlusPeople> updatePlusOners(PlusOneCounter counter,
      List<PlusPeople> sourceItems) {
    for (ListIterator<PlusPeople> iterator = sourceItems.listIterator(); iterator.hasNext();) {
      PlusPeople plusOner = iterator.next();
      if (!counter.containsKey(plusOner.getId())) {
        logger.plusOnerDeleted();
        iterator.remove();
      } else {
        int numOfPlusOne = counter.get(plusOner.getId());
        if (numOfPlusOne == 0) {
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
   * 新規のPlusOnerをソースアイテムリストへ適用する
   * 
   * @throws Exception
   */
  public List<PlusPeople> addNewPlusOners(List<PlusPeople> newItems, List<PlusPeople> sourceItems)
      throws Exception {

    return new CompareItemsForUpdate<PlusPeople>() {

      @Override
      PlusPeople setItemForNewAdd(PlusPeople newItem) throws Exception {
        logger.plusOnerAdded();
        return newItem;
      }

      @Override
      PlusPeople setItemForUpdate(PlusPeople newItem, PlusPeople sourceItem) throws Exception {
        return null;
      }
    }.update(newItems, sourceItems);
  }

}
