package jp.leopanda.gPlusAnalytics.client.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * アクテビティをフィルタする
 * 
 * @author LeoPanda
 *
 */
public class FilteredActivities extends ItemFilter<PlusActivity> {
  /**
   * @param sourceItems
   */
  public FilteredActivities(List<PlusActivity> sourceItems) {
    super(sourceItems);
  }

  /**
   * 単一+1erでフィルタする
   * 
   * @param plusPeople
   * @return
   */
  public List<PlusActivity> byPlusOner(PlusPeople plusPeople) {
    return doFilter(plusActivity -> plusActivity.getPlusOnerIds().contains(plusPeople.getId()));
  }

  /**
   * キーワードでフィルタする
   * 
   * @param keyword
   * @return
   */
  public List<PlusActivity> byKeyword(String keyword) {
    return doFilter(plusActivity -> plusActivity.getTitle().contains(keyword));
  }

  /**
   * 投稿先でフィルタする
   * 
   * @param plusActivites
   * @param keyword
   * @return
   */
  public List<PlusActivity> byAccessDescription(String keyword) {
    return doFilter(
        plusActivity -> Formatter.getBeforeBracket(plusActivity.getAccessDescription())
            .equals(keyword));
  }

  /**
   * 投稿年でフィルタする
   * 
   * @param plusActivites
   * @param year
   * @return
   */
  public List<PlusActivity> byPublishedYear(String year) {
    return doFilter(plusActivity -> Formatter.getYear(plusActivity.getPublished()).equals(year));
  }

  /**
   * 投稿月でフィルタする
   * 
   * @param month
   * @return
   */
  public List<PlusActivity> byPublishedMonth(String month) {
    return doFilter(plusActivity -> Formatter.getMonth(plusActivity.getPublished()).equals(month));
  }

  /**
   * リストで示されたユーザーが+1したアクテビティを抽出する
   * 
   * @param plusOners
   * @return
   */
  public List<PlusActivity> byPlusOners(List<PlusPeople> plusOners) {
    Set<String> activityIds = new HashSet<String>();
    for (PlusPeople plusOner : plusOners) {
      sourceItems.stream()
          .filter(plusActivity -> plusActivity.getPlusOnerIds().contains(plusOner.getId()))
          .forEach(plusActivity -> activityIds.add(plusActivity.getId()));
    }
    return doFilter(plusActivity -> activityIds.contains(plusActivity.getId()));
  }

}
