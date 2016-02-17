package jp.leopanda.gPlusAnalytics.dataStore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;
import jp.leopanda.gPlusAnalytics.server.GoogleApiService;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import java.util.Calendar;
/**
 * データストア操作サービス
 * 
 * @author LeoPanda
 *
 */
public class DataStoreHandler {

	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

	ActivityStoreHandler activityHandler = new ActivityStoreHandler(ds);
	PlusOnerStoreHandler plusOnerHandler = new PlusOnerStoreHandler(ds);
	
	boolean forceUpdate = false;
	/**
	 * 更新条件を無視しAPIデータをすべて読みなおしてデータストアを強制的に更新させるように設定を変更する
	 */
	public void setForceUpdate(boolean forceUpdate){
		this.forceUpdate = forceUpdate;
	}

	public ActivityStoreHandler getActivityHanler() {
		return this.activityHandler;
	}
	public PlusOnerStoreHandler getPlusOnerHanler() {
		return this.plusOnerHandler;
	}
	/**
	 * データストア上にある最後のアクテビティの作成日を基準に それより５日前から現在までの最新のデータをAPIから読み込み
	 * データストアの内容を更新する。
	 * 
	 * @param userId
	 * @param oAuthToken
	 * @param googleApi
	 * @throws HostGateException
	 */
	public String updateBrandNew(String userId, String oAuthToken,
			GoogleApiService googleApi) throws HostGateException {
		List<PlusActivity> latestActivites = googleApi.getLatestActivity(
				userId,
				backDate(activityHandler.getLatestActivityPublished(), 5),
				oAuthToken);
		checkUpdates(userId, oAuthToken, googleApi, latestActivites);
		return activityHandler.getLatestActivityPublished().toString();
	}
	/**
	 * データストアをクリアする
	 * 
	 * @param actorId
	 */
	public void clearDataStore(String actorId) {
		activityHandler.remove(actorId);
		plusOnerHandler.remove(actorId);
	}
	/**
	 * 日付を指定日分バックデートする
	 * 
	 * @param refDate
	 * @param backDays
	 * @return
	 */
	private Date backDate(Date refDate, int backdays) {
		if (refDate == null) {
			return null;
		}
		if(forceUpdate){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(refDate);
		cal.add(Calendar.DATE, (-1 * backdays));
		return cal.getTime();
	}
	/**
	 * ユーザーのアクテビティに変更があったかどうか走査する
	 * 
	 * @param userId
	 * @param oAuthToken
	 * @param googleApi
	 * @param latestActivites
	 * @throws HostGateException
	 */
	private void checkUpdates(String userId, String oAuthToken,
			GoogleApiService googleApi, List<PlusActivity> latestActivites)
			throws HostGateException {
		for (PlusActivity newActivity : latestActivites) {
			findUpdateEntities(newActivity, googleApi.getPlusOnersByActivity(
					newActivity.getId(), oAuthToken));
		};
	}
	/**
	 * 新規のアクテビティあればデータストアへ追加する。 既存のアクテビティの＋１数が更新されていればデータストアを更新する。
	 * 
	 * 
	 * @param newActivity
	 * @param newPlusOners
	 */
	private void findUpdateEntities(PlusActivity newActivity,
			List<PlusPeople> newPlusOners) {
		Filter activityFilter = new FilterPredicate(Activity.id.val,
				FilterOperator.EQUAL, newActivity.getId());
		PreparedQuery pq = ds.prepare(new Query(Activity.KIND.val)
				.setFilter(activityFilter));
		Entity activityEntity = pq.asSingleEntity();

		if (activityEntity == null) {
			// データストアの新規追加
			plusOnerHandler.updateNewPlusOners(newActivity.getActorId(),
					new ArrayList<String>(), newPlusOners);
			activityHandler.putActivity(newActivity, newPlusOners,
					plusOnerHandler);
		} else {
			// +1数に変更があった時は更新する
			Integer oldNumOfPlusOners = (int) (long) activityEntity
					.getProperty(Activity.numOfPlusOners.val);
			if (!oldNumOfPlusOners.equals(newActivity.getNumOfPlusOners())) {
				doUpdate(newActivity, newPlusOners, activityEntity);
			} else if(forceUpdate){
				doUpdate(newActivity, newPlusOners, activityEntity);				
			}
		}
	}
	/**
	 * データストアの更新
	 * 
	 * @param newActivity
	 * @param newPlusOners
	 * @param activityEntity
	 */
	private void doUpdate(PlusActivity newActivity,
			List<PlusPeople> newPlusOners, Entity activityEntity) {
		// 新規+1ersでデータストアの+1erエンティティを更新
		String actorId = (String) activityEntity
				.getProperty(Activity.actorId.val);
		@SuppressWarnings("unchecked")
		List<String> oldPlusOnerIds = (List<String>) activityEntity
				.getProperty(Activity.plusOnerIds.val);
		plusOnerHandler.updateNewPlusOners(actorId, oldPlusOnerIds,
				newPlusOners);
		// Activityエンティティの+1er分布情報を更新
		ds.put(activityHandler.setDistributionInfo(newActivity, newPlusOners,
				activityEntity, plusOnerHandler));
	}
}
