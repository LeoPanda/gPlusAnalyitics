package jp.leopanda.gPlusAnalytics.client.enums;

import jp.leopanda.gPlusAnalytics.client.cssResource.CssStyle;

/**
 * パネルに使用する CSSのスタイル
 * 
 * @author LeoPanda
 *
 */
public enum MyStyle {
  PAGE_BUTTON(CssStyle.INSTANCE.style().pageButton()),
  TITLE_LABEL(CssStyle.INSTANCE.style().titleLabel()),
  COUNT_LABLEL(CssStyle.INSTANCE.style().countLabel()),
  FILTER_BUTTON(CssStyle.INSTANCE.style().filterButton()),
  RESET_BUTTON(CssStyle.INSTANCE.style().resetButton()),
  FILTER_TEXT(CssStyle.INSTANCE.style().filterLogText()),
  FILTER_LABEL(CssStyle.INSTANCE.style().filterLabel());
  private String style;

  private MyStyle(String style) {
    CssStyle.INSTANCE.style().ensureInjected();
    this.style = style;
  }

  public String getStyle() {
    return style;
  }
}
