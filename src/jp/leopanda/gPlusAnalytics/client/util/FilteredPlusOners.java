package jp.leopanda.gPlusAnalytics.client.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * +1ersをフィルタする
 * 
 * @author LeoPanda
 *
 */
public class FilteredPlusOners extends ItemFilter<PlusPeople> {

  /**
   * @param sourceItems
   */
  public FilteredPlusOners(List<PlusPeople> sourceItems) {
    super(sourceItems);
  }

  /**
   * アクテビティでフィルタする
   * 
   * @param plusOners
   * @param activity
   * @return
   */
  public List<PlusPeople> byActivity(PlusActivity activity) {
    return doFilter(
        plusPeople -> activity.getPlusOnerIds().contains(plusPeople.getId()));
  }

  /**
   * キーワードでフィルタする
   * 
   * @param plusOners
   * @param keyword
   * @return
   */
  public List<PlusPeople> byKeyword(String keyword) {
    return doFilter(
        plusPeople -> plusPeople.getDisplayName().contains(keyword));
  }

  /**
   * アクテビティリストに+1したすべてのユーザーを抽出する
   * 
   * @param plusOners
   * @param plusActivities
   * @return
   */
  public List<PlusPeople> byActivies(List<PlusActivity> plusActivities) {
   Set<String> plusOnerIds =
        plusActivities.stream().flatMap(activity -> activity.getPlusOnerIds().stream())
            .collect(Collectors.toSet());
    return doFilter(plusPeople -> plusOnerIds.contains(plusPeople.getId()));
  }

  /**
   * +1数でフィルタする
   * 
   * @param plusOners
   * @param keyword
   * @return
   */
  public List<PlusPeople> byNumOfPlusOne(NumOfPlusOneFilterKeyword keyword) {
    return doFilterByNumOfPlusOne(keyword, PlusPeople::getNumOfPlusOne);
  }
}
