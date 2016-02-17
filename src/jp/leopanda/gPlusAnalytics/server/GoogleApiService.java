package jp.leopanda.gPlusAnalytics.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
				+ "/activities/public";
		return new JsonDecoder<PlusActivityList, PlusActivity>(uri, oAuthToken) {
		}.decode(PlusActivityList.class).getItems();
	}
	/**
	 * 基準日付以降の更新日を持つアクテビティを取得
	 * 
	 * @param userId
	 * @param oAuthToken
	 * @param nowDate
	 * @return
	 * @throws HostGateException
	 */
	public List<PlusActivity> getLatestActivity(String userId, Date nowDate,
			String oAuthToken) throws HostGateException {
		String uri = "https://www.googleapis.com/plus/v1/people/" + userId
				+ "/activities/public";
		List<PlusActivity> laterPageActivities = new JsonDecoder<PlusActivityList, PlusActivity>(
				uri, oAuthToken) {
		}.decodeLaterUpdatedPage(PlusActivityList.class, nowDate).getItems();
		if (nowDate == null) {
			return laterPageActivities;
		}
		List<PlusActivity> results = new ArrayList<PlusActivity>();
		for (PlusActivity activity : laterPageActivities) {
			if (activity.getPublished().compareTo(nowDate) > 0) {
				results.add(activity);
			}
		}
		return results;
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
				+ activityId + "/people/plusoners";
		return new JsonDecoder<PlusPeopleList, PlusPeople>(uri, oAuthToken) {
		}.decode(PlusPeopleList.class).getItems();
	}
	/**
	 * 特定ユーザーのアクテビティに＋１したユーザーすべての一覧を取得
	 * 
	 * @param userId
	 * @param oAuthToken
	 * @return
	 * @throws HostGateException
	 */
	public List<PlusPeople> getPlusOners(String userId, String oAuthToken)
			throws HostGateException {
		List<PlusPeople> results = new ArrayList<PlusPeople>();
		List<PlusActivity> activitys = getPlusActivity(userId, oAuthToken);
		for (PlusActivity plusActivity : activitys) {
			results.addAll(getPlusOnersByActivity(plusActivity.getId(),
					oAuthToken));
		}
		return SortPeople(results);
	}
	/**
	 * PlusPeopleをuserId順にソートし、重複を合算する
	 * 
	 * @param peopleList
	 * @return
	 */
	private List<PlusPeople> SortPeople(List<PlusPeople> peopleList) {
		List<PlusPeople> results = new ArrayList<PlusPeople>();
		Collections.sort(peopleList, new Comparator<PlusPeople>() {
			@Override
			public int compare(PlusPeople o1, PlusPeople o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		int plusOne = 1;
		PlusPeople stack = peopleList.get(0);
		String userId = stack.getId();
		for (PlusPeople plusPeople : peopleList) {
			if (plusPeople.getId().equals(userId)) {
				plusOne += 1;
			} else {
				stack.setNumOfPlusOne(plusOne);
				results.add(stack);
				stack = plusPeople;
				userId = stack.getId();
				plusOne = 1;
			}
		}
		stack.setNumOfPlusOne(plusOne);
		results.add(stack);
		return results;
	}

}
