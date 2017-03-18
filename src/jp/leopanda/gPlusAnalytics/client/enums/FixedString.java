package jp.leopanda.gPlusAnalytics.client.enums;

/**
 * 固定文字列
 * 
 * @author LeoPanda
 *
 */
public enum FixedString {
  WINDOW_OPTION("width=800,height=800,top=100,left=800,location=0"),
  BLANK_CELL("<br/>&nbsp;&nbsp;&nbsp;<br/>");

  private String value;

  private FixedString(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

}
