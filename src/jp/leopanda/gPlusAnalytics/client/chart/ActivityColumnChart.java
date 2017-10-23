package jp.leopanda.gPlusAnalytics.client.chart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jp.leopanda.gPlusAnalytics.client.chart.abstracts.RangeFilterdChart;
import jp.leopanda.gPlusAnalytics.client.enums.Distribution;
import jp.leopanda.gPlusAnalytics.client.enums.FixedString;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

import com.google.gwt.user.client.Window;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ChartWrapper;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.controls.filter.ChartRangeFilterUi;
import com.googlecode.gwt.charts.client.corechart.ColumnChartOptions;
import com.googlecode.gwt.charts.client.corechart.ComboChartOptions;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;
import com.googlecode.gwt.charts.client.options.Bar;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;
import com.googlecode.gwt.charts.client.options.SeriesType;
import com.googlecode.gwt.charts.client.options.TextPosition;
import com.googlecode.gwt.charts.client.options.VAxis;

/**
 * アクテビティへの＋１数をグラフ化する
 * 
 * @author LeoPanda
 *
 */
public class ActivityColumnChart extends
    RangeFilterdChart<PlusActivity, ColumnChartOptions> {
  private Map<Integer, String> activityUrls = new HashMap<Integer, String>();
  private Bar columnBar;

  /**
   * コンストラクタ
   */
  public ActivityColumnChart() {
    super(ChartType.COLUMN);
    setChartFunction(chart -> getChart(chart));
    setChartOptionsFunction(chartOptions -> getChartOptions(chartOptions));
    setDataTableFunction(dataTable -> getDataTable(dataTable));
    setRangeFilterUiFunction(rangeFilterUi -> setRangeFilterUi(rangeFilterUi));
  }

  /**
   * チャートの設定
   * 
   * @param chart
   * @return
   */
  private ChartWrapper<ColumnChartOptions> getChart(ChartWrapper<ColumnChartOptions> chart) {
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
   * 
   * @param chartOptions
   * @return
   */
  private ColumnChartOptions getChartOptions(ColumnChartOptions chartOptions) {
    chartOptions.setTitle(getChartTitle());
    chartOptions.setLegend(Legend.create(LegendPosition.TOP));
    HAxis haxis = HAxis.create("アクテビティ");
    haxis.setTextPosition(TextPosition.NONE);
    chartOptions.setHAxis(haxis);
    chartOptions.setVAxes(VAxis.create("+1数"));
    // バーの設定
    chartOptions.setBar(getColumnBar());
    // 積み上げカラムに設定
    chartOptions.setIsStacked(true);
    return chartOptions;

  }

  /**
   * レンジフィルターUIを設定する
   * 
   * @param rangeFilterUi
   * @return
   */
  private ChartRangeFilterUi setRangeFilterUi(ChartRangeFilterUi rangeFilterUi) {
    setRangeValues(5, getSourceData().size(), 10);
    rangeFilterUi.setChartType(ChartType.COMBO);
    rangeFilterUi.setChartOptions(presetFilterChartOptions());
    return rangeFilterUi;
  }

  /**
   * レンジフィルターUIのチャートオプションを設定する
   * 
   * @return
   */
  private ComboChartOptions presetFilterChartOptions() {
    ComboChartOptions filterChartOptions = ComboChartOptions.create();
    filterChartOptions.setWidth(getChartWidth());
    filterChartOptions.setSeriesType(SeriesType.BARS);
    filterChartOptions.setHeight(100);
    HAxis haxis = HAxis.create();
    haxis.setTextPosition(TextPosition.NONE);
    filterChartOptions.setHAxis(haxis);
    filterChartOptions.setIsStacked(true);
    // フィルターチャートのカラムバーをチャートと同一に設定
    filterChartOptions.setBar(getColumnBar());
    return filterChartOptions;
  }

  /**
   * 共通カラムバー
   * 
   * @return
   */
  private Bar getColumnBar() {
    columnBar = Optional.ofNullable(columnBar).orElse(Bar.create());
    columnBar.setGroupWidth("98%");
    return columnBar;
  }

  /**
   * チャートのデータテーブルを設定する
   * 
   * @param dataTable
   * @return
   */
  private DataTable getDataTable(DataTable dataTable) {
    List<PlusActivity> activities = new ArrayList<PlusActivity>(getSourceData());
    Collections.sort(activities, Comparator.comparing(PlusActivity::getPublished));
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
      dataTable.setValue(row, 1, activity.getFirstLookers());
      dataTable.setValue(row, 2, activity.getLowMiddleLookers());
      dataTable.setValue(row, 3, activity.getHighMiddleLookers());
      dataTable.setValue(row, 4, activity.getHighLookers());
      row += 1;
    }
    return dataTable;
  }

}
