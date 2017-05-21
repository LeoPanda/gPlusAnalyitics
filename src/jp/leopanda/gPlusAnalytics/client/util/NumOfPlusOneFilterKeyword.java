package jp.leopanda.gPlusAnalytics.client.util;

import jp.leopanda.gPlusAnalytics.client.enums.CompOperator;

/**
 * +1数フィルターキーワードオブジェクト
 * 
 * @author LeoPanda
 *
 */
public class NumOfPlusOneFilterKeyword {
  int numOfPlusOne;
  CompOperator comparator;

  /**
   * 
   */
  public NumOfPlusOneFilterKeyword(int numOfPlusOne, CompOperator comparator) {
    this.numOfPlusOne = numOfPlusOne;
    this.comparator = comparator;
  }

  public int getNumOfPlusOne() {
    return numOfPlusOne;
  }

  public CompOperator getComparator() {
    return comparator;
  }

}
