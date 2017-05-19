package jp.leopanda.gPlusAnalytics.client.util;

import java.util.Comparator;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

/**
 * アイテムリストをソートする比較演算子を提供する
 * 
 * @author LeoPanda
 *
 */
public class SortComparator {

  /**
   * アクテビティリストを最新投稿日順に列べる比較演算子
   * 
   * @return
   */
  public Comparator<PlusActivity> getLatestActivitesOrder() {
    return new Comparator<PlusActivity>() {

      @Override
      public int compare(PlusActivity o1, PlusActivity o2) {
        return o2.getPublished().compareTo(o2.getPublished());
      }
    };
  }

  /**
   * アクテビティリストを投稿日の昇順に列べる比較演算子
   * 
   * @return
   */
  public Comparator<PlusActivity> getAscendingActivitesOrder() {
    return new Comparator<PlusActivity>() {

      @Override
      public int compare(PlusActivity o1, PlusActivity o2) {
        return o2.getPublished().compareTo(o2.getPublished());
      }
    };
  }

  /**
   * +1ersリストを+1数の降順に列べる比較演算子
   * @return
   */
  public Comparator<PlusPeople> getPlusOnerDecendingOrder() {
    return new Comparator<PlusPeople>() {
      @Override
      public int compare(PlusPeople o1, PlusPeople o2) {
        return o2.getNumOfPlusOne() - o1.getNumOfPlusOne();
      }
    };
  }
}
