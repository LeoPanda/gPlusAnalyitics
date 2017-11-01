package jp.leopanda.gPlusAnalytics.client.util;

import jp.leopanda.gPlusAnalytics.client.enums.CompOperator;
import jp.leopanda.gPlusAnalytics.client.enums.ItemType;

/**
 * +1数フィルターキーワードオブジェクト
 * 
 * @author LeoPanda
 *
 */
public class NumOfPlusOneFilterKeyword {
  int numOfPlusOne;
  CompOperator comparator;
  ItemType itemType;

  /**
   * 
   */
  public NumOfPlusOneFilterKeyword(int numOfPlusOne, CompOperator comparator,ItemType itemType) {
    this.numOfPlusOne = numOfPlusOne;
    this.comparator = comparator;
    this.itemType = itemType;
  }

  public int getNumOfPlusOne() {
    return numOfPlusOne;
  }

  public CompOperator getComparator() {
    return comparator;
  }
  
  public ItemType getItemType(){
    return itemType;
  }

}
