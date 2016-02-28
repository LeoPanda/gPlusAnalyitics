package jp.leopanda.gPlusAnalytics.server;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import jp.leopanda.gPlusAnalytics.dataObject.ResultPack;
import jp.leopanda.gPlusAnalytics.dataStore.DataStoreHandler;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateService;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;

/**
 * Google+ REST APIを呼び出すためのサーバーサイドコンポーネント
 * 
 * @author LeoPanda
 *
 */
public class GoogleGateServiceImpl extends RemoteServiceServlet implements GoogleGateService {
  private static final long serialVersionUID = 1L;

  GoogleApiService googleApi = new GoogleApiService();
  DataStoreHandler dataHandler = new DataStoreHandler(googleApi);

  /**
   * データストアの内容を最新状態に更新する
   * 
   * @param userId アクティビティのオーナーID
   * @param oauthToken google認証トークン
   * @return 常にnull
   * @throws HostGateException 処理例外スロー
   */
  public String updateDataStore(String userId, String oauthToken) throws HostGateException {
    return dataHandler.updateBrandNew(userId, oauthToken);
  }

  /**
   * データストアをクリアする
   * 
   * @param userid アクティビティのオーナーID
   */
  public String clearDataStore(String userid) {
    dataHandler.clearDataStore(userid);
    return null;
  }

  /**
   * データストアの初期ロード
   * 
   * @param userId アクティビティのオーナーID
   * @param oauthToken google+認証トークン
   * @return 常にnull
   * @throws HostGateException 処理例外スロー
   */
  public String initialLoadToStore(String userId, String oauthToken) throws HostGateException {
    return dataHandler.initialLoadToStore(userId, oauthToken);
  }

  /**
   * データストアからactivityとplusOnersを得る
   * 
   * @param userId アクティビティのオーナーID
   * @return アイテムオブジェクトリストのパック
   * @throws HostGateException 処理例外スロー
   */
  public ResultPack getItems(String userId) throws HostGateException {
    return dataHandler.getActivityHanler().getItems(userId);
  }
}
