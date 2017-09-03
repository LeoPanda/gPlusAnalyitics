package jp.leopanda.gPlusAnalytics.dataStore;

import java.util.Collections;

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
  SourceItemHandler<PlusActivity> activitiesHandler;
  SourceItemHandler<PlusPeople> plusOnersHandler;
  String actorId;

  /**
   * コンストラクタ
   */
  public DataStoreHandler(String actorId) {
    this.actorId = actorId;
    this.ds = DatastoreServiceFactory.getDatastoreService();
    activitiesHandler = new SourceItemHandler<PlusActivity>("Activities", ds, actorId) {
    };
    plusOnersHandler = new SourceItemHandler<PlusPeople>("PlusOners", ds, actorId) {
    };
  }

  /**
   * カレントのActorIDを返す
   * @return
   */
  public String getActorId(){
    return this.actorId;
  }
  
  /**
   * ソースアイテムをデータストアから取得する
   * 
   * @return
   * @throws HostGateException
   */
  public SourceItems getItems() throws HostGateException {
    SourceItems items = new SourceItems();
    //Jsonからデコード
    activitiesHandler.setClass(PlusActivity.class);
    plusOnersHandler.setClass(PlusPeople.class);
    items.activities = activitiesHandler.getItems();
    items.plusOners = plusOnersHandler.getItems();
    // ソート
    Collections.sort(items.activities, new SortComparator().getLatestActivitesOrder());
    Collections.sort(items.plusOners,new SortComparator().getPlusOnerDecendingOrder());
 
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
