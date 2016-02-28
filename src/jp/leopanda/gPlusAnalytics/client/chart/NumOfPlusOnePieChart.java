package jp.leopanda.gPlusAnalytics.client.chart;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.chart.abstracts.SimpleChart;
import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

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
 * アクテビティへの＋１数をグラフ化する
 * 
 * @author LeoPanda
 *
 */
public class NumOfPlusOnePieChart extends SimpleChart<PieChartOptions> {
  private int totalPlusOne = 0;

  /**
   * コンストラクタ
   */
  public NumOfPlusOnePieChart() {
    super(ChartType.PIE, ChartPackage.CORECHART);
  }

  protected void afterDrawChart() {
    add(new Label("総+1数:" + NumberFormat.getDecimalFormat().format(totalPlusOne)));
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
    for (PlusActivity activity : Global.getActivityItems()) {
      first += activity.getFirstLookers();
      lowMiddle += activity.getLowMiddleLookers();
      highMiddle += activity.getHighMiddleLookers();
      high += activity.getHighLookers();
    }
    totalPlusOne = (first + lowMiddle + highMiddle + high);
    DataTable dataTable = DataTable.create();
    dataTable.addColumn(ColumnType.STRING, "+1分布");
    dataTable.addColumn(ColumnType.NUMBER, "ユーザー数");
    dataTable.addRow(Distribution.FIRST_LOOKER.name, first);
    dataTable.addRow(Distribution.LOW_MIDDLE_LOOKER.name, lowMiddle);
    dataTable.addRow(Distribution.HIGH_MIDDLE_LOOKER.name, highMiddle);
    dataTable.addRow(Distribution.HIGH_LOOKER.name, high);
    return dataTable;
  }
}
