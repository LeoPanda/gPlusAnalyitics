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

  public ActivityStoreHandler getActivityHandler() {
    return this.activityHandler;
  }

  Logger logger = Logger.getLogger("DataHandler");

  /**
   * @throws IOException Google APIをコールしデータストアに初期データを読み込む
   * 
   * @param userId アクティビティのオーナーID @param oauthToken google認証トークン @return 常にNull(gwt
   * RPCのインターフェースに同期するため) @throws HostGateException 処理例外スロー @throws
   */
  public String initialLoadToStore(String userId, GoogleApiService googleApi)
      throws HostGateException, IOException {
    if (activityHandler.getLatestActivityPublished(userId) != null) {
      throw new HostGateException("データストアがすでに存在しています。初期化するためにはいったんデータストアを消去してください。");
    }
    List<PlusActivity> activities = googleApi.getPlusActivity(userId);
    for (PlusActivity activity : activities) {
      if (activityFilter(activity)) {
        continue;
      }
      activityHandler.putActivity(activity,
          googleApi.getPlusOnersByActivity(activity.getId()), null);
    }
    return null;
  }

  /**
   * データストアをクリアする
   * 
   * @param actorId
   *          アクティビティのオーナーID
   */
  public void clearDataStore(String actorId) {
    activityHandler.remove(actorId);
  }

  /**
   * G+APIとデータストアのactivityを比較し、データストアに無いデータを追加する。 +1数に差異がある場合はG+APIデータで置き換える
   * 
   * @param userId
   *          アクティビティのオーナーID
   * @param oauthToken
   *          google認証トークン
   * @throws HostGateException
   *           処理例外スロー
   * @throws IOException
   */
  public String updateBrandNew(String userId, GoogleApiService googleApi)
      throws HostGateException, IOException {
    List<PlusActivity> activities = googleApi.getPlusActivity(userId);
    ActivityCheckMap checkMap = activityHandler.getActivityCheckMap(userId);
    for (PlusActivity activity : activities) {
      if (activityFilter(activity)) {
        continue;
      }
      Integer numOfPlusOne = checkMap.getNumOfPlusOne(activity.getId());
      if (numOfPlusOne == null) { // データストアになければ新規作成
        activityHandler.putActivity(activity,
            googleApi.getPlusOnersByActivity(activity.getId()), null);
      } else if (!numOfPlusOne.equals(activity.getNumOfPlusOners())) { // +1数が違えばアップデート
        activityHandler.putActivity(activity,
            googleApi.getPlusOnersByActivity(activity.getId()),
            checkMap.getEntityKey(activity.getId()));
      }
    }
    return null;
  }

  /**
   * 写真投稿以外のアクティビティを除外する
   * 
   * @param activity
   *          チェック対象のアクティビティアイテムオブジェクト
   * @return アイテムに写真投稿がない場合にtrue
   */
  private boolean activityFilter(PlusActivity activity) {
    if (activity.getAttachmentImageUrls() == null) {
      return true;
    } else if (activity.getAttachmentImageUrls().size() == 0){
      return true;
    } else if (activity.getAttachmentImageUrls().get(0) == null) {
      return true;
    }
    return false;
  }

}
