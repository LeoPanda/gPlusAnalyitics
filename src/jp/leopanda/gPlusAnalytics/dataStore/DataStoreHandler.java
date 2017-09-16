package jp.leopanda.gPlusAnalytics.dataStore;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import jp.leopanda.gPlusAnalytics.client.util.SortComparator;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.dataObject.SourceItems;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;
import jp.leopanda.gPlusAnalytics.server.DailyStats;

/**
 * Actor毎のDataStore Handler
 * 
 * @author LeoPanda
 *
 */
public class DataStoreHandler {

  private DatastoreService ds;
  StoredItemHandler<PlusActivity> interruptedActivitiesHandler;
  StoredItemHandler<PlusActivity> activitiesHandler;
  StoredItemHandler<PlusPeople> plusOnersHandler;
  StoredItemHandler<DailyStats> dailyStatsHandler;

  String actorId;
  SourceItems items = null;

  Logger logger = Logger.getLogger(DataStoreHandler.class.getName());

  /**
   * コンストラクタ
   */
  public DataStoreHandler(String actorId) {
    this.actorId = actorId;
    this.ds = DatastoreServiceFactory.getDatastoreService();

    interruptedActivitiesHandler = new StoredItemHandler<PlusActivity>("interruptedActivities", ds,
        actorId) {
    };
    activitiesHandler = new StoredItemHandler<PlusActivity>("Activities", ds, actorId) {
    };
    plusOnersHandler = new StoredItemHandler<PlusPeople>("PlusOners", ds, actorId) {
    };
    dailyStatsHandler = new StoredItemHandler<DailyStats>("dailyStats", ds, actorId) {
    };
  }

  /**
   * カレントのActorIDを返す
   * 
   * @return
   */
  public String getActorId() {
    return this.actorId;
  }

  /**
   * バッチ統計情報リストを取得する
   * 
   * @return
   * @throws HostGateException
   */
  public List<DailyStats> getDailyStats() throws HostGateException {
    dailyStatsHandler.setClass(DailyStats.class);
    return dailyStatsHandler.getItems();
  }

  /**
   * バッチ統計上リストをデータストアに書き込む
   * 
   * @param dailyStatsList
   * @throws Exception
   */
  public void putDailyStats(List<DailyStats> dailyStatsList) throws Exception {
    dailyStatsHandler.setListLimit(200);
    dailyStatsHandler.putItems(dailyStatsList);
  }

  /**
   * 中断時未処理アクテビティリストを取得する
   * 
   * @return
   * @throws HostGateException
   */
  public List<PlusActivity> getInterruptedActivities() throws HostGateException {
    interruptedActivitiesHandler.setClass(PlusActivity.class);
    List<PlusActivity> items = interruptedActivitiesHandler.getItems();
    return items;
  }

  /**
   * 未処理のアクテビティリストをデータストアに書き込む
   * 
   * @param interruptedActivities
   * @throws HostGateException
   */
  public void putInterrupted(List<PlusActivity> interruptedActivities) throws HostGateException {
    interruptedActivitiesHandler.setListLimit(200);
    interruptedActivitiesHandler.putItems(interruptedActivities);
  }

  /**
   * ソースアイテムをデータストアから取得する
   * 
   * @return
   * @throws HostGateException
   */
  public SourceItems getItems() throws HostGateException {
    if (items != null) {
      logger.info("Items are returned from server memory.");
      return items;
    }
    items = new SourceItems();
    // Jsonからデコード
    activitiesHandler.setClass(PlusActivity.class);
    plusOnersHandler.setClass(PlusPeople.class);
    items.activities = activitiesHandler.getItems();
    items.plusOners = plusOnersHandler.getItems();
    // ソート
    Collections.sort(items.activities, new SortComparator().getLatestActivitesOrder());
    Collections.sort(items.plusOners, new SortComparator().getPlusOnerDecendingOrder());
    return items;
  }

  /**
   * ソースアイテムをデータストアへ書き込む
   * 
   * @param items
   * @throws HostGateException
   */
  public void putItems(SourceItems items) throws HostGateException {
    activitiesHandler.setListLimit(200);
    activitiesHandler.putItems(items.activities);
    plusOnersHandler.setListLimit(1000);
    plusOnersHandler.putItems(items.plusOners);
  }

}
