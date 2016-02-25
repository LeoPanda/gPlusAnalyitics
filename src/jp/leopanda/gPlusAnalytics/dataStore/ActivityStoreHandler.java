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
import jp.leopanda.gPlusAnalytics.dataObject.ResultPack;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;

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
	 * @param plusOners
	 * @throws IOException
	 */
	public void putActivity(PlusActivity activity, List<PlusPeople> plusOners,
			Key entityKey) throws HostGateException {
		Entity activityEntity;
		if (entityKey != null) {
			try {
				activityEntity = ds.get(entityKey);
			} catch (EntityNotFoundException e) {
				throw new HostGateException(e.toString());
			}
		} else {
			activityEntity = new Entity(Activity.KIND.val);
		}
		activityEntity.setProperty(Activity.id.val, activity.getId());
		activityEntity.setProperty(Activity.actorId.val, activity.getActorId());
		activityEntity.setProperty(Activity.published.val,
				activity.getPublished());
		activityEntity.setProperty(Activity.numOfPlusOners.val,
				activity.getNumOfPlusOners());

		Serializer serializer = new Serializer();
		activityEntity.setProperty(Activity.activiyItem.val,
				serializer.encode(activity));
		activityEntity.setProperty(Activity.plusOnerItems.val,
				serializer.encode(plusOners));
		ds.put(activityEntity);
	}
	/**
	 * アクテビティチェックマップを返す
	 * 
	 * @param actorId
	 * @return
	 */
	public ActivityCheckMap getActivityCheckMap(String actorId) {
		ActivityCheckMap checkMap = new ActivityCheckMap();
		PreparedQuery pq = ds.prepare(getActivityQuery(actorId).addProjection(
				new PropertyProjection(Activity.id.val, String.class))
				.addProjection(
						new PropertyProjection(Activity.numOfPlusOners.val,
								Integer.class)));
		for (Entity entity : pq.asIterable()) {
			checkMap.put((String) entity.getProperty(Activity.id.val),
					(int)(long)entity.getProperty(Activity.numOfPlusOners.val),
					entity.getKey());
		}
		return checkMap;
	}
	/**
	 * 　データストアの内容をオブジェクト形式で返す
	 * 
	 * @param actorId
	 * @return
	 * @throws HostGateException
	 */
	public ResultPack getItems(String actorId) throws HostGateException {
		ResultPack result = new ResultPack();
		PlusOnerHandler plusOnerHandler = new PlusOnerHandler();
		List<PlusActivity> activities = new ArrayList<PlusActivity>();
		Serializer serializer = new Serializer();
		PreparedQuery pq = ds.prepare(getActivityQuery(actorId));
		for (Entity entity : pq.asIterable()) {
			PlusActivity activity = serializer
					.decodeAsPlusActivity((Blob) entity
							.getProperty(Activity.activiyItem.val));
			activities.add(activity);
			List<PlusPeople> plusOners = serializer
					.decodeAsPlusOners((Blob) entity
							.getProperty(Activity.plusOnerItems.val));
			activity.setPlusOnerIds(getPlusOnerIds(plusOners));// アクテビティに+1ユーザーIDリストをセット
			plusOnerHandler.aggregatePlusOnes(plusOners); // +1情報を集計
		}
		// アクテビティに統計情報をセット
		for (PlusActivity activity : activities) {
			activity = setDistributionInfo(activity,
					plusOnerHandler.getNumOfPlusOneMap());
		}
		// アクティビティを最新日付順にソート
		Collections.sort(activities, new Comparator<PlusActivity>() {
			@Override
			public int compare(PlusActivity o1, PlusActivity o2) {
				return o2.getPublished().compareTo(o1.getPublished());
			}
		});
		//+1ersを+1数降順にソート
		List<PlusPeople> plusOners = plusOnerHandler.getPlusOners();
		Collections.sort(plusOners,new Comparator<PlusPeople>(){
			@Override
			public int compare(PlusPeople o1, PlusPeople o2) {
				return o2.getNumOfPlusOne() - o1.getNumOfPlusOne();
			}
		});
		result.setActivities(activities);
		result.setPlusOners(plusOners);
		return result;
	}
	/**
	 * +1ユーザーのIDをリストにして抽出
	 * 
	 * @param plusOners
	 * @return
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
	 * @return
	 */
	public Date getLatestActivityPublished(String actorId) {
		PreparedQuery pq = ds.prepare(getActivityQuery(actorId).addProjection(
				new PropertyProjection(Activity.published.val, Date.class))
				.addSort(Activity.published.val, SortDirection.DESCENDING));
		List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(1));
		if (entities.size() == 0) {
			return null;
		}
		return (Date) entities.get(0).getProperty(Activity.published.val);
	}
	/**
	 * アクティビティに統計情報をセットする
	 * 
	 * @param activity
	 * @param plusOners
	 * @return
	 */
	public PlusActivity setDistributionInfo(PlusActivity activity,
			Map<String, Integer> numOfPlusOneMap) {
		int firstLookers = 0, lowMiddleLookers = 0, highMiddleLookers = 0, highLookers = 0;
		for (String plusOneId : activity.getPlusOnerIds()) {
			int numOfPlusOne = numOfPlusOneMap.get(plusOneId);
			if (numOfPlusOne > Distribution.HIGH_LOOKER.getThreshold()) {
				highLookers += 1;
			} else if (numOfPlusOne > Distribution.HIGH_MIDDLE_LOOKER
					.getThreshold()) {
				highMiddleLookers += 1;
			} else if (numOfPlusOne > Distribution.LOW_MIDDLE_LOOKER
					.getThreshold()) {
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
	 * @return
	 */
	private Query getActivityQuery(String actorId) {

		return new Query(Activity.KIND.val).setFilter(new FilterPredicate(
				Activity.actorId.val, FilterOperator.EQUAL, actorId));
	}

}
