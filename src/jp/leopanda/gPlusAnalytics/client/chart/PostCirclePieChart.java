package jp.leopanda.gPlusAnalytics.client.chart;

import com.googlecode.gwt.charts.client.corechart.PieChartOptions;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;

import jp.leopanda.gPlusAnalytics.client.chart.abstracts.AggregatePieChart;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * @author LeoPanda
 *
 */
public class PostCirclePieChart extends AggregatePieChart<PlusActivity> {

  /**
   * 投稿先集計パイチャート
   */
  public PostCirclePieChart() {
    super();
  }

  /*
   * チャートラベルの位置を設定
   */
  @Override
  protected PieChartOptions getChartOptions() {
    PieChartOptions options = super.getChartOptions();
    options.setLegend(Legend.create(LegendPosition.NONE));
    return options;
  }

  /*
   * @see jp.leopanda.gPlusAnalytics.client.chart.AggregatePieChart#getTargetField
   * (jp.leopanda.gPlusAnalytics.dataObject.PlusItem)
   */
  @Override
  protected String getTargetField(PlusActivity item) {
    return item.getAccessDescription();
  }

  /*
   * @see jp.leopanda.gPlusAnalytics.client.chart.AggregatePieChart#setFieldNameMap ()
   */
  @Override
  protected void setFieldAliasMap() {}

  
}
