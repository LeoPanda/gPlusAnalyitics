package jp.leopanda.gPlusAnalytics.client.chart;

import com.googlecode.gwt.charts.client.corechart.PieChartOptions;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;

import jp.leopanda.gPlusAnalytics.client.chart.abstracts.SummarySingleFieldPieChart;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * 投稿先集計パイチャート
 * 
 * @author LeoPanda
 */
public class PostCirclePieChart extends SummarySingleFieldPieChart<PlusActivity> {

  /**
   * コンストラクタ
   */
  public PostCirclePieChart() {
    super(item -> item.getAccessDescription());//集計項目の設定
    setChartOptionsFunction(chartOptions -> setChartOptions(chartOptions));
  }

  /**
   * チャートオプションの生成
   * 
   * @param chartOptions
   * @return
   */
  private PieChartOptions setChartOptions(PieChartOptions chartOptions) {
    chartOptions.setTitle(getChartTitle());
    chartOptions.setLegend(getLegend());
    return chartOptions;
  }

  /**
   * チャートラベルの設定
   * @return
   */
  private Legend getLegend() {
    Legend legend = Legend.create(LegendPosition.TOP);
    legend.setMaxLines(1);
    return legend;
  }
}
