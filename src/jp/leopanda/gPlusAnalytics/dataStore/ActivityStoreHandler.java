package jp.leopanda.gPlusAnalytics.dataStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.client.util.SortComparator;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.dataObject.SourceItems;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;
import jp.leopanda.gPlusAnalytics.server.PlusApiService;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * Activityエンティティの操作メソッドを提供する
 * 
 * @author LeoPanda
 *
 */
@Deprecated
public class ActivityStoreHandler {
  DatastoreService ds;

  public ActivityStoreHandler(DatastoreService ds) {
    this.ds = ds;
  }

  Logger logger = Logger.getLogger("ActivityStoreHandlerLogger");

  /**
   * データストアへActivity Entityを書き込む
   * 
   * @param activity
   *          アクティビティのアイテムオブジェクト
   * @param plusOners
   *          アクティビティに+1したユーザーのアイテムオブジェクトリスト
   * @throws HostGateException
   *           例外スロー
   */
  @Deprecated
  public void putActivity(PlusActivity activity, List<PlusPeople> plusOners, Key entityKey)
      throws HostGateException {
    DataStoreEntity activityEntity;
    if (entityKey != null) {
      try {
        activityEntity = new DataStoreEntity(ds.get(entityKey));
      } catch (EntityNotFoundException e) {
        throw new HostGateException(e.toString());
      }
    } else {
      activityEntity = new DataStoreEntity(ActivityProperty.KIND);
    }
    activityEntity.setProperty(ActivityProperty.ID, activity.getId());
    activityEntity.setProperty(ActivityProperty.ACTOR_ID, activity.getActorId());
    activityEntity.setProperty(ActivityProperty.PUBLISHED, activity.getPublished());
    activityEntity.setProperty(ActivityProperty.NUM_OF_PLUSONERS, activity.getNumOfPlusOners());

    SerializerOld serializer = new SerializerOld();
    activityEntity.setProperty(ActivityProperty.ACTIVITY_ITEM, serializer.encode(activity));
    activityEntity.setProperty(ActivityProperty.PLUSONER_ITEMS, serializer.encode(plusOners));
    ds.put(activityEntity.getEntity());
  }

  /**
   * データストア上のアクテビティをGoogle+から読み直して再更新する。
   * 
   * @param actorId
   * @param googleApi
   * @throws HostGateException
   * @throws IOException
   */
  @Deprecated
  public void updateActivies(String actorId, PlusApiService googleApi)
      throws HostGateException, IOException {
    PreparedQuery pq = ds.prepare(getAllActivitiesQuery(actorId));
    for (Entity entity : pq.asIterable()) {
      DataStoreEntity newEntity = new DataStoreEntity(entity);
      String activityId = (String) newEntity.getProperty(ActivityProperty.ID);
      logger.info("activityID=" + activityId);
      PlusActivity newActivity = googleApi.getPlusActiviy(activityId);
      if (newActivity == null) {
        ds.delete(entity.getKey());
        logger.info("entity removed.");
        continue;
      }
      PlusActivity oldActivity = getOldActivity(newEntity);
      if (oldActivity == null) {
        continue;
      }
      newEntity.setProperty(ActivityProperty.ACTIVITY_ITEM, new SerializerOld().encode(newActivity));
      ds.put(newEntity.getEntity());
      logger.info("entity updated.");
    }

  }

  /**
   * データストア上の更新対象アクテビティを取得する。 すでに更新が完了している場合はNullを返す。
   * 
   * @param entity
   * @return
   * @throws HostGateException
   */
  @Deprecated
  private PlusActivity getOldActivity(DataStoreEntity entity) throws HostGateException {
    PlusActivity oldActivity = new Serializer<PlusActivity>(){}
        .decodeAsPlusActivity((Blob) entity.getProperty(ActivityProperty.ACTIVITY_ITEM));
    logger.info("entity key=" + entity.getEntity().getKey().toString());
    if (oldActivity.object.attachments != null) {
      if (oldActivity.object.attachments.get(0).getImageHeight() != null) {
        logger.info("has been updated. skip process.");
        return null;
      }
    }
    return oldActivity;
  }

  /**
   * アクテビティチェックマップを返す
   * 
   * @param actorId
   *          アクティビティのオーナーID
   * @return アクティビティチェックマップ
   */
  @Deprecated
  public ActivityCheckMap getActivityCheckMap(String actorId) {
    ActivityCheckMap checkMap = new ActivityCheckMap();
    PreparedQuery pq = ds.prepare(
        getAllActivitiesQuery(actorId)
            .addProjection(ActivityProperty.ID.getProjection())
            .addProjection(ActivityProperty.NUM_OF_PLUSONERS.getProjection()));
    for (Entity entity : pq.asIterable()) {
      DataStoreEntity activityEntity = new DataStoreEntity(entity);
      checkMap.put((String) activityEntity.getProperty(ActivityProperty.ID),
          (int) (long) activityEntity.getProperty(ActivityProperty.NUM_OF_PLUSONERS),
          entity.getKey());
    }
    return checkMap;
  }

