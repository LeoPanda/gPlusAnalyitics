package jp.leopanda.gPlusAnalytics.dataStore;

import java.io.IOException;
import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;
import jp.leopanda.gPlusAnalytics.server.GoogleApiService;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import java.util.logging.Logger;
/**
 * データストア操作サービス
 * 
 * @author LeoPanda
 *
 */
public class DataStoreHandler {

	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	ActivityStoreHandler activityHandler = new ActivityStoreHandler(ds);
	GoogleApiService googleApi;
	public DataStoreHandler(GoogleApiService googleApi) {
		this.googleApi = googleApi;
	}

	public ActivityStoreHandler getActivityHanler() {
		return this.activityHandler;
	}
	Logger logger = Logger.getLogger("DataHandler");

	/**
	 * データの初期ロード
	 * 
	 * @param userId
	 * @param oAuthToken
	 * @return
	 * @throws HostGateException
	 * @throws IOException
	 */
	public String initialLoadToStore(String userId, String oAuthToken)
			throws HostGateException {
		if (activityHandler.getLatestActivityPublished(userId) != null) {
			throw new HostGateException(
					"データストアがすでに存在しています。初期化するためにはいったんデータストアを消去してください。");
		}
		List<PlusActivity> activities = googleApi.getPlusActivity(userId,
				oAuthToken);
		for (PlusActivity activity : activities) {
			if (activityFilter(activity)) {
				continue;
			}
			activityHandler
					.putActivity(activity, googleApi.getPlusOnersByActivity(
							activity.getId(), oAuthToken), null);
		}
		return null;
	}
	/**
	 * データストアをクリアする
	 * 
	 * @param actorId
	 */
	public void clearDataStore(String actorId) {
		activityHandler.remove(actorId);
	}

	/**
	 * G+APIとデータストアのactivityを比較し、データストアに無いデータを追加する。 +1数に差異がある場合はG+APIデータで置き換える
	 * 
	 * @param userId
	 * @param oAuthToken
	 * @param googleApi
	 * @throws HostGateException
	 */
	public String updateBrandNew(String userId, String oAuthToken)
			throws HostGateException {
		List<PlusActivity> activities = googleApi.getPlusActivity(userId,
				oAuthToken);
		ActivityCheckMap checkMap = activityHandler.getActivityCheckMap(userId);
		for (PlusActivity activity : activities) {
			if (activityFilter(activity)) {
				continue;
			}
			Integer numOfPlusOne = checkMap.getNumOfPlusOne(activity.getId());
			if (numOfPlusOne == null) {// データストアになければ新規作成
				activityHandler.putActivity(activity, googleApi
						.getPlusOnersByActivity(activity.getId(), oAuthToken),
						null);
			} else if (numOfPlusOne != activity.getNumOfPlusOners()) {// +1数が違えばアップデート
				activityHandler.putActivity(activity, googleApi
						.getPlusOnersByActivity(activity.getId(), oAuthToken),
						checkMap.getEntityKey(activity.getId()));
			}
		}
		return null;
	}
	/**
	 * 写真投稿以外のアクティビティを除外する
	 * 
	 * @param activity
	 * @return
	 */
	private boolean activityFilter(PlusActivity activity) {
		return (activity.getAttachmentImageUrls() == null)
				|| (activity.getAttachmentImageUrls().get(0) == null);
	}

}
