package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

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
public abstract class SimpleChart<I extends PlusItem, O extends Options> extends ChartOnMenu<I> {
  protected ChartWrapper<O> chart;
  protected O chartOptions;
  protected DataTable dataTable;
  private ChartType chartType;
  private ChartLoader chartLoader;

  /**
   * コンストラクタ
   */
  public SimpleChart(ChartType chartType, ChartPackage chartPackage) {
    super();
    this.chartType = chartType;
    this.chartLoader = new ChartLoader(chartPackage);
  }

  /**
   * チャートを描画する
   */
  @Override
  public void draw(List<I> sourceItems) {
    super.draw(sourceItems);
    chartLoader.loadApi(new Runnable() {
      @Override
      public void run() {
        beforeDrawChart();
        addChartToPanel(chartType);
        drawCore();
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
  protected void drawCore() {
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
  protected DataTable getDataTable() {
    if (dataTable == null) {
      dataTable = DataTable.create();
    }
    return dataTable;
  };

  /*
   * チャートを再描画する
   */
  @Override
  public void reDraw() {
    clearDataTable();
    chart.removeAllHandlers();
    draw(sourceItems);
  }

  /**
   * データテーブルをクリアする
   */
  protected void clearDataTable() {
    dataTable.removeRows(0, dataTable.getNumberOfRows());
    dataTable.removeColumns(0, dataTable.getNumberOfColumns());
  }

}