  /**
   * データストアの内容をオブジェクト形式で返す
   * 
   * @param actorId
   *          アクティビティのオーナーID
   * @return アクティビティと+1ersのアイテムオブジェクトをパックしたデータオブジェクト
   * @throws HostGateException
   *           例外スロー
   */
  @Deprecated
  public SourceItems getItems(String actorId) throws HostGateException {
    AllPlusOnersMap plusOnersMap = new AllPlusOnersMap();
    List<PlusActivity> activities = new ArrayList<PlusActivity>();
    Serializer<PlusActivity> serializer = new Serializer<PlusActivity>(){};
    PreparedQuery pq = ds.prepare(getAllActivitiesQuery(actorId));
    for (Entity entity : pq.asIterable()) {
      DataStoreEntity activityEntity = new DataStoreEntity(entity);
      PlusActivity activity = serializer
          .decodeAsPlusActivity((Blob) activityEntity.getProperty(ActivityProperty.ACTIVITY_ITEM));
      List<PlusPeople> plusOners = serializer
          .decodeAsPlusOners((Blob) activityEntity.getProperty(ActivityProperty.PLUSONER_ITEMS));
      activity.setPlusOnerIds(getPlusOnerIds(plusOners));// アクテビティに+1ユーザーIDリストをセット
      activities.add(activity);
      plusOnersMap.addToMap(plusOners); // +1ers情報の集計
    }
    // アクテビティに統計情報をセット
    for (PlusActivity activity : activities) {
      activity = setDistributionInfo(activity, plusOnersMap.getNumOfPlusOneMap());
    }
    // アクティビティを最新日付順にソート
    Collections.sort(activities, new SortComparator().getLatestActivitesOrder());
    //結果内容のセット
    SourceItems result = new SourceItems();
    result.setActivities(activities);
    result.setPlusOners(plusOnersMap.getPlusOners());
    return result;
  }

  /**
   * +1ユーザーのIDをリストにして抽出
   * 
   * @param plusOners
   *          +1ersアイテムオブジェクトリスト
   * @return +1ersユーザーIDのリスト
   */
  public List<String> getPlusOnerIds(List<PlusPeople> plusOners) {
    List<String> plusOnerIds = new ArrayList<String>();
    for (PlusPeople plusPeople : plusOners) {
      plusOnerIds.add(plusPeople.getId());
    }
    return plusOnerIds;
  }

  /**
   * 特定オーナーのエンティティをすべて削除する
   * 
   * @param actorId
   *          アクティビティのオーナーID
   */
  public void removeAllActivities(String actorId) {
    PreparedQuery pq = ds.prepare(getAllActivitiesQuery(actorId).setKeysOnly());
    for (Entity entity : pq.asIterable()) {
      ds.delete(entity.getKey());
    }
  }

  /**
   * データストア上の最新のアクテビティエンティティの作成日付を返す
   * 
   * @param actorId
   *          アクティビティのオーナーID
   * @return データストア上の最新のアクテビティエンティティの作成日付
   */
  public Date getLatestActivityPublished(String actorId) {
    PreparedQuery pq = ds.prepare(getAllActivitiesQuery(actorId)
        .addProjection(ActivityProperty.PUBLISHED.getProjection())
        .addSort(ActivityProperty.PUBLISHED.getName(), SortDirection.DESCENDING));
    List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(1));
    if (entities.size() == 0) {
      return null;
    }
    DataStoreEntity activityEntity = new DataStoreEntity(entities.get(0));
    return (Date) activityEntity.getProperty(ActivityProperty.PUBLISHED);
  }

  /**
   * アクティビティに統計情報をセットする
   * 
   * @param activity
   *          アクティビティアイテムオブジェクト
   * @param numOfPlusOneMap
   *          +1resチェックマップ
   * @return 統計情報をセットしたアイテムオブジェクト
   */
  public PlusActivity setDistributionInfo(PlusActivity activity,
      Map<String, Integer> numOfPlusOneMap) {
    int firstLookers = 0;
    int lowMiddleLookers = 0;
    int highMiddleLookers = 0;
    int highLookers = 0;
    for (String plusOneId : activity.getPlusOnerIds()) {
      int numOfPlusOne = numOfPlusOneMap.get(plusOneId);
      if (numOfPlusOne > Distribution.HIGH_LOOKER.threshold) {
        highLookers += 1;
      } else if (numOfPlusOne > Distribution.HIGH_MIDDLE_LOOKER.threshold) {
        highMiddleLookers += 1;
      } else if (numOfPlusOne > Distribution.LOW_MIDDLE_LOOKER.threshold) {
        lowMiddleLookers += 1;
      } else {
        firstLookers += 1;
      }
    }
    activity.setHighLookers(highLookers);
    activity.setHighMiddleLookers(highMiddleLookers);
    activity.setLowMiddleLookers(lowMiddleLookers);
    activity.setFirstLookers(firstLookers);

    return activity;
  }

  /**
   * Activityデータストアの基本クエリを作成する
   * 
   * @param actorId
   *          アクティビティのオーナーID
   * @return アクティビティエンティティを取り出す基本Query
   */
  private Query getAllActivitiesQuery(String actorId) {

    return new Query(ActivityProperty.KIND.getName()).setFilter(new FilterPredicate(
        ActivityProperty.ACTOR_ID.getName(), FilterOperator.EQUAL, actorId));
  }
}
