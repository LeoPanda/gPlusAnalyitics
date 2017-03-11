package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.corechart.ComboChartOptions;
import com.googlecode.gwt.charts.client.options.Options;
import com.googlecode.gwt.charts.client.options.SeriesType;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * レンジフィルターにカラムチャートを使用したチャート
 * 
 * @author LeoPanda
 *
 */
public abstract class ColumnChartRangeFilterdChart<I extends PlusItem, O extends Options> extends
    ChartRangeFilterdChart<I, O> {
  protected ComboChartOptions filterChartOptions; // レンジフィルターのチャートオプション

  /**
   * コンストラクタ
   * 
   * @param chartType
   *          チャートの種類
   */
  public ColumnChartRangeFilterdChart(ChartType chartType) {
    super(chartType);
  }

  /*
   * レンジフィルターの初期設定
   */
  @Override
  protected void setRangeFilter() {
    super.setRangeFilter();
    filterUi.setChartType(ChartType.COMBO);
    filterUi.setChartOptions(presetFilterChartOptions());
  }

  /*
   * レンジフィルターのチャートオプションをカラムチャート用にセットする
   */
  private ComboChartOptions presetFilterChartOptions() {
    if (filterChartOptions == null) {
      filterChartOptions = ComboChartOptions.create();
      filterChartOptions.setWidth(getChartWidth());
      filterChartOptions.setSeriesType(SeriesType.BARS);
    }
    return filterChartOptions;
  }
}
