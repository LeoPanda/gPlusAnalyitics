package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ChartWrapper;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.options.Options;

/**
 * @author LeoPanda
 *
 */
public abstract class SimpleChart<O extends Options> extends ChartOnMenu {
  protected ChartWrapper<O> chart;
  protected O chartOptions;

  /**
   * コンストラクタ
   */
  public SimpleChart(final ChartType chartType, ChartPackage chartPackage) {
    super();
    ChartLoader chartLoader = new ChartLoader(chartPackage);
    chartLoader.loadApi(new Runnable() {
      @Override
      public void run() {
        beforeDrawChart();
        addChartToPanel(chartType);
        draw();
        afterDrawChart();
      }

    });
  }

  /**
   * チャートウィジェットをパネルに追加
   */
  protected void addChartToPanel(ChartType chartType) {
    add(getChart(chartType));
  }

  /**
   * チャートの生成
   * 
   * @return チャートラッパー
   */
  protected ChartWrapper<O> getChart(ChartType chartType) {
    if (chart == null) {
      chart = new ChartWrapper<O>();
      chart.setChartType(chartType);
    }
    return chart;
  }

  /**
   * チャート描画前処理
   */
  protected void beforeDrawChart() {}

  /**
   * チャート描画処理
   */
  protected void draw() {
    // グラフの設定
    chart.setOptions(getChartOptions());
    // グラフ描画
    chart.setDataTable(getDataTable());
    chart.draw();
  }

  /**
   * チャート描画後処理
   */
  protected void afterDrawChart() {}

  /**
   * チャートオプションの生成
   * 
   * @return チャートオプション
   */
  @SuppressWarnings("unchecked")
  protected O getChartOptions() {
    chartOptions = (O) O.create();
    chartOptions.setHeight(getChartHeight());
    chartOptions.setWidth(getChartWidth());
    return chartOptions;
  }

  /**
   * データテーブルの生成
   * 
   * @return データテーブル
   */
  protected abstract  DataTable getDataTable();

}
