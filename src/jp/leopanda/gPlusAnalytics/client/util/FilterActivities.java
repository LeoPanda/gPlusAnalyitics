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
public class FilterActivities {

  /**
   * 単一+1erでフィルタする
   * 
   * @param plusActivites
   * @param plusPeople
   * @return
   */
  public List<PlusActivity> byPlusOner(List<PlusActivity> plusActivites, PlusPeople plusPeople) {
    return new ItemFilter<PlusActivity, String>() {
      @Override
      public boolean compare(PlusActivity plusActivity, String plusOnerId) {
        return plusActivity.getPlusOnerIds().contains(plusOnerId);
      }
    }.doFilter(plusActivites, plusPeople.getId());
  }

  /**
   * キーワードでフィルタする
   * 
   * @param plusActivites
   * @param keyword
   * @return
   */
  public List<PlusActivity> byKeyword(List<PlusActivity> plusActivites, String keyword) {
    return new ItemFilter<PlusActivity, String>() {
      @Override
      public boolean compare(PlusActivity sourceItem, String keyword) {
        return sourceItem.getTitle().contains(keyword);
      }
    }.doFilter(plusActivites, keyword);
  }

  /**
   * 投稿先でフィルタする
   * @param plusActivites
   * @param keyword
   * @return
   */
  public List<PlusActivity> byAccessDescription(List<PlusActivity> plusActivites, String keyword) {
    return new ItemFilter<PlusActivity, String>() {
      @Override
      public boolean compare(PlusActivity sourceItem, String keyword) {
        return Formatter.getBeforeBracket(sourceItem.getAccessDescription()).equals(keyword);
      }
    }.doFilter(plusActivites, keyword);
  }
  
  /**
   * 投稿日でフィルタする
   * @param plusActivites
   * @param year
   * @param month
   * @return
   */
  public List<PlusActivity> byPublished(List<PlusActivity> plusActivites,String year, String month){
    List<PlusActivity> currentPlusActivites = new ItemFilter<PlusActivity, String>() {
      @Override
      public boolean compare(PlusActivity sourceItem, String year) {
        return Formatter.getYear(sourceItem.getPublished()).equals(year);
      }
    }.doFilter(plusActivites, year);

    return new ItemFilter<PlusActivity, String>() {
      @Override
      public boolean compare(PlusActivity item, String month) {
        String publishedMonth = Formatter.getMonth(item.getPublished());
        return publishedMonth.equals(month);
      }
    }.doFilter(currentPlusActivites, month);
  }
  
  /**
   * リストで示されたユーザーが+1したアクテビティを抽出する
   * @param plusActivities
   * @param plusOners
   * @return
   */
  public List<PlusActivity> byPlusOners(List<PlusActivity> plusActivities,List<PlusPeople> plusOners){
    Set<String> activityIds = new HashSet<String>();
    for (PlusPeople plusOner : plusOners) {
      for(PlusActivity plusActivity:plusActivities){
        if(plusActivity.getPlusOnerIds().contains(plusOner.getId())){
          activityIds.add(plusActivity.getId());
        }
      }  
    }
    return byActivityIds(plusActivities,activityIds);
  }
  
  
  /**
   * ActivityIDのセットでフィルタする
   * @param plusActivites
   * @param activityId
   * @return
   */
  private List<PlusActivity> byActivityIds(List<PlusActivity> plusActivities,Set<String> activityIds){
    return new ItemFilter<PlusActivity,Set<String>>(){

      @Override
      public boolean compare(PlusActivity sourceItem, Set<String> activityIds) {
        return activityIds.contains(sourceItem.getId());
      }
      
    }.doFilter(plusActivities, activityIds);
  }
}
