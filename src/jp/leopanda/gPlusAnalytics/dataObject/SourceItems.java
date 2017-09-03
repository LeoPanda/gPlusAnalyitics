package jp.leopanda.gPlusAnalytics.dataObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * データストアに保管されているすべてのデータアイテム
 * 
 * @author LeoPanda
 *
 */
public class SourceItems implements Serializable {
  private static final long serialVersionUID = 1L;
  public List<PlusActivity> activities;
  public List<PlusPeople> plusOners;

  /**
   * コンストラクタ
   */
  public SourceItems() {
    this.activities = new ArrayList<PlusActivity>();
    this.plusOners = new ArrayList<PlusPeople>();
  }

  public void setActivities(List<PlusActivity> activities) {
    this.activities = activities;
  }

  public void setPlusOners(List<PlusPeople> plusOners) {
    this.plusOners = plusOners;
  }
}
