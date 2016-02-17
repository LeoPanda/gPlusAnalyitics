package jp.leopanda.gPlusAnalytics.dataStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
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
	/**
	 * アクテビティデータストアの+1er分布情報をセットする
	 * 
	 * @param newActivity
	 * @param newPlusOners
	 * @param activityEntity
	 * @return
	 */
	public Entity setDistributionInfo(PlusActivity newActivity,
			List<PlusPeople> newPlusOners, Entity activityEntity,
			PlusOnerStoreHandler plusOneHandler) {
		activityEntity.setProperty(Activity.plusOnerIds.val,
				getPlusOnerIds(newPlusOners));
		activityEntity.setProperty(Activity.numOfPlusOners.val,
				newActivity.getNumOfPlusOners());
		int firstLooker = 0, lowMiddleLooker = 0, highMiddleLooker = 0, highLooker = 0;

		for (PlusPeople plusOner : newPlusOners) {
			int plusOne = plusOneHandler.getPluOner(newActivity.getActorId(),
					plusOner.getId()).getNumOfPlusOne();
			if (plusOne >= Distribution.HIGH_LOOKER.getThreshold()) {
				highLooker += 1;
			} else if (plusOne >= Distribution.HIGH_MIDDLE_LOOKER
					.getThreshold()) {
				highMiddleLooker += 1;
			} else if (plusOne >= Distribution.LOW_MIDDLE_LOOKER.getThreshold()) {
				lowMiddleLooker += 1;
			} else {
				firstLooker += 1;
			}
		}
		activityEntity.setProperty(Activity.highLookers.val, highLooker);
		activityEntity.setProperty(Activity.highMiddleLookers.val,
				highMiddleLooker);
		activityEntity.setProperty(Activity.lowMiddleLookers.val,
				lowMiddleLooker);
		activityEntity.setProperty(Activity.firstLookers.val, firstLooker);
		return activityEntity;
	}
	/**
	 * データストアへActivity Entityを書き込む
	 * 
	 * @param activity
	 * @param plusOners
	 */
	public void putActivity(PlusActivity activity, List<PlusPeople> plusOners,
			PlusOnerStoreHandler plusOneHandler) {
		if (activity.getAttachmentImageUrls().size() == 0) {
			return;
		} else if (activity.getAttachmentImageUrls().get(0) == null) {
			return;
		};
		Entity entity = new Entity(Activity.KIND.val);
		entity.setProperty(Activity.title.val, activity.getTitle());
		entity.setProperty(Activity.id.val, activity.getId());
		entity.setProperty(Activity.actorId.val, activity.getActorId());
		entity.setProperty(Activity.url.val, activity.getUrl());
		entity.setProperty(Activity.published.val, activity.getPublished());
		entity.setProperty(Activity.updated.val, activity.getUpdated());
		entity.setProperty(Activity.itemObjectContent.val,
				activity.getItemOjbectContent());
		entity.setProperty(Activity.attachmentImageUrls.val,
				activity.getAttachmentImageUrls());
		entity.setProperty(Activity.accessDescription.val,
				activity.getAccessDescription());
		entity = setDistributionInfo(activity, plusOners, entity,
				plusOneHandler);
		ds.put(entity);
	}
	/**
	 * 　アクテビティエンティティをデータオブジェクト形式で返す
	 * 
	 * @param actorId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PlusActivity> getActivities(String actorId) {
		List<PlusActivity> results = new ArrayList<PlusActivity>();
		Filter actorFilter = new FilterPredicate(Activity.actorId.val,
				FilterOperator.EQUAL, actorId);
		PreparedQuery pq = ds.prepare(new Query(Activity.KIND.val)
				.setFilter(actorFilter));
		for (Entity entity : pq.asIterable()) {
			PlusActivity result = new PlusActivity();
			result.setId((String) entity.getProperty(Activity.id.val));
			String title = (String) entity.getProperty(Activity.title.val);
			result.setTitle(title);
			result.setActorId((String) entity.getProperty(Activity.actorId.val));
			result.setUrl((String) entity.getProperty(Activity.url.val));
			result.setUpdated((Date) entity.getProperty(Activity.updated.val));
			result.setPublished((Date) entity
					.getProperty(Activity.published.val));
			result.setItemObjectContent((String) entity
					.getProperty(Activity.itemObjectContent.val));
			result.setAccessDescription((String) entity
					.getProperty(Activity.accessDescription.val));
			result.setAttacimentImageUrls((List<String>) entity
					.getProperty(Activity.attachmentImageUrls.val));
			result.setNumOfPlusOners((int) (long) entity
					.getProperty(Activity.numOfPlusOners.val));
			result.setFirstLookers((int) (long) entity
					.getProperty(Activity.firstLookers.val));
			result.setLowMiddleLookers((int) (long) entity
					.getProperty(Activity.lowMiddleLookers.val));
			result.setHighMiddleLookers((int) (long) entity
					.getProperty(Activity.highMiddleLookers.val));
			result.setHighLookers((int) (long) entity
					.getProperty(Activity.highLookers.val));
			results.add(result);
		}
		Collections.sort(results, new Comparator<PlusActivity>() {
			@Override
			public int compare(PlusActivity o1, PlusActivity o2) {
				return o2.getPublished().compareTo(o1.getPublished());
			}
		});
		return results;
	}
	/**
	 * エンティティを削除する
	 * 
	 * @param actorId
	 */
	public void remove(String actorId) {
		Filter actorFilter = new FilterPredicate(Activity.actorId.val,
				FilterOperator.EQUAL, actorId);
		PreparedQuery pq = ds.prepare(new Query(Activity.KIND.val)
				.setFilter(actorFilter));
		for (Entity entity : pq.asIterable()) {
			ds.delete(entity.getKey());
		}
	}
	/**
	 * データストア上の最新のアクテビティエンティティの作成日付を返す
	 * 
	 * @return
	 */
	public Date getLatestActivityPublished() {
		PreparedQuery pq = ds.prepare(new Query(Activity.KIND.val).addSort(
				Activity.published.val, SortDirection.DESCENDING));
		List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(1));
		if (entities.size() == 0) {
			return null;
		}
		return (Date) entities.get(0).getProperty(Activity.published.val);
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

}
