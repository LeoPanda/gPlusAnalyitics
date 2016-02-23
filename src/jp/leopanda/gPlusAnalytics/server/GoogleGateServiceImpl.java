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
public class GoogleGateServiceImpl extends RemoteServiceServlet
		implements
			GoogleGateService {
	private static final long serialVersionUID = 1L;

	GoogleApiService googleApi = new GoogleApiService();
	DataStoreHandler dataHandler = new DataStoreHandler(googleApi);
	/**
	 * データストアの内容を最新状態に更新する
	 * @param userId
	 * @param oAuthToken
	 * @return
	 * @throws HostGateException
	 */
	public String updateDataStore(String userId,String oAuthToken) throws HostGateException{
		return dataHandler.updateBrandNew(userId, oAuthToken);
	}
	/**
	 * データストアをクリアする
	 * @param userid
	 */
	public String clearDataStore(String userid){
		dataHandler.clearDataStore(userid);
		return null;
	}
	/**
	 * データストアの初期ロード
	 * @param userId
	 * @param oAuthToken
	 * @return
	 * @throws HostGateException
	 */
	public String initialLoadToStore(String userId,String oAuthToken) throws HostGateException{
		return dataHandler.initialLoadToStore(userId, oAuthToken);
	}
	/**
	 * データストアからactivityとplusOnersを得る
	 * @param userId
	 * @return
	 * @throws HostGateException
	 */
	public ResultPack getItems(String userId) throws HostGateException{
		return dataHandler.getActivityHanler().getItems(userId);
	}
}
