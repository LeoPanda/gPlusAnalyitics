package jp.leopanda.gPlusAnalytics.client.chart.abstracts;

import jp.leopanda.gPlusAnalytics.client.chart.abstracts.SimpleChart;
import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

import java.util.function.Consumer;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ChartWrapper;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.PieChartOptions;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;

/**
 * ＋１分布数値を集計してを円グラフにするための総称クラス
 * 
 * @author LeoPanda
 *
 */
public abstract class SummryDistributionValuesPieChart<I extends PlusItem>
    extends SimpleChart<I, PieChartOptions> {
  private String chartTitle;
  private String chartValueName;
  private Label summaryLabel;
  private Consumer<DistributionValues> summarySetter;

  /**
   * コンストラクタ
   */
  public SummryDistributionValuesPieChart(String chartTitle, String chartValueName) {
    super(ChartType.PIE, ChartPackage.CORECHART);
    this.chartTitle = chartTitle;
    this.chartValueName = chartValueName;
    setChartOptionsFunction(chartOptions -> getChartOptions(chartOptions));
    setDataTableFunction(dataTable -> getDataTable(dataTable));
  }

  /**
   * 集計ロジックの設定
   * 
   * @param summarySetter
   */
  protected void setSummaryFunction(Consumer<DistributionValues> summarySetter) {
    this.summarySetter = summarySetter;
  }

  /**
   * グラフのオプションを作成する
   * 
   * @param chartOptions
   * @return
   */
  private PieChartOptions getChartOptions(PieChartOptions chartOptions) {
    chartOptions.setTitle(getChartTitle());
    chartOptions.setLegend(Legend.create(LegendPosition.TOP));
    return chartOptions;
  }

  /**
   * データテーブルへの統計データ追加
   * 
   * @param dataTable
   */
  private DataTable getDataTable(DataTable dataTable) {
    DistributionValues values = new DistributionValues();
    summarySetter.accept(values);
    dataTable.addColumn(ColumnType.STRING, chartTitle);
    dataTable.addColumn(ColumnType.NUMBER, chartValueName);
    dataTable.addRow(Distribution.FIRST_LOOKER.name, values.first);
    dataTable.addRow(Distribution.LOW_MIDDLE_LOOKER.name, values.lowMiddle);
    dataTable.addRow(Distribution.HIGH_MIDDLE_LOOKER.name, values.highMiddle);
    dataTable.addRow(Distribution.HIGH_LOOKER.name, values.high);
    return dataTable;
  }

  /**
   * 集計表示ラベルのセット
   */
  protected void setSummaryLabel(String title, int value) {
    summaryLabel = new Label(title + NumberFormat.getDecimalFormat().format(value));
  }

  @Override
  void runDraw(ChartWrapper<PieChartOptions> chart, DataTable dataTable) {
    super.runDraw(chart, dataTable);
    add(summaryLabel);
  }

  @Override
  public void reDraw() {
    clear();
    super.reDraw();
  }

  /**
   *分布データ集計値クラス
   * @author LeoPanda
   *
   */
  public class DistributionValues {
    public int first = 0;
    public int lowMiddle = 0;
    public int highMiddle = 0;
    public int high = 0;
  }

}
