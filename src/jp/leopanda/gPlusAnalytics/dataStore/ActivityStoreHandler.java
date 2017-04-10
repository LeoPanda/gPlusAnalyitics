package jp.leopanda.gPlusAnalytics.dataStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.dataObject.StoredItems;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;
import jp.leopanda.gPlusAnalytics.server.PlusApiService;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
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
public class ActivityStoreHandler {
  DatastoreService ds;

  public ActivityStoreHandler(DatastoreService ds) {
    this.ds = ds;
  }

  Logger loger = Logger.getLogger("ActivityStoreHandlerLogger");

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
  public void putActivity(PlusActivity activity, List<PlusPeople> plusOners, Key entityKey)
      throws HostGateException {
    Entity activityEntity;
    if (entityKey != null) {
      try {
        activityEntity = ds.get(entityKey);
      } catch (EntityNotFoundException e) {
        throw new HostGateException(e.toString());
      }
    } else {
      activityEntity = new Entity(ActivityEntity.KIND.val);
    }
    activityEntity.setProperty(ActivityEntity.ID.val, activity.getId());
    activityEntity.setProperty(ActivityEntity.ACTOR_ID.val, activity.getActorId());
    activityEntity.setProperty(ActivityEntity.PUBLISHED.val, activity.getPublished());
    activityEntity.setProperty(ActivityEntity.NUM_OF_PLUSONERS.val, activity.getNumOfPlusOners());

    Serializer serializer = new Serializer();
    activityEntity.setProperty(ActivityEntity.ACTIVITY_ITEM.val, serializer.encode(activity));
    activityEntity.setProperty(ActivityEntity.PLUSONER_ITEMS.val, serializer.encode(plusOners));
    ds.put(activityEntity);
  }

  /**
   * データストアのアクテビティをAPIを読み直して再更新する。
   * 
   * @param actorId
   * @param googleApi
   * @throws HostGateException
   * @throws IOException
   */
  public void updateActivies(String actorId, PlusApiService googleApi)
      throws HostGateException, IOException {
    PreparedQuery pq = ds.prepare(getActivityQuery(actorId));
    for (Entity entity : pq.asIterable()) {
      PlusActivity oldActivity = getOldActivity(entity);
      if (oldActivity == null) {
        continue;
      }
      PlusActivity newActivity = googleApi.getPlusActiviy(oldActivity.getId());
      if (newActivity == null) {
        ds.delete(entity.getKey());
        loger.info("entity deleted.");
        continue;
      }
      entity.setProperty(ActivityEntity.ACTIVITY_ITEM.val, new Serializer().encode(newActivity));
      ds.put(entity);
      loger.info("entity updated.");
    }

  }

  /**
   * データストア上の更新対象アクテビティを取得する。
   * すでに更新が完了している場合はNullを返す。
   * @param entity
   * @return
   * @throws HostGateException
   */
  private PlusActivity getOldActivity(Entity entity) throws HostGateException {
    PlusActivity oldActivity = new Serializer()
        .decodeAsPlusActivity((Blob) entity.getProperty(ActivityEntity.ACTIVITY_ITEM.val));
    loger.info("entity key=" + entity.getKey().toString());
    loger.info("activityID=" + oldActivity.id);
    if (oldActivity.object.attachments != null) {
      if (oldActivity.object.attachments.get(0).getImageHeight() != null) {
        loger.info("has been updated. skip process.");
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
  public ActivityCheckMap getActivityCheckMap(String actorId) {
    ActivityCheckMap checkMap = new ActivityCheckMap();
    PreparedQuery pq = ds.prepare(getActivityQuery(actorId).addProjection(
        new PropertyProjection(ActivityEntity.ID.val, String.class)).addProjection(
            new PropertyProjection(ActivityEntity.NUM_OF_PLUSONERS.val, Integer.class)));
    for (Entity entity : pq.asIterable()) {
      checkMap.put((String) entity.getProperty(ActivityEntity.ID.val),
          (int) (long) entity.getProperty(ActivityEntity.NUM_OF_PLUSONERS.val), entity.getKey());
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
  public StoredItems getItems(String actorId) throws HostGateException {
    PlusOnerHandler plusOnerHandler = new PlusOnerHandler();
    List<PlusActivity> activities = new ArrayList<PlusActivity>();
    Serializer serializer = new Serializer();
    PreparedQuery pq = ds.prepare(getActivityQuery(actorId));
    for (Entity entity : pq.asIterable()) {
      PlusActivity activity = serializer.decodeAsPlusActivity((Blob) entity
          .getProperty(ActivityEntity.ACTIVITY_ITEM.val));
      activities.add(activity);
      List<PlusPeople> plusOners = serializer
          .decodeAsPlusOners((Blob) entity.getProperty(ActivityEntity.PLUSONER_ITEMS.val));
      activity.setPlusOnerIds(getPlusOnerIds(plusOners));// アクテビティに+1ユーザーIDリストをセット
      plusOnerHandler.aggregatePlusOnes(plusOners); // +1情報を集計
    }
    // アクテビティに統計情報をセット
    for (PlusActivity activity : activities) {
      activity = setDistributionInfo(activity, plusOnerHandler.getNumOfPlusOneMap());
    }
    // アクティビティを最新日付順にソート
    Collections.sort(activities, new Comparator<PlusActivity>() {
      @Override
      public int compare(PlusActivity o1, PlusActivity o2) {
        return o2.getPublished().compareTo(o1.getPublished());
      }
    });
    // +1ersを+1数降順にソート
    List<PlusPeople> plusOners = plusOnerHandler.getPlusOners();
    Collections.sort(plusOners, new Comparator<PlusPeople>() {
      @Override
      public int compare(PlusPeople o1, PlusPeople o2) {
        return o2.getNumOfPlusOne() - o1.getNumOfPlusOne();
      }
    });
    StoredItems result = new StoredItems();
    result.setActivities(activities);
    result.setPlusOners(plusOners);
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
   * エンティティを削除する
   * 
   * @param actorId
   *          アクティビティのオーナーID
   */
  public void remove(String actorId) {
    PreparedQuery pq = ds.prepare(getActivityQuery(actorId).setKeysOnly());
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
    PreparedQuery pq = ds.prepare(getActivityQuery(actorId).addProjection(
        new PropertyProjection(ActivityEntity.PUBLISHED.val, Date.class)).addSort(
            ActivityEntity.PUBLISHED.val, SortDirection.DESCENDING));
    List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(1));
    if (entities.size() == 0) {
      return null;
    }
    return (Date) entities.get(0).getProperty(ActivityEntity.PUBLISHED.val);
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
  private Query getActivityQuery(String actorId) {

    return new Query(ActivityEntity.KIND.val).setFilter(new FilterPredicate(
        ActivityEntity.ACTOR_ID.val, FilterOperator.EQUAL, actorId));
  }

}
