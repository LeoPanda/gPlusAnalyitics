package jp.leopanda.gPlusAnalytics.dataObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class PlusActivityList extends PlusItemList<PlusActivity> implements Serializable {
  private static final long serialVersionUID = 1L;
  public Date updated;
  public List<PlusActivity> items;

  /*
   * getter
   */
  public List<PlusActivity> getItems() {
    return items;
  }

  /*
   * setter
   */
  public void setItems(List<PlusActivity> items) {
    this.items = items;
  }

  /* 
   * Itemを累積する
   */
  @Override
  public List<PlusActivity> deposit(List<PlusActivity> deposition) {
    for (PlusActivity item : this.items) {
      deposition.add(item);
    }
    return deposition;
  }
}
