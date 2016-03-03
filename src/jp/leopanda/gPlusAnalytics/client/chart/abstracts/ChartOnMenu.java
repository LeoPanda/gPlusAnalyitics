package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.enums.ChartInfo;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * メニューに載せるチャートの共通パネル
 * 
 * @author LeoPanda
 *
 */
public abstract class ChartOnMenu<I extends PlusItem> extends VerticalPanel {
  private ChartInfo chartInfo;
  protected List<I> sourceItems;

  public void setMenuInfo(ChartInfo chartInfo) {
    this.chartInfo = chartInfo;
    setWidth(String.valueOf(chartInfo.width) + "px");
  }

  protected int getChartWidth() {
    return this.chartInfo.width;
  }

  protected int getChartHeight() {
    return this.chartInfo.height;
  }

  protected String getChartTitle() {
    return this.chartInfo.title;
  }

  /**
   * チャートを描画する
   * 
   * @param sourceItems
   */
  public void draw(List<I> sourceItems) {
    this.sourceItems = sourceItems;
  };

  /**
   * チャートを再描画する
   */
  public abstract void reDraw();

}
