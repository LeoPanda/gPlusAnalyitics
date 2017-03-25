package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.enums.ChartType;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItemList;

import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * チャートの共通パネル
 * 
 * @author LeoPanda
 *
 */
public abstract class ChartBase<I extends PlusItem> extends VerticalPanel {
  private ChartType chartType;
  protected List<I> sourceData;

  /**
   * コンストラクタ
   * 
   * @param sourceData
   */
  public ChartBase() {
  }

  /**
   * チャートのソースデータをセットする
   * 
   * @param plusItemList
   */
  @SuppressWarnings("unchecked")
  public void setSourceData(PlusItemList<?> plusItemList) {
    this.sourceData = (List<I>) plusItemList.getItems();
  }

  /**
   * パネルに表示するチャートの種類をセットする
   * 
   * @param chartType
   */
  public void setChartType(ChartType chartType) {
    this.chartType = chartType;
    setWidth(String.valueOf(chartType.width) + "px");
  }

  protected int getChartWidth() {
    return this.chartType.width;
  }

  protected int getChartHeight() {
    return this.chartType.height;
  }

  protected String getChartTitle() {
    return this.chartType.title;
  }

  /**
   * チャートを描画する
   * 
   * @param sourceItems
   *          チャートのソースデータ
   */
  public abstract void draw();

  /**
   * チャートを再描画する
   */
  public abstract void reDraw();

}