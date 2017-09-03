package jp.leopanda.gPlusAnalytics.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import org.mortbay.log.Log;

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
  List<PlusPeople> plusOnersForUpdate; // 変更のあったアクテビティに＋１したユーザーのアイテムリスト

  int addedActivities = 0;
  int updatedActivities = 0;
  int updatedPlusOners = 0;
  int addedPlusOners = 0;
  int deletedPlusOners = 0;
  
  
  Logger loger = Logger.getLogger("DataConductor");

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
    storeHandler.putItems(updateSouceItems());
    return "";
  }

  /**
   * Google+APIを呼んで最新状態をチェックし、ソースアイテムに反映する。
   * 
   * @return
   * @throws Exception
   */
  private SourceItems updateSouceItems() throws Exception {
    PlusOneCounter plusOneCounter = new PlusOneCounter();
    this.plusOnersForUpdate = new ArrayList<PlusPeople>();

    SourceItems sourceItems = storeHandler.getItems();
    List<PlusActivity> newActivites = apiService.getPlusActivies(storeHandler.getActorId());// アクテビティは全件読み込む
    newActivites = removeNoImageActivity(newActivites);

    sourceItems.activities = updatePlusActivities(newActivites, sourceItems.activities);
    sourceItems.activities = removeInvalidActivities(newActivites, sourceItems.activities);
    sourceItems.activities = setStatisticsInfo(
        plusOneCounter.aggregatePlusOneCount(sourceItems.activities), sourceItems.activities);

    sourceItems.plusOners = addNewPlusOners(plusOnersForUpdate, sourceItems.plusOners);
    sourceItems.plusOners = updatePlusOners(plusOneCounter, sourceItems.plusOners);

    loger.info("追加アクテビティ数:" + String.valueOf(addedActivities));
    loger.info("更新アクテビティ数:" + String.valueOf(updatedActivities));
    loger.info("追加ユーザー数:" + String.valueOf(addedPlusOners));
    loger.info("更新ユーザー数:" + String.valueOf(plusOnersForUpdate.size()-addedPlusOners));
    loger.info("削除ユーザー数:" + String.valueOf(deletedPlusOners));
    
    return sourceItems;
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
        deletedPlusOners ++;
        iterator.remove();
      } else {
        int numOfPlusOne = counter.get(plusOner.getId());
        if (numOfPlusOne == 0) {
          deletedPlusOners ++;
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
   * 変更のあったアクテビティを更新する
   * 
   * @param newActivity
   * @param sourceActivities
   * @return
   * @throws Exception
   * @throws IOException
   */
  private List<PlusActivity> updatePlusActivities(List<PlusActivity> newItems,
      List<PlusActivity> sourceItems) throws Exception {
    return new CompareItemsForUpdate<PlusActivity>() {
      @Override
      PlusActivity setItemForNewAdd(PlusActivity newItem) throws Exception {
        newItem.setPlusOnerIds(
            stackPlusOnersForUpdate(apiService.getPlusOnersByActivity(newItem.getId())));
        addedActivities++;
        return newItem;
      }

      @Override
      PlusActivity setItemForUpdate(PlusActivity newItem, PlusActivity sourceItem)
          throws Exception {
        if (sourceItem.getNumOfPlusOners().equals(newItem.getNumOfPlusOners())) {
          return null; // 更新しない
        }
        updatedActivities++;
        addedActivities--;
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
        addedPlusOners++;
        return newItem;
      }

      @Override
      PlusPeople setItemForUpdate(PlusPeople newItem, PlusPeople sourceItem) throws Exception {
        return null;
      }
    }.update(newItems, sourceItems);
  }

  /**
   * ソースアイテムリストを最新のアイテムリストと比較しアップデートする抽象クラス
   * 
   * @param <I>
   */
  private abstract class CompareItemsForUpdate<I extends PlusItem> {
    List<I> update(List<I> newItems, List<I> sourceItems) throws Exception {
      for (I newItem : newItems) {
        sourceItems = updateItems(newItem, sourceItems);
      }
      return sourceItems;
    }

    private List<I> updateItems(I newItem, List<I> sourceItems) throws Exception {

      for (ListIterator<I> iterator = sourceItems.listIterator(); iterator.hasNext();) {
        I sourceItem = iterator.next();
        if (newItem.getId().equals(sourceItem.getId())) {
          I modifiedItem = setItemForUpdate(newItem, sourceItem);
          if (modifiedItem != null) {
            iterator.set(modifiedItem);
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

}
