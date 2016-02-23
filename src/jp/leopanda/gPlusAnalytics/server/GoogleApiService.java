package jp.leopanda.gPlusAnalytics.server;

import java.util.List;

import jp.leopanda.common.server.UrlService;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivityList;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeopleList;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;

/**
 * Google+ REST APIを呼び出すためのサーバーサイドコンポーネント
 * 
 * @author LeoPanda
 *
 */
public class GoogleApiService {
	UrlService urlService = new UrlService();
	/**
	 * Google+ 特定ユーザーのすべてのアクテビティリストを取得
	 * 
	 * @param userId
	 * @param oAuthToken
	 * @return
	 * @throws HostGateException
	 */
	public List<PlusActivity> getPlusActivity(String userId, String oAuthToken)
			throws HostGateException {
		String uri = "https://www.googleapis.com/plus/v1/people/" + userId
				+ "/activities/public"
				+"?fields=" + ClassInfo.getFiledNames(PlusActivityList.class);
		return new JsonDecoder<PlusActivityList, PlusActivity>(uri, oAuthToken) {
		}.decode(PlusActivityList.class).getItems();
	}
	/**
	 * 単一のアクティビティへ +1 したユーザーの一覧を取得
	 * 
	 * @param activityId
	 * @param oAuthToken
	 * @return
	 * @throws HostGateException
	 */
	public List<PlusPeople> getPlusOnersByActivity(String activityId,
			String oAuthToken) throws HostGateException {
		String uri = "https://www.googleapis.com/plus/v1/activities/"
				+ activityId + "/people/plusoners"
		+"?fields=" + ClassInfo.getFiledNames(PlusPeopleList.class);
		return new JsonDecoder<PlusPeopleList, PlusPeople>(uri, oAuthToken) {
		}.decode(PlusPeopleList.class).getItems();
	}

}
