package jp.leopanda.gPlusAnalytics.client.chart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jp.leopanda.gPlusAnalytics.client.chart.abstracts.RangeFilterdChart;
import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.client.enums.FixedString;
import jp.leopanda.gPlusAnalytics.client.util.SortComparator;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

import com.google.gwt.user.client.Window;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ChartWrapper;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterUi;
import com.googlecode.gwt.charts.client.corechart.LineChartOptions;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;
import com.googlecode.gwt.charts.client.options.TextPosition;
import com.googlecode.gwt.charts.client.options.VAxis;

/**
 * リピーター比率の変化トレンドをグラフ化する
 * 
 * @author LeoPanda
 *
 */
public class ActivityLineChart extends
    RangeFilterdChart<PlusActivity, LineChartOptions> {
  private Map<Integer, String> activityUrls = new HashMap<Integer, String>();
  Logger logger = Logger.getLogger("ActivityLineChart");

  /**
   * コンストラクタ
   */
  public ActivityLineChart() {
    super(ChartType.LINE);
    setChartFunction(chart -> getChart(chart));
    setDataTableFunction(dataTable -> getDataTable(dataTable));
    setChartOptionsFunction(chartOptions -> getChartOptions(chartOptions));
    setRangeFilterUiFunction(rangeFilterUi -> setRangeFilterUi(rangeFilterUi));
  }

  /**
   * チャートの設定
   * @param chart
   * @return
   */
  private ChartWrapper<LineChartOptions> getChart(ChartWrapper<LineChartOptions> chart) {
    chart.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        Integer selectedRow = (int) chart.getDataTable()
            .getValueNumber(chart.getSelection().get(0).getRow(), 0);
        Window.open(activityUrls.get(selectedRow), "", FixedString.WINDOW_OPTION.getValue());
      }
    });
    return chart;
  }

  /**
   * チャートオプションの設定
   * @param chartOptions
   * @return
   */
  private LineChartOptions getChartOptions(LineChartOptions chartOptions) {
    chartOptions.setTitle(getChartTitle());
    chartOptions.setLegend(Legend.create(LegendPosition.TOP));
    HAxis haxis = HAxis.create("アクテビティ");
    haxis.setTextPosition(TextPosition.NONE);
    chartOptions.setHAxis(haxis);
    chartOptions.setVAxes(VAxis.create("リピータの＋１数比率"));
    return chartOptions;

  }
  /**
   * レンジフィルターUIを設定する
   * @param rangeFilterUi
   * @return
   */
  private ChartRangeFilterUi setRangeFilterUi(ChartRangeFilterUi rangeFilterUi){
    setRangeValues(5, getSourceData().size(), 50);
    rangeFilterUi.setChartType(ChartType.LINE);
    rangeFilterUi.setChartOptions(getFilterUiOptions());
    return rangeFilterUi;
  }

  /**
   * レンジフィルターUIのチャートオプションを設定する
   * @return
   */
  private LineChartOptions getFilterUiOptions() {
    LineChartOptions filterChartOptions = LineChartOptions.create();
    filterChartOptions.setHeight(100);
    HAxis haxis = HAxis.create();
    haxis.setTextPosition(TextPosition.NONE);
    filterChartOptions.setHAxis(haxis);
    filterChartOptions.setVAxes(VAxis.create(""));
    return filterChartOptions;
  }

  /**
   * チャートのデータテーブルを設定する
   * @param dataTable
   * @return
   */
  private DataTable getDataTable(DataTable dataTable) {
    List<PlusActivity> activities = new ArrayList<PlusActivity>(getSourceData());
    Collections.sort(activities, new SortComparator().getAscendingActivitesOrder());
    dataTable.addColumn(ColumnType.NUMBER, "No.");
    dataTable.addColumn(ColumnType.NUMBER, Distribution.FIRST_LOOKER.name);
    dataTable.addColumn(ColumnType.NUMBER, Distribution.LOW_MIDDLE_LOOKER.name);
    dataTable.addColumn(ColumnType.NUMBER, Distribution.HIGH_MIDDLE_LOOKER.name);
    dataTable.addColumn(ColumnType.NUMBER, Distribution.HIGH_LOOKER.name);
    dataTable.addRows(activities.size() + 1);
    int row = 1;
    for (PlusActivity activity : activities) {
      activityUrls.put(row, activity.getUrl());
      dataTable.setValue(row, 0, row);
      dataTable.setValue(row, 1,
          getRepeaterPercentage(activity.getNumOfPlusOners(), activity.getFirstLookers()));
      dataTable.setValue(row, 2,
          getRepeaterPercentage(activity.getNumOfPlusOners(), activity.getLowMiddleLookers()));
      dataTable.setValue(row, 3,
          getRepeaterPercentage(activity.getNumOfPlusOners(), activity.getHighMiddleLookers()));
      dataTable.setValue(row, 4,
          getRepeaterPercentage(activity.getNumOfPlusOners(), activity.getHighLookers()));
      row += 1;
    }
    return dataTable;
  }

  /**
   * リピーター比率を求める
   * 
   * @param numOfPlusOners
   * @param numOfRepeaters
   * @return
   */
  private double getRepeaterPercentage(int numOfPlusOners, int numOfRepeaters) {
    return 100 * (double) numOfRepeaters / (double) numOfPlusOners;
  }

}
