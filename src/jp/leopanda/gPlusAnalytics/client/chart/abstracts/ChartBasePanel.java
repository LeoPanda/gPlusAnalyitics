package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import jp.leopanda.gPlusAnalytics.client.Statics;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.gPlusAnalytics.interFace.Drawable;

import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ChartWrapper;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.options.Options;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * チャートの共通パネル
 * 
 * @author LeoPanda
 *
 */
public abstract class ChartBasePanel<I extends PlusItem, O extends Options> extends VerticalPanel
    implements Drawable<I> {
  private ChartWrapper<O> chart;
  private ChartType chartType;
  private O chartOptions;
  private DataTable dataTable;
  private ChartLoader chartLoader;
  private List<I> sourceData;

  private int chartHeight;
  private int chartWidth;
  private String chartTitle;

  private UnaryOperator<ChartWrapper<O>> chartSetter;
  private UnaryOperator<O> chartOptionSetter;
  private UnaryOperator<DataTable> dataTableSetter;

  /**
   * コンストラクタ
   * 
   * @param sourceData
   */
  public ChartBasePanel(ChartType chartType, ChartPackage chartPackage) {
    this.chartLoader = new ChartLoader(chartPackage);
    this.chartType = chartType;
  }

  /**
   * チャートの設定関数をセットする
   * 
   * @param chartSetter
   */
  protected void setChartFunction(UnaryOperator<ChartWrapper<O>> chartSetter) {
    this.chartSetter = chartSetter;
  }

  /**
   * チャートのオプション設定関数をセットする
   * 
   * @param chartOptionSetter
   */
  protected void setChartOptionsFunction(UnaryOperator<O> chartOptionSetter) {
    this.chartOptionSetter = chartOptionSetter;
  }

  /**
   * チャートのソースデータ設定関数をセットする
   * 
   * @param dataTableSetter
   */
  protected void setDataTableFunction(UnaryOperator<DataTable> dataTableSetter) {
    this.dataTableSetter = dataTableSetter;

  }

  /**
   * チャートを描画する
   */
  public void draw() {
    if (getSourceData().size() > 0) {
      chartLoader.loadApi(() -> runDraw(getChart(), dataTableSetter.apply(getDataTable())));
    } else {
      add(new Label("表示するデータがありません。"));
    }
  }

  /**
   * チャート描画処理をセットする
   */
  abstract void runDraw(ChartWrapper<O> chart, DataTable dataTable);

  /**
   * チャートを再描画する
   */
  public void reDraw() {
    resetChart();
    draw();
  }

  /**
   * チャートをリセットする
   */
  public void resetChart() {
    clearDataTable();
    getChart().removeAllHandlers();
    super.clear();
  }

  /**
   * チャートの取得
   * 
   * @return チャートラッパー
   */
  private ChartWrapper<O> getChart() {
    chart = Optional.ofNullable(chart).orElse(new ChartWrapper<O>());
    chart.setChartType(chartType);
    Optional.ofNullable(chartSetter)
        .ifPresent(setter -> chart = setter.apply(chart));
    Optional.ofNullable(chartOptionSetter)
        .ifPresent(setter -> chart.setOptions(setter.apply(getChartOptions())));
    return chart;
  }

  /**
   * チャートオプションの生成
   * 
   * @return チャートオプション
   */
  @SuppressWarnings("unchecked")
  private O getChartOptions() {
    chartOptions = Optional.ofNullable(chartOptions).orElse((O) O.create());
    chartOptions.setHeight(getChartHeight());
    chartOptions.setWidth(getChartWidth());
    return chartOptions;
  }

  /**
   * データテーブルの取得
   * 
   * @return データテーブル
   */
  private DataTable getDataTable() {
    dataTable = Optional.ofNullable(dataTable).orElse(DataTable.create());
    return dataTable;
  }

  /**
   * データテーブルをクリアする
   */
  private void clearDataTable() {
    Optional.ofNullable(dataTable).ifPresent(dataTable -> {
      dataTable.removeRows(0, dataTable.getNumberOfRows());
      dataTable.removeColumns(0, dataTable.getNumberOfColumns());
    });
  }

  /**
   * チャートのソースデータを取得する
   * 
   * @return
   */
  protected List<I> getSourceData() {
    return this.sourceData;
  }

  /**
   * チャートのソースデータをセットする
   * 
   * @param plusItemList
   */
  public void setSourceData(List<I> plusItemList) {
    this.sourceData = (List<I>) plusItemList;
  }

  /**
   * チャートのパネル表示属性をセットする
   * 
   * @param chartTitile
   * @param chartWidth
   * @param chartHeight
   */
  public void setOutlineSpec(String chartTitile, int chartWidth, int chartHeight) {
    this.chartTitle = chartTitile;
    this.chartWidth = chartWidth;
    this.chartHeight = chartHeight;
    setWidth(Statics.getLengthWithUnit(getChartWidth()));

  }

  /**
   * チャートの幅を取得する
   * 
   * @return
   */
  protected int getChartWidth() {
    return this.chartWidth;
  }

  /**
   * チャートの高さを取得する
   * 
   * @return
   */
  protected int getChartHeight() {
    return this.chartHeight;
  }

  /**
   * チャートのタイトルを取得する
   * 
   * @return
   */
  protected String getChartTitle() {
    return this.chartTitle;
  }

}
