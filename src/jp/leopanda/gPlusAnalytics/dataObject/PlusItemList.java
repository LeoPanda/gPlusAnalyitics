package jp.leopanda.gPlusAnalytics.dataObject;

import java.util.List;

public abstract class PlusItemList<T extends PlusItem> implements PlusListCommon<T> {
	public String nextPageToken;
  /*
   * getter 
   */
  public String getNextPageToken() {
    return nextPageToken;
  }
  public abstract List<T> getItems();
  /*
   * setter
   */
  public abstract void setItems(List<T> items);
 /*
  * deposit to another DO
  */
  public abstract List<T> deposit(List<T> deposition);
}
