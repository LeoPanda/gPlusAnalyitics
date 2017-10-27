package jp.leopanda.gPlusAnalytics.client.panel.parts;

import com.google.gwt.user.client.ui.Widget;

/**
 * 写真パネル表示セル定義
 * 
 * @author LeoPanda
 *
 */
class PhotoCell {
  Widget cell;
  int width;

  PhotoCell(Widget cell, int width) {
    this.cell = cell;
    this.width = width;
  }

  public Widget getCell() {
    return cell;
  }

  public int getWidth() {
    return width;
  }
}