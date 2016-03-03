package jp.leopanda.gPlusAnalytics.client.enums;

/**
 * パネルに使用する CSSのスタイル名
 * 
 * @author LeoPanda
 *
 */
public enum CssStyle {
  NONE(""), NORMAL("normal"), LABEL_NORMAL("headerLabel"), LABEL_TITLE("titleLabel"), LABEL_COUNT(
      "countLabel"), LABEL_FILTER("filterLabel"), BUTTON_PAGE("pageButton"), BUTTON_FILTER(
      "filterButton"), BUTTON_RESET("resetButton");

  private String styleName;

  private CssStyle(String styleName) {
    this.styleName = styleName;
  }

  public String getName() {
    return styleName;
  }
}
