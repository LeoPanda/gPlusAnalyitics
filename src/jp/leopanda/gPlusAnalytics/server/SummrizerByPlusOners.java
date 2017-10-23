package jp.leopanda.gPlusAnalytics.server;

import java.util.HashMap;
import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * +1er毎に+1数を集計するカウンター Map<+1erId,+1数>
 * 
 * @author LeoPanda
 *
 */
public class SummrizerByPlusOners extends HashMap<String, Integer> {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * 全アクテビティの＋１数を+1er毎に集計する
   * 
   * @param sourceActivities
   */
  public SummrizerByPlusOners aggregatePlusOneCount(List<PlusActivity> sourceActivities) {
    for (PlusActivity activity : sourceActivities) {
      for (String plusOnerId : activity.getPlusOnerIds()) {
        countPlusOne(plusOnerId);
      }
    }
    return this;
  }

  /**
   * PlusOner毎に＋１数をカウントする
   * 
   * @param plusOnerId
   */
  private void countPlusOne(String plusOnerId) {
    if (this.containsKey(plusOnerId)) {
      this.put(plusOnerId, this.get(plusOnerId) + 1);
    } else {
      this.put(plusOnerId, 1);
    }
  }
}
