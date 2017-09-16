package jp.leopanda.gPlusAnalytics.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.dataStore.DataStoreHandler;

/**
 * ソースアクテビティリストの処理クラス
 * 
 * @author LeoPanda
 *
 */
public class ActivitiesProcesser {
  LoggerWidhConter logger;
  PlusApiService apiService;
  List<PlusPeople> plusOnersForUpdate; // 変更のあったActivityに+1したユーザーのリスト

  ActivitiesProcesser(PlusApiService apiService, LoggerWidhConter logger) {
    this.apiService = apiService;
    this.logger = logger;
    plusOnersForUpdate = new ArrayList<PlusPeople>();
  }

  /**
   * 変更のあったActivityに＋１したユーザーのリストを取得する
   * リストはupdatePlusActivitiesAndStackPlusOners()で更新されるので先行してcallしておくこと。
   * 
   * @return
   */
  public List<PlusPeople> getPlusOnersForUpdate() {
    logger.setnumOfPlusOnerUpdated(plusOnersForUpdate.size());
    return this.plusOnersForUpdate;
  }

  /**
   * 変更のあったアクテビティを更新し、処理対象の+1erリストを抽出する 処理の終わったnewActivitiesはリストから削除される
   * 
   * @param newActivities
   *          最新のアクテビティリスト
   * @param sourceActivities
   *          ソースアクテビティリスト
   * @return
   * @throws Exception
   */
  public List<PlusActivity> updatePlusActivitiesAndStackPlusOners(List<PlusActivity> newActivities,
      List<PlusActivity> sourceActivities) throws Exception {
    logger.setnumOfNewActivities(newActivities.size());
    return new CompareItemsForUpdate<PlusActivity>() {
      @Override
      PlusActivity setItemForNewAdd(PlusActivity newItem) throws Exception {
        newItem.setPlusOnerIds(
            stackPlusOnersForUpdate(apiService.getPlusOnersByActivity(newItem.getId())));
        logger.activitiesAdded();
        return newItem;
      }

      @Override
      PlusActivity setItemForUpdate(PlusActivity newItem, PlusActivity sourceItem)
          throws Exception {
        if (sourceItem.getNumOfPlusOners().equals(newItem.getNumOfPlusOners())) {
          return null; // 更新しない
        }
        logger.activitiesUpdated();
        return setItemForNewAdd(newItem); // 新規アイテムで置き換え
      }
    }.update(newActivities, sourceActivities);
  }

  /**
   * 更新対象のPlusOnerを更新リストにスタックする。
   * 
   * @param onersInActivity
   *          APIコールで得たactivity毎の＋１ユーザーリスト
   * @return Activityに+1したユーザーのIDリスト
   */
  private List<String> stackPlusOnersForUpdate(List<PlusPeople> onersInActivity) {
    List<String> plusOnerIds = new ArrayList<String>();
    for (PlusPeople onerInActivity : onersInActivity) {
      String plusOnerId = onerInActivity.getId();
      plusOnerIds.add(plusOnerId);
      if (!idIsExistIn(this.plusOnersForUpdate, plusOnerId)) {
        this.plusOnersForUpdate.add(onerInActivity);
      }
    }
    return plusOnerIds;
  }

  /**
   * ソースアクテビティリストの統計情報をセットする
   * 
   * @param plusOneCounter
   * @param sourceItems
   * @return
   */
  public List<PlusActivity> setStatisticsInfo(PlusOneCounter plusOneCounter,
      List<PlusActivity> sourceItems) {
    for (ListIterator<PlusActivity> iterator = sourceItems.listIterator(); iterator.hasNext();) {
      PlusActivity activity = iterator.next();
      iterator.set(setActivityStastics(plusOneCounter, activity));
    }
    return sourceItems;
  }

  /**
   * アクテビティ単体の統計情報をセットする
   * 
   * @param plusOneCounter
   * @param activity
   * @return
   */
  private PlusActivity setActivityStastics(PlusOneCounter plusOneCounter, PlusActivity activity) {

    activity.highLookers = 0;
    activity.highMiddleLookers = 0;
    activity.lowMiddleLookers = 0;
    activity.firstLookers = 0;

    for (String plusOnerId : activity.plusOnerIds) {
      int numOfPlusOne = plusOneCounter.get(plusOnerId);
      if (numOfPlusOne > Distribution.HIGH_LOOKER.threshold) {
        activity.highLookers++;
      } else if (numOfPlusOne > Distribution.HIGH_MIDDLE_LOOKER.threshold) {
        activity.highMiddleLookers++;
      } else if (numOfPlusOne > Distribution.LOW_MIDDLE_LOOKER.threshold) {
        activity.lowMiddleLookers++;
      } else {
        activity.firstLookers++;
      }
    }
    logger.countUpPlusOne(activity.highLookers + activity.highMiddleLookers
        + activity.lowMiddleLookers + activity.firstLookers);
    logger.countUpPlusOners(activity.getNumOfPlusOners());
    return activity;
  }

  /**
   * 最新アクテビティアイテムリストにないアイテムをソースアイテムリストから削除する
   * 
   * @param newActivities
   * @param sourceActivities
   * @return 更新後のソースアイテムリスト
   */
  public List<PlusActivity> removeDisusedActivities(List<PlusActivity> newActivities,
      List<PlusActivity> sourceActivities) {
    for (Iterator<PlusActivity> iterator = sourceActivities.iterator(); iterator.hasNext();) {
      PlusActivity activity = iterator.next();
      if (!idIsExistIn(newActivities, activity.getId())) {
        logger.activitiesDeleted();
        iterator.remove();
      }
    }
    return sourceActivities;
  }

  /**
   * 処理対象のアクテビティをG+APIから読み込む
   * 
   * @return
   * @throws Exception
   */
  public List<PlusActivity> getNewActivitiesFromAPI(DataStoreHandler storeHandler)
      throws Exception {
    List<PlusActivity> newActivites = apiService.getPlusActivies(storeHandler.getActorId());
    newActivites = removeNoImageActivity(newActivites);
    return newActivites;
  }

  /**
   * IDがアイテムリストに存在するかをチェックする
   * 
   * @param items
   * @param id
   * @return
   */
  private <I extends PlusItem> boolean idIsExistIn(List<I> items, String id) {
    for (I item : items) {
      if (item.getId().equals(id)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 投稿写真のないアクテビティを除外する
   * 
   * @param activities
   * @return
   */
  private List<PlusActivity> removeNoImageActivity(List<PlusActivity> activities) {
    for (Iterator<PlusActivity> iterator = activities.iterator(); iterator.hasNext();) {
      PlusActivity activity = iterator.next();
      if (noImage(activity)) {
        iterator.remove();
      }
    }
    return activities;
  }

  /**
   * 単体アクテビティの写真投稿の有無をチェックする
   * 
   * @param activity
   * @return アイテムに写真投稿がない場合にtrue
   */
  private boolean noImage(PlusActivity activity) {
    if (activity.getAttachmentImageUrls() == null) {
      return true;
    } else if (activity.getAttachmentImageUrls().size() == 0) {
      return true;
    } else if (activity.getAttachmentImageUrls().get(0) == null) {
      return true;
    }
    return false;
  }

}
