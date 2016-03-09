package jp.leopanda.gPlusAnalytics.client.panel.abstracts;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;

/**
 * HTMLを表示するテーブルカラム
 * 
 * @author LeoPanda
 *
 */
public abstract class SafeHtmlColumn<I> extends Column<I, SafeHtml> {
  static final SafeHtmlCell cell = new SafeHtmlCell();

  public SafeHtmlColumn() {
    super(cell);
  }
}
