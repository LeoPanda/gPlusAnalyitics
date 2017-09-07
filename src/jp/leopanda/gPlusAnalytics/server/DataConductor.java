package jp.leopanda.gPlusAnalytics.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.dataObject.SourceItems;
import jp.leopanda.gPlusAnalytics.dataStore.DataStoreHandler;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;
import jp.leopanda.googleAuthorization.client.NoCredentialException;

/**
 * Google+ APIサービスとDataStoreハンドラの両方を操作してデータを管理するクラス
 * 
 * @author LeoPanda
 *
 */
public class DataConductor {

  DataStoreHandler storeHandler;
  PlusApiService apiService;
  List<PlusActivity> newActivites; // 処理対象のアクテビティリスト
  List<PlusPeople> plusOnersForUpdate; // 変更のあったアクテビティに＋１したユーザーのアイテムリスト

  LoggerWidhConter logger = new LoggerWidhConter(DataConductor.class.getName());

  /**
   * コンストラクタ
   * 
   * @throws HostGateException
   * 
   * @throws NoCredentialException
   * @throws IOException
   * 
   */
  public DataConductor(DataStoreHandler storeHandler, PlusApiService apiService) {
    this.storeHandler = storeHandler;
    this.apiService = apiService;
  }

  /**
   * データストアを最新状態に更新する
   * 
   * @throws Exception
   */
  public String updateDataStore() throws Exception {
    SourceItems sourceItems = storeHandler.getItems();
    // 実行時間超過時の割り込み処理
    new DeadlineInterrupter<PlusActivity, SourceItems>(newActivites, sourceItems) {
      @Override
      public void whenExceeded(List<PlusActivity> unprocessedItems, SourceItems sourceItems)
          throws Exception {
        putInterruptedList(unprocessedItems);
        storeHandler.putItems(sourceItems);
      }
    }.setInterruptTask();
    // 通常処理
    storeHandler.putItems(updateSouceItems(sourceItems));
    logger.writeLog();
    return "";
  }

  /**
   * Google+APIを呼んで最新状態をチェックし、ソースアイテムに反映する。
   * 
   * @return
   * @throws Exception
   */
  private SourceItems updateSouceItems(SourceItems sourceItems) throws Exception {
    PlusOneCounter plusOneCounter = new PlusOneCounter();
    this.plusOnersForUpdate = new ArrayList<PlusPeople>();
    List<PlusActivity> interruptedActivites = storeHandler.getInterrupted();
    if (interruptedActivites.size() > 0) {
      //前回処理が途中で中断されていた場合は残りのアクテビティを処理する
      newActivites = interruptedActivites;
    } else {
      //通常時はG+APIから写真付き投稿アクテビティをすべて読み出す
      newActivites = getNewActivitiesFromAPI();
      sourceItems.activities = removeInvalidActivities(newActivites, sourceItems.activities);
    }
    // アクテビティリストの処理
    sourceItems.activities = updatePlusActivitiesAndStackPlusOners(newActivites,
        sourceItems.activities);
    sourceItems.activities = setStatisticsInfo(
        plusOneCounter.aggregatePlusOneCount(sourceItems.activities), sourceItems.activities);
    // +1erリストの処理
    sourceItems.plusOners = addNewPlusOners(plusOnersForUpdate, sourceItems.plusOners);
    sourceItems.plusOners = updatePlusOners(plusOneCounter, sourceItems.plusOners);
    // 中断時未処理アクテビティリストをクリアする
    putInterruptedList(newActivites);

    return sourceItems;
  }

  /**
   * 中断時未処理アクテビティリストをデータストアに書き込む
   * 
   * @throws Exception
   */
  private void putInterruptedList(List<PlusActivity> unprocessedActivities) throws Exception {
    storeHandler.putInterrupted(unprocessedActivities);

  }

  /**
   * 処理対象のアクテビティをG+APIから新規読み込みする
   * 
   * @return
   * @throws Exception
   */
  private List<PlusActivity> getNewActivitiesFromAPI() throws Exception {
    List<PlusActivity> newActivites = apiService.getPlusActivies(storeHandler.getActorId());// アクテビティは全件読み込む
    newActivites = removeNoImageActivity(newActivites);
    return newActivites;

  }

  /**
   * ソース+1erアイテムリストの+1数を更新する ソースアクテビティに存在しなくなった+1erはアイテムリストから削除する
   * 
   * @param counter
   * @param sourceItems
   * @return
   */
  private List<PlusPeople> updatePlusOners(PlusOneCounter counter,
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
   * ソースアクテビティリストの統計情報をセットする
   * 
   * @param plusOneCounter
   * @param sourceItems
   * @return
   */
  private List<PlusActivity> setStatisticsInfo(PlusOneCounter plusOneCounter,
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
    return activity;
  }

  /**
   * 最新アクテビティアイテムリストにないアイテムをソースアイテムリストから削除する
   * 
   * @param newActivities
   * @param sourceItems
   * @return 更新後のソースアイテムリスト
   */
  private List<PlusActivity> removeInvalidActivities(List<PlusActivity> newActivities,
      List<PlusActivity> sourceItems) {
    for (Iterator<PlusActivity> iterator = sourceItems.iterator(); iterator.hasNext();) {
      PlusActivity activity = iterator.next();
      if (!idIsExistIn(newActivities, activity.getId())) {
        iterator.remove();
      }
    }
    return sourceItems;
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
   * 写真投稿の有無をチェックする
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

  /**
   * 変更のあったアクテビティを更新し、処理対象の+1リストを作成する 処理の終わったnewItemはリストから削除される
   * 
   * @param newActivity
   * @param sourceActivities
   * @return
   * @throws Exception
   * @throws IOException
   */
  private List<PlusActivity> updatePlusActivitiesAndStackPlusOners(List<PlusActivity> newItems,
      List<PlusActivity> sourceItems) throws Exception {
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
    }.update(newItems, sourceItems);
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
   * 新規のPlusOnerをソースアイテムリストへ適用する
   * 
   * @throws Exception
   */
  private List<PlusPeople> addNewPlusOners(List<PlusPeople> newItems, List<PlusPeople> sourceItems)
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
