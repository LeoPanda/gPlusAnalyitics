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
  CHECKBOX_LABEL(CssStyle.INSTANCE.style().cehckBoxLabel()),
  RESET_BUTTON(CssStyle.INSTANCE.style().resetButton()),
  DIVIDER_LABEL(CssStyle.INSTANCE.style().dividerLabel()),
  COUNT_LABLEL(CssStyle.INSTANCE.style().countLabel()),
  TABLE_TEXT(CssStyle.INSTANCE.style().tableText()),
  TABLE_PHOTO(CssStyle.INSTANCE.style().tablePhoto()),
  FILTER_BUTTON(CssStyle.INSTANCE.style().filterButton()),
  FILTER_DISABLE(CssStyle.INSTANCE.style().filterDisable()),
  FILTER_CARD(CssStyle.INSTANCE.style().filterLogCard()),
  FILTER_LABEL(CssStyle.INSTANCE.style().filterLabel()),
  DETAIL_POPWINDOW(CssStyle.INSTANCE.style().detailPopWindow()),
  DETAIL_POPLINE(CssStyle.INSTANCE.style().detailPopLine()),
  GRID_IMAGE("gridImage");
  private String style;

  private MyStyle(String style) {
    CssStyle.INSTANCE.style().ensureInjected();
    this.style = style;
  }

  public String getStyle() {
    return style;
  }
}
