package jp.leopanda.gPlusAnalytics.dataStore;

import java.util.ArrayList;
import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/**
 * @author LeoPanda
 *
 */
public class PlusOnerStoreHandler {
	DatastoreService ds;
	public PlusOnerStoreHandler(DatastoreService ds) {
		this.ds = ds;
	}
	/**
	 * Idリストに含まれない新規＋１ユーザーでデータストアをカウントアップする
	 * 
	 * @param actorId
	 * @param oldPlusOnerIds
	 * @param newPlusOners
	 */
	public void updateNewPlusOners(String actorId, List<String> oldPlusOnerIds,
			List<PlusPeople> newPlusOners) {
		for (PlusPeople newPlusOner : newPlusOners)
			if (!oldPlusOnerIds.contains(newPlusOner.getId())) {
				updatePlusOner(actorId, newPlusOner);
			}
	}
	/**
	 * 特定＋１ユーザーエンティティの+1数をカウントアップする データストアにユーザーがいなければ新規に作成する
	 * 
	 * @param plusOnerId
	 */
	private void updatePlusOner(String actorId, PlusPeople plusOner) {
		Filter idFilter = new FilterPredicate(PlusOner.id.val,
				FilterOperator.EQUAL, plusOner.getId());
		Filter actorIdFilter = new FilterPredicate(PlusOner.actorId.val,
				FilterOperator.EQUAL, actorId);

		PreparedQuery pq = ds
				.prepare(new Query(PlusOner.KIND.val)
						.setFilter(CompositeFilterOperator.and(idFilter,
								actorIdFilter)));
		List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(1));
		if (entities.size() == 0) {
			putPlusOners(plusOner, actorId, 1);
		} else {
			Entity entity = entities.get(0);
			Integer numOfPlusOne = (int) (long) entity
					.getProperty(PlusOner.numOfPlusOne.val);
			entity.setProperty(PlusOner.numOfPlusOne.val, numOfPlusOne + 1);
			ds.put(entity);
		}
	}
	/**
	 * データストアへPlusOners entityを書き込む
	 * 
	 * @param plusPeople
	 * @param numOfPlusOne
	 */
	public void putPlusOners(PlusPeople plusPeople, String actorId,
			int numOfPlusOne) {
		Entity entity = new Entity(PlusOner.KIND.val);
		entity.setProperty(PlusOner.actorId.val, actorId);
		entity.setProperty(PlusOner.title.val, plusPeople.getTitle());
		entity.setProperty(PlusOner.id.val, plusPeople.getId());
		entity.setProperty(PlusOner.url.val, plusPeople.getUrl());
		entity.setProperty(PlusOner.displayName.val,
				plusPeople.getDisplayName());
		entity.setProperty(PlusOner.imageUrl.val, plusPeople.getImageUrl());
		entity.setProperty(PlusOner.numOfPlusOne.val, numOfPlusOne);
		ds.put(entity);
	}

	/**
	 * 　+1ersエンティティのリストをデータオブジェクト形式で返す
	 * 
	 * @param actorId
	 * @return
	 */
	public List<PlusPeople> getPlusOners(String actorId) {
		List<PlusPeople> results = new ArrayList<PlusPeople>();
		Filter actorFilter = new FilterPredicate(PlusOner.actorId.val,
				FilterOperator.EQUAL, actorId);
		PreparedQuery pq = ds.prepare(new Query(PlusOner.KIND.val)
				.setFilter(actorFilter));
		for (Entity entity : pq.asIterable()) {
			results.add(setStoreToPlusPeople(entity));
		}
		return results;
	}
	/**
	 * +1erエンティティをデータオブジェクト形式で返す
	 * @param actorId
	 * @param plusOneId
	 * @return
	 */
	public PlusPeople getPluOner(String actorId,String plusOneId){
		Filter idFilter = new FilterPredicate(PlusOner.id.val,
				FilterOperator.EQUAL,plusOneId);
		Filter actorIdFilter = new FilterPredicate(PlusOner.actorId.val,
				FilterOperator.EQUAL, actorId);
		PreparedQuery pq = ds
				.prepare(new Query(PlusOner.KIND.val)
						.setFilter(CompositeFilterOperator.and(idFilter,
								actorIdFilter)));
		List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(1));
		if(entities.size() == 0 ){
			return null;
		}else{
			return setStoreToPlusPeople(entities.get(0));
		}
	}
	/**
	 * データオストアの内容をデータオブジェクトへ転記する
	 * @param entity
	 * @return
	 */
	private PlusPeople setStoreToPlusPeople(Entity entity) {
		PlusPeople result = new PlusPeople();
		result.setId((String) entity.getProperty(PlusOner.id.val));
		result.setTitle((String) entity.getProperty(PlusOner.title.val));
		result.setUrl((String) entity.getProperty(PlusOner.url.val));
		result.setImageUlr((String) entity
				.getProperty(PlusOner.imageUrl.val));
		result.setNumOfPlusOne((int) (long) entity
				.getProperty(PlusOner.numOfPlusOne.val));
		result.setDisplayName((String) entity
				.getProperty(PlusOner.displayName.val));
		return result;
	}
	/**
	 * エンティティを削除する
	 * 
	 * @param actorId
	 */
	public void remove(String actorId) {
		Filter actorFilter = new FilterPredicate(Activity.actorId.val,
				FilterOperator.EQUAL, actorId);
		PreparedQuery pq = ds.prepare(new Query(PlusOner.KIND.val)
				.setFilter(actorFilter));
		for (Entity entity : pq.asIterable()) {
			ds.delete(entity.getKey());
		}
	}
}
