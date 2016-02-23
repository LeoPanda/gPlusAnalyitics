package jp.leopanda.gPlusAnalytics.dataObject;

import java.io.Serializable;
import java.util.List;

public class PlusPeopleList extends PlusItemList<PlusPeople> implements Serializable{
	private static final long serialVersionUID = 1L;
public List<PlusPeople> items;
  public Integer totalItems;
  /*
   * getter 
   */
  public Integer getTotalItems() {
    return totalItems;
  }
  @Override
  public List<PlusPeople> getItems() {
		return this.items;
  }
  @Override
  public void setItems(List<PlusPeople> items) {
		this.items = items;
  }
  @Override
  public List<PlusPeople> deposit(List<PlusPeople> deposition) {
	    for (PlusPeople item : this.items) {
		      deposition.add(item);
		}
  return deposition;
  }
   
}


