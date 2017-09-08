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

/**
 * Actor毎のDataStore Handler
 * 
 * @author LeoPanda
 *
 */
public class DataStoreHandler {

  private DatastoreService ds;
  SourceItemHandler<PlusActivity> interruptedActivitiesHandler;
  SourceItemHandler<PlusActivity> activitiesHandler;
  SourceItemHandler<PlusPeople> plusOnersHandler;

  String actorId;
  SourceItems items = null;

  Logger logger = Logger.getLogger(DataStoreHandler.class.getName());
  
  /**
   * コンストラクタ
   */
  public DataStoreHandler(String actorId) {
    this.actorId = actorId;
    this.ds = DatastoreServiceFactory.getDatastoreService();

    interruptedActivitiesHandler = new SourceItemHandler<PlusActivity>("interruptedActivities", ds,
        actorId) {
    };
    activitiesHandler = new SourceItemHandler<PlusActivity>("Activities", ds, actorId) {
    };
    plusOnersHandler = new SourceItemHandler<PlusPeople>("PlusOners", ds, actorId) {
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
   * 中断時未処理アクテビティリストを取得する
   * 
   * @return
   * @throws HostGateException
   */
  public List<PlusActivity> getInterrupted() throws HostGateException {
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
