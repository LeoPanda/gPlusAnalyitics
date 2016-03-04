package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.chart.abstracts.SimpleChart;
import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.PieChartOptions;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;

/**
 * +1ユーザーの累積＋１数分布状況をグラフ化する
 * 
 * @author LeoPanda
 *
 */
public class PlusOnersPieChart extends SimpleChart<PlusPeople, PieChartOptions> {
  private int totalPlusOners = 0;

  /**
   * コンストラクタ
   */
  public PlusOnersPieChart() {
    super(ChartType.PIE, ChartPackage.CORECHART);
  }

  @Override
  protected void afterDrawChart() {
    add(new Label("総ユーザー数:" + NumberFormat.getDecimalFormat().format(totalPlusOners)));
  }

  /*
   * グラフのオプションを作成する
   */
  protected PieChartOptions getChartOptions() {
    super.getChartOptions();
    chartOptions.setTitle(getChartTitle());
    chartOptions.setLegend(Legend.create(LegendPosition.TOP));
    return chartOptions;
  }

  /*
   * グラフに表示するデータをセットする
   */
  protected DataTable getDataTable() {
    int first = 0;
    int lowMiddle = 0;
    int highMiddle = 0;
    int high = 0;
    for (PlusPeople plusOner : sourceItems) {
      totalPlusOners += 1;
      int plusOne = plusOner.getNumOfPlusOne();
      if (plusOne >= Distribution.HIGH_LOOKER.threshold) {
        high += 1;
      } else if (plusOne >= Distribution.HIGH_MIDDLE_LOOKER.threshold) {
        highMiddle += 1;
      } else if (plusOne >= Distribution.LOW_MIDDLE_LOOKER.threshold) {
        lowMiddle += 1;
      } else {
        first += 1;
      }
    }
    dataTable = super.getDataTable();
    dataTable.addColumn(ColumnType.STRING, "+1分布");
    dataTable.addColumn(ColumnType.NUMBER, "ユーザー数");
    dataTable.addRow(Distribution.FIRST_LOOKER.name, first);
    dataTable.addRow(Distribution.LOW_MIDDLE_LOOKER.name, lowMiddle);
    dataTable.addRow(Distribution.HIGH_MIDDLE_LOOKER.name, highMiddle);
    dataTable.addRow(Distribution.HIGH_LOOKER.name, high);
    return dataTable;
  }

  /*
   * 再描画する
   */
  @Override
  public void reDraw() {
    remove(this.getWidgetCount() - 1);
    totalPlusOners = 0;
    super.reDraw();
  }
}
