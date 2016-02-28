package jp.leopanda.gPlusAnalytics.client.chart;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.leopanda.gPlusAnalytics.client.Global;
import jp.leopanda.gPlusAnalytics.client.chart.abstracts.SimpleChart;
import jp.leopanda.gPlusAnalytics.client.enums.WindowOption;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

import com.google.gwt.user.client.Window;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ChartType;
import com.googlecode.gwt.charts.client.ChartWrapper;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.calendar.CalendarOptions;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;

/**
 * アクテビティへの＋１数をカレンダー上に表示する
 * 
 * @author LeoPanda
 *
 */
public class ActivityCalendarChart extends SimpleChart<CalendarOptions> {
  private Map<Date, String> activityUrls = new HashMap<Date, String>();

  /**
   * コンストラクタ
   */
  public ActivityCalendarChart() {
    super(ChartType.CALENDAR, ChartPackage.CALENDAR);
  }

  /* 
   * グラフの作成
   */
  protected ChartWrapper<CalendarOptions> getChart(ChartType chartType) {
    chart = super.getChart(chartType);
    chart.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        if (chart.getSelection().get(0).getRow() != null) {
          Date selectedDate =
              chart.getDataTable().getValueDate(chart.getSelection().get(0).getRow(), 0);
          Window.open(activityUrls.get(selectedDate), "", WindowOption.ITEM_DETAIL.getValue());
        } else {
          Window.alert("この日は投稿がありません。");
        }
      }
    });
    return chart;
  }

  /* 
   * グラフのオプションを作成する
   */
  protected CalendarOptions getChartOptions() {
    chartOptions = super.getChartOptions();
    chartOptions.setTitle(getChartTitle());
    chartOptions.setNoDataPattern("#DDD", "#A0A0A0");
    return chartOptions;
  }

  /*
   * グラフに表示するデータをセットする
   * 
   */
  protected DataTable getDataTable() {
    DataTable dataTable = DataTable.create();
    dataTable.addColumn(ColumnType.DATE, "投稿日");
    dataTable.addColumn(ColumnType.NUMBER, "+1数");
    for (PlusActivity activity : Global.getActivityItems()) {
      dataTable.addRow(activity.getPublished(), activity.getNumOfPlusOners());
      activityUrls.put(activity.getPublished(), activity.getUrl());
    }
    return dataTable;
  }
}
