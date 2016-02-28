package jp.leopanda.gPlusAnalytics.dataObject;


import java.io.Serializable;

/**
 * @author LeoPanda
 *
 */
public class ItemAccess implements Serializable {
  private static final long serialVersionUID = 1L;
  public String description;

  public String getDescription() {
    return description;
  }
}
