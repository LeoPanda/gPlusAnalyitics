package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.enums.ChartBody;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * メニューに載せるチャートの共通パネル
 * 
 * @author LeoPanda
 *
 */
public abstract class ChartPanel<I extends PlusItem> extends VerticalPanel {
  private ChartBody chartBody;
  protected List<I> sourceItems;

  public void setMenuInfo(ChartBody chartBody) {
    this.chartBody = chartBody;
    setWidth(String.valueOf(chartBody.width) + "px");
  }

  protected int getChartWidth() {
    return this.chartBody.width;
  }

  protected int getChartHeight() {
    return this.chartBody.height;
  }

  protected String getChartTitle() {
    return this.chartBody.title;
  }

  /**
   * チャートを描画する
   * 
   * @param sourceItems チャートのソースデータ
   */
  public void draw(List<I> sourceItems) {
    this.sourceItems = sourceItems;
  }

  /**
   * チャートを再描画する
   */
  public abstract void reDraw();

}
