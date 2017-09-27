package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ChartWrapper;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.options.Options;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * 単体チャートのアブストラクトクラス
 * 
 * @author LeoPanda
 *
 */
public abstract class SimpleChart<I extends PlusItem, O extends Options>
    extends ChartBasePanel<I, O> {

  /**
   * コンストラクタ
   */
  public SimpleChart(ChartType chartType, ChartPackage chartPackage) {
    super(chartType, chartPackage);
  }

  /*
   * チャート描画処理
   */
  @Override
  void runDraw(ChartWrapper<O> chart, DataTable dataTable) {
    add(chart);
    chart.setDataTable(dataTable);
    chart.draw();
  }
}
