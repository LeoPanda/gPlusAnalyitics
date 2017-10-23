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
  PHOTO_CELL(CssStyle.INSTANCE.style().photoCell()),
  CHECKBOX_LABEL(CssStyle.INSTANCE.style().cehckBoxLabel()),
  RESET_BUTTON(CssStyle.INSTANCE.style().resetButton()),
  NEXTPAGE_BUTTON(CssStyle.INSTANCE.style().nextPageButton()),
  YEAR_LABEL(CssStyle.INSTANCE.style().yearLabel()),
  MONTH_LABEL(CssStyle.INSTANCE.style().monthLabel()),
  COUNT_LABLEL(CssStyle.INSTANCE.style().countLabel()),
  TABLE_TEXT(CssStyle.INSTANCE.style().tableText()),
  TABLE_PHOTO(CssStyle.INSTANCE.style().tablePhoto()),
  FILTER_BUTTON(CssStyle.INSTANCE.style().filterButton()),
  FILTER_DISABLE(CssStyle.INSTANCE.style().filterDisable()),
  FILTER_CARD(CssStyle.INSTANCE.style().filterLogCard()),
  FILTER_LABEL(CssStyle.INSTANCE.style().filterLabel()),
  FILTER_NUMERIC(CssStyle.INSTANCE.style().filterNumeric()),
  FILTER_BOOLEAN(CssStyle.INSTANCE.style().filterBoolean()),
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
