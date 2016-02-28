package jp.leopanda.gPlusAnalytics.dataObject;

import java.util.List;

public interface PlusListCommon<T extends PlusItem> {

  public List<T> getItems();

  public List<T> deposit(List<T> deposition);

  public void setItems(List<T> allItems);


}
