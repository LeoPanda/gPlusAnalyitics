package jp.leopanda.gPlusAnalytics.dataObject;

import java.io.Serializable;
import java.util.List;

/**
 * データストアに保管されているすべてのデータアイテム
 * 
 * @author LeoPanda
 *
 */
public class StoredItems implements Serializable {
  private static final long serialVersionUID = 1L;
  public List<PlusActivity> activities;
  public List<PlusPeople> plusOners;

  public void setActivities(List<PlusActivity> activties) {
    this.activities = activties;
  }

  public void setPlusOners(List<PlusPeople> plusOners) {
    this.plusOners = plusOners;
  }
}
