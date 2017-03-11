package jp.leopanda.gPlusAnalytics.client.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * +1ersをフィルタする
 * 
 * @author LeoPanda
 *
 */
public class FilterPlusOners {

  /**
   * アクテビティでフィルタする
   * 
   * @param plusOners
   * @param activity
   * @return
   */
  public List<PlusPeople> byActivity(List<PlusPeople> plusOners, PlusActivity activity) {
    return new ItemFilter<PlusPeople, PlusActivity>() {
      @Override
      public boolean compare(PlusPeople plusPeople, PlusActivity activity) {
        return activity.getPlusOnerIds().contains(plusPeople.getId());
      }
    }.doFilter(plusOners, activity);
  }

  /**
   * キーワードでフィルタする
   * 
   * @param plusOners
   * @param keyword
   * @return
   */
  public List<PlusPeople> byKeyword(List<PlusPeople> plusOners, String keyword) {
    return new ItemFilter<PlusPeople, String>() {
      @Override
      public boolean compare(PlusPeople item, String keyword) {
        return item.getDisplayName().contains(keyword);
      }
    }.doFilter(plusOners, keyword);
  }

  /**
   * アクテビティリストに+1したすべてのユーザーを抽出する
   * 
   * @param plusOners
   * @param plusActivities
   * @return
   */
  public List<PlusPeople> byActivies(List<PlusPeople> plusOners,
      List<PlusActivity> plusActivities) {
    Set<String> plusOnerIds = new HashSet<String>();
    for (PlusActivity plusActivity : plusActivities) {
      for (String plusOnerId : plusActivity.getPlusOnerIds()) {
        plusOnerIds.add(plusOnerId);
      }
    }
    return byPlusOnerIds(plusOners, plusOnerIds);
  }

  /**
   * +1erのIDセットでフィルタする
   * 
   * @param plusOners
   * @param plusOnerIds
   * @return
   */
  private List<PlusPeople> byPlusOnerIds(List<PlusPeople> plusOners, Set<String> plusOnerIds) {
    return new ItemFilter<PlusPeople, Set<String>>() {
      @Override
      public boolean compare(PlusPeople sourceItem, Set<String> plusOnerIds) {
        return plusOnerIds.contains(sourceItem.getId());
      }
    }.doFilter(plusOners, plusOnerIds);
  }
}
