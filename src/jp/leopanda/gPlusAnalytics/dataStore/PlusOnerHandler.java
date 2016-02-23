package jp.leopanda.gPlusAnalytics.dataStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * PlusOnersのハンドリングを行う
 * @author LeoPanda
 *
 */
public class PlusOnerHandler {
	/**
	 * Activityに＋１したユーザーを集計する
	 * 
	 * @param plusOners
	 */
	Map<String, PlusPeople> plusOnersMap = new HashMap<String, PlusPeople>();
	Map<String, Integer> numOfPlusOneMap = new HashMap<String, Integer>();
	
	public Map<String, Integer> getNumOfPlusOneMap(){
		return numOfPlusOneMap;
	}

	public void aggregatePlusOnes(List<PlusPeople> plusOners) {
		for (PlusPeople plusOner : plusOners) {
			String plusOneId = plusOner.getId();
			if (plusOnersMap.get(plusOneId) == null) {
				plusOnersMap.put(plusOneId, plusOner);
				numOfPlusOneMap.put(plusOneId, 1);
			} else {
				int numOfPlusOne = numOfPlusOneMap.get(plusOneId);
				numOfPlusOneMap.put(plusOneId,
						numOfPlusOne + 1);
			}
		}
	}
	/**
	 * Activityに＋１したユーザーをを抽出する
	 * 
	 * @return
	 */
	public List<PlusPeople> getPlusOners() {
		List<PlusPeople> results = new ArrayList<PlusPeople>();
		for (PlusPeople plusOner : plusOnersMap.values()) {
			plusOner.setNumOfPlusOne(numOfPlusOneMap.get(plusOner.getId()));
			results.add(plusOner);
		}
		return results;
	}

}
