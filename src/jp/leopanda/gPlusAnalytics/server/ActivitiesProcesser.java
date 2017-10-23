package jp.leopanda.gPlusAnalytics.server;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

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
   * 変更のあったアクテビティを更新し、処理対象の+1erリストを抽出する
   * 
   * @param newActivities 最新のアクテビティリスト
   * @param sourceActivities ソースアクテビティリスト
   * @return
   * @throws Exception
   */
  public List<PlusActivity> updatePlusActivitiesAndStackPlusOners(List<PlusActivity> newActivities,
      List<PlusActivity> sourceActivities) throws Exception {
    logger.setnumOfNewActivities(newActivities.size());
    return new NewItemsApplyer<PlusActivity>(newActivities, sourceActivities,logger) {}.apply(
        newItem -> setPlusOnerIdsFromApi(newItem),
        (newItem, sourceItem) -> replaceWhen(newItem, sourceItem));
  }

  /**
   * 同一IDをもつ既存アイテムを新規アイテムで置き換える場合の条件を設定する
   * 
   * @param newItem
   * @param sourceItem
   * @return
   * @throws Exception
   */
  private boolean replaceWhen(PlusActivity newItem, PlusActivity sourceItem) {
    return !(sourceItem.getNumOfPlusOners().equals(newItem.getNumOfPlusOners()));
  }

  /**
   * アクテビティに+1したユーザーのリストをG+APIサービスを呼んでセットする
   * 
   * @param newItem
   * @return
   * @throws Exception
   */
  private PlusActivity setPlusOnerIdsFromApi(PlusActivity newItem) throws Exception {
    newItem.setPlusOnerIds(
        getPlusOnerIdsAndStackToUpdateList(apiService.getPlusOnersByActivity(newItem.getId())));
    return newItem;
  }

  /**
   * 新規アクテビティに+1したユーザーのリストを抽出し
   * +1erの更新対象リストにスタックする
   * 
   * @param onersInActivity
   *          APIコールで得たactivity毎の＋１ユーザーリスト
   * @return Activityに+1したユーザーのIDリスト
   */
  private List<String> getPlusOnerIdsAndStackToUpdateList(List<PlusPeople> onersInActivity) {
    List<String> plusOnerIds = new ArrayList<String>();
    onersInActivity.forEach(plusOner -> {
      String plusOnerId = plusOner.getId();
      plusOnerIds.add(plusOnerId);
      if (!idIsExistIn(this.plusOnersForUpdate, plusOnerId)) {
        this.plusOnersForUpdate.add(plusOner);
      }
    });
    return plusOnerIds;
  }

  /**
   * ソースアクテビティリストの統計情報をセットする
   * 
   * @param plusOneSummrizer
   * @param sourceItems
   * @return
   */
  public List<PlusActivity> setStatisticsInfo(SummrizerByPlusOners plusOneSummrizer,
      List<PlusActivity> sourceItems) {
    ListIterator<PlusActivity> iterator = sourceItems.listIterator();
    while (iterator.hasNext()) {
      PlusActivity activity = iterator.next();
      iterator.set(setActivityStastics(plusOneSummrizer, activity));
    }
    return sourceItems;
  }

  /**
   * アクテビティ単体の統計情報をセットする
   * 
   * @param plusOneSummrizer
   * @param activity
   * @return
   */
  private PlusActivity setActivityStastics(SummrizerByPlusOners plusOneSummrizer, PlusActivity activity) {

    activity.highLookers = 0;
    activity.highMiddleLookers = 0;
    activity.lowMiddleLookers = 0;
    activity.firstLookers = 0;

    for (String plusOnerId : activity.plusOnerIds) {
      int numOfPlusOne = plusOneSummrizer.get(plusOnerId);
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
  public List<PlusActivity> removeDisusedActivitiesFromSource(List<PlusActivity> newActivities,
      List<PlusActivity> sourceActivities) {
    List<PlusActivity> removeActivies = sourceActivities.stream()
        .filter(sourceActivity -> !idIsExistIn(newActivities, sourceActivity.getId()))
        .collect(Collectors.toList());
    logger.activitiesDeleted(removeActivies.size());
    sourceActivities.removeAll(removeActivies);
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
    return removeNoImageActivity(apiService.getPlusActivies(storeHandler.getActorId()));
  }

  /**
   * IDがアイテムリストに存在するかをチェックする
   * 
   * @param items
   * @param id
   * @return
   */
  private <I extends PlusItem> boolean idIsExistIn(List<I> items, String id) {
    return items.stream().anyMatch(item -> item.getId().equals(id));
  }

  /**
   * 投稿写真のないアクテビティを除外する
   * 
   * @param activities
   * @return
   */
  private List<PlusActivity> removeNoImageActivity(List<PlusActivity> activities) {
    activities.removeIf(activity -> !activity.getFirstAttachmentImageUrl().isPresent());
    return activities;
  }
}
