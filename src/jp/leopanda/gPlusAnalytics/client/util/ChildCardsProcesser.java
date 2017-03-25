package jp.leopanda.gPlusAnalytics.client.util;

import java.util.Iterator;

import com.google.gwt.user.client.ui.Widget;

import jp.leopanda.gPlusAnalytics.client.panel.FilterLogCard;
import jp.leopanda.gPlusAnalytics.client.panel.FilterLogPanel;

/**
 * フィルターログパネルに追加されたログカードを走査しそれぞれのカードに命令を送る
 * @author LeoPanda
 *
 */
public abstract class ChildCardsProcesser {
  FilterLogPanel parentPanel;

  public ChildCardsProcesser(FilterLogPanel parentPanel) {
    this.parentPanel = parentPanel;
  }

  /**
   * すべてのログカードを処理する
   */
  public void processAll() {
    Iterator<Widget> iterator = parentPanel.iterator();
    while (iterator.hasNext()) {
      Widget widget = iterator.next();
      if (widget.getClass() == FilterLogCard.class) {
        FilterLogCard card = (FilterLogCard) widget;
        cardProcess(card);
      }
    }
  }
  /**
   * それぞれのカードの個別処理
   * @param card
   */
  public abstract void cardProcess(FilterLogCard card);
}