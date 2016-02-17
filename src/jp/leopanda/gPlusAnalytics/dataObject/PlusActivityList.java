package jp.leopanda.gPlusAnalytics.dataObject;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PlusActivityList extends PlusItemList<PlusActivity> implements IsSerializable{
	public String kind;
	public String title;
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
	  public void setItems(List<PlusActivity> items){
	    this.items = items;
	  }
	 /*
	  * deposit to another DO
	  */
	  public List<PlusActivity> deposit(List<PlusActivity> deposition){
	    for (PlusActivity item : this.items) {
	      deposition.add(item);
	    }
	    return deposition;
	  }
}
