package jp.leopanda.gPlusAnalytics.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
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
	DataStoreHandler dataHandler = new DataStoreHandler();
	/**
	 * データストアの内容を最新状態に更新する
	 * @param userId
	 * @param oAuthToken
	 * @return
	 * @throws HostGateException
	 */
	public String updateDataStore(String userId,String oAuthToken,boolean forceUpdate) throws HostGateException{
		dataHandler.setForceUpdate(forceUpdate);
		return dataHandler.updateBrandNew(userId, oAuthToken, googleApi);
	}
	/**
	 * データストアからアクテビティの一覧を取得する
	 * 
	 * @param userId
	 * @param oAuthToken
	 * @return
	 * @throws HostGateException
	 */
	public List<PlusActivity> getActivities(String userId, String oAuthToken)
			throws HostGateException {
		return dataHandler.getActivityHanler().getActivities(userId);
	}
	/**
	 * データストアからユーザのアクテビティに＋１したすべてのユーザの一覧を取得する
	 * @param userId
	 * @param oAuthToken
	 * @return
	 * @throws HostGateException
	 */
	public List<PlusPeople> getPlusOners(String userId, String oAuthToken)
			throws HostGateException {
		return dataHandler.getPlusOnerHanler().getPlusOners(userId);
	}
	/**
	 * データストアをクリアする
	 * @param userid
	 */
	public String clearDataStore(String userid){
		dataHandler.clearDataStore(userid);
		return "done";
	}
}
